package com.vksfeng.quan.peerhub_pojo.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Friendship {
    private Long userId;
    private Long friendId;
    private String status;  // 'pending', 'accepted', 'blocked'
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
