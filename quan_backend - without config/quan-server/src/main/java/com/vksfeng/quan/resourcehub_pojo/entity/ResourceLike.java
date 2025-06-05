package com.vksfeng.quan.resourcehub_pojo.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ResourceLike {
    private Long id;
    private Long userId;
    private Long resourceId;
    private LocalDateTime createdAt;
}
