package com.vksfeng.quan.achievement.pojo.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Achievement {
    private Long id;
    private String code;
    private String name;
    private String description;
    private String iconUrl;
    private String type;
    private String ruleJson;
    private LocalDateTime createdAt;
}
