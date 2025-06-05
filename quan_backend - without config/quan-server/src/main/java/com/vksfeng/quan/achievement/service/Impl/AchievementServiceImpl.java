package com.vksfeng.quan.achievement.service.Impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vksfeng.quan.achievement.mapper.AchievementMapper;
import com.vksfeng.quan.achievement.mapper.UserAchievementMapper;
import com.vksfeng.quan.achievement.pojo.dto.AchievementDTO;
import com.vksfeng.quan.achievement.pojo.entity.Achievement;
import com.vksfeng.quan.achievement.pojo.vo.UserAchievementVO;
import com.vksfeng.quan.achievement.service.AchievementService;
import com.vksfeng.quan.context.BaseContext;
import com.vksfeng.quan.exception.NotLoginException;
import com.vksfeng.quan.result.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AchievementServiceImpl implements AchievementService {

    @Autowired
    private UserAchievementMapper userAchievementMapper;

    @Autowired
    private AchievementMapper achievementMapper;

    public Long getUserId() {
        Long userId = BaseContext.getCurrentId();
        if (userId == null) {
            throw new NotLoginException("用户未登录");
        }
        return userId;
    }

    @Override
    public Result<List<UserAchievementVO>> getUserAchievement() {
        List<UserAchievementVO> userAchievementVOList = userAchievementMapper.getUserAchievement(getUserId());
        return Result.success(userAchievementVOList);
    }

    @Override
    public Result insertAchievement(AchievementDTO achievementDTO) {
        if (getUserId() != 1) {
            return Result.error("权限不足");
        }
        Achievement achievement = new Achievement();
        BeanUtils.copyProperties(achievementDTO, achievement);
        try {
            ObjectMapper mapper = new ObjectMapper();
            achievement.setRuleJson(mapper.writeValueAsString(achievementDTO.getRuleJson()));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("规则 JSON 序列化失败", e);
        }
        achievement.setCreatedAt(LocalDateTime.now());
        achievementMapper.insert(achievement);
        return Result.success("插入成功");
    }
}
