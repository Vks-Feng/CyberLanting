package com.vksfeng.quan.user_pojo.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserLocation {
    private Long userId;
    private Double latitude;
    private Double longitude;
    private LocalDateTime lastUpdate;
}
