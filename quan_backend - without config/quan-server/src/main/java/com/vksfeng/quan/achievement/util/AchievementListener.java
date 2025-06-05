package com.vksfeng.quan.achievement.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vksfeng.quan.achievement.mapper.AchievementMapper;
import com.vksfeng.quan.achievement.mapper.UserAchievementMapper;
import com.vksfeng.quan.achievement.pojo.entity.Achievement;
import com.vksfeng.quan.achievement.pojo.entity.AchievementTriggerEvent;
import com.vksfeng.quan.achievement.pojo.entity.RuleJson;
import com.vksfeng.quan.achievement.pojo.entity.UserAchievement;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.connection.stream.StreamReadOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

import static com.vksfeng.quan.constant.RedisConstant.ACHIEVEMENT_STREAM_KEY;

@Slf4j
@Component
public class AchievementListener {

    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private AchievementMapper achievementMapper;
    @Autowired
    private UserAchievementMapper userAchievementMapper;
    @Autowired
    private RuleEvaluator ruleEvaluator;
    @Autowired
    private AchievementContextBuilder contextBuilder;

    @PostConstruct
    public void startListen() {
        Executors.newSingleThreadExecutor().submit(() -> {
            log.info("成就监听器启动...");
            while (true) {
                try {
                    List<MapRecord<String, Object, Object>> records =
                            redisTemplate.opsForStream().read(StreamReadOptions.empty().block(Duration.ofSeconds(2)).count(10),
                                    StreamOffset.latest(ACHIEVEMENT_STREAM_KEY));

                    if (records != null) {
                        for (MapRecord<String, Object, Object> record : records) {
                            handleRecord(record);
                        }
                    }
                } catch (Exception e) {
                    log.error("Redis Stream 成就事件监听失败：", e);
                    break;
                }
            }
        });
    }

    private void handleRecord(MapRecord<String, Object, Object> record) throws JsonProcessingException {
        Map<Object, Object> value = record.getValue();
        AchievementTriggerEvent event = parseEvent(value);

        log.info("收到行为事件：{}", event);

        List<Achievement> achievements = achievementMapper.findByType(event.getType());

        if (achievements.isEmpty()) {
            log.info("没有找到类型为 {} 的成就", event.getType());
            return;
        }

        for (Achievement achievement : achievements) {
            // 是否已经达成
            if (userAchievementMapper.exists(event.getUserId(), achievement.getId())) continue;

            // 解析规则 JSON
            RuleJson rule = null;
            try {
                rule = new ObjectMapper().readValue(achievement.getRuleJson(), RuleJson.class);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

            // 构造上下文
            Map<String, Object> context = contextBuilder.buildContext(event);

            // 规则判断
            boolean passed = ruleEvaluator.evaluate(rule, context);

            if (passed) {
                log.info("用户 {} 达成成就：{}", event.getUserId(), achievement.getCode());

                userAchievementMapper.insert(new UserAchievement(null, event.getUserId(), achievement.getId(), LocalDateTime.now()));
                // TODO: 推送通知、插入日志、广播给好友
            }
        }
    }

    private AchievementTriggerEvent parseEvent(Map<Object, Object> value) throws JsonProcessingException {
        Long userId = Long.parseLong(value.get("userId").toString());
        String type = value.get("type").toString();
        String payloadStr = value.getOrDefault("payload", "{}").toString();
        Map<String, Object> payload = new ObjectMapper().readValue(payloadStr, new TypeReference<>() {});
        AchievementTriggerEvent event = new AchievementTriggerEvent();
        event.setUserId(userId);
        event.setType(type);
        event.setPayload(payload);
        return event;
    }
}
