package com.vksfeng.quan.peerhub_pojo.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FeedComment {
    private Long id;
    private Long feedId;
    private Long userId;
    private String content;
    private LocalDateTime createdAt;
}
