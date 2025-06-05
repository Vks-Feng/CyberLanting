package com.vksfeng.quan.achievement.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static com.vksfeng.quan.constant.RedisConstant.ACHIEVEMENT_STREAM_KEY;

@Component
public class AchievementEventPublisher {

    @Autowired
    private StringRedisTemplate redisTemplate;

    public void publish(Long userId, String type, Map<String, Object> payload) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Map<String, String> message = new HashMap<>();
            message.put("userId", userId.toString());
            message.put("type", type);
            message.put("payload", objectMapper.writeValueAsString(payload == null ? Map.of() : payload));

            redisTemplate.opsForStream().add(ACHIEVEMENT_STREAM_KEY, message);

        } catch (Exception e) {
            // 生产环境中可以 log.error + 监控上报
            throw new RuntimeException("发布成就事件失败", e);
        }
    }
}

