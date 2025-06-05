package com.vksfeng.quan.achievement.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserAchievementVO {
    private String name;
    private String description;
    private String iconUrl;
    private LocalDateTime achievedAt;
}
