package com.vksfeng.quan.achievement.mapper;

import com.vksfeng.quan.achievement.pojo.entity.Achievement;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AchievementMapper {
    List<Achievement> findByType(String type);

    void insert(Achievement achievement);
}
