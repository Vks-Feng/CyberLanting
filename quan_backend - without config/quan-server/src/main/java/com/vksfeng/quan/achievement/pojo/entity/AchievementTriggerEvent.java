package com.vksfeng.quan.achievement.pojo.entity;

import lombok.Data;

import java.util.Map;

@Data
public class AchievementTriggerEvent {
    private Long userId;
    private String type;
    private Map<String, Object> payload;
}
