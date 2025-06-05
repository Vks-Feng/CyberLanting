package com.vksfeng.quan.peerhub_pojo.entity;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class FeedLike {
    private Long id;
    private Long feedId;
    private Long userId;
    private LocalDateTime createdAt;
}
