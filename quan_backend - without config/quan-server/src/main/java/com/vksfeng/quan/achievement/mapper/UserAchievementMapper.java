package com.vksfeng.quan.achievement.mapper;

import com.vksfeng.quan.achievement.pojo.entity.UserAchievement;
import com.vksfeng.quan.achievement.pojo.vo.UserAchievementVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserAchievementMapper {
    // 判断用户是否已经获得该成就
    boolean exists(Long userId, Long achievementId);

    // 插入用户获得成就记录
    void insert(UserAchievement userAchievement);

    List<UserAchievementVO> getUserAchievement(Long userId);
}
