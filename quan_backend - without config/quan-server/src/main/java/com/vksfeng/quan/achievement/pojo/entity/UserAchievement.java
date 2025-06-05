package com.vksfeng.quan.achievement.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserAchievement {
    private Long id;
    private Long userId;
    private Long achievementId;
    private LocalDateTime achievedAt;
}
