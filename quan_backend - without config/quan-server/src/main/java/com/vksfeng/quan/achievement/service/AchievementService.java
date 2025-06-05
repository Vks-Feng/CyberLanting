package com.vksfeng.quan.achievement.service;

import com.vksfeng.quan.achievement.pojo.dto.AchievementDTO;
import com.vksfeng.quan.achievement.pojo.vo.UserAchievementVO;
import com.vksfeng.quan.result.Result;

import java.util.List;

public interface AchievementService {
    Result<List<UserAchievementVO>> getUserAchievement();

    Result insertAchievement(AchievementDTO achievementDTO);
}
