package com.vksfeng.quan.achievement.pojo.dto;

import lombok.Data;

import java.util.Map;

@Data
public class AchievementDTO {
    private String code;
    private String name;
    private String description;
    private String iconUrl;
    private String type;
    private Map<String, Object> ruleJson;
}
