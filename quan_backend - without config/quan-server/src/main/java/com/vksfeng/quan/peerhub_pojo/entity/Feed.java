package com.vksfeng.quan.peerhub_pojo.entity;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class Feed {
    private Long id;
    private Long userId;
    private String type; // task_completed, objective_completed, 'post'
    private Long relatedId;
    private String content;
    private Long likeCount;
    private Long commentCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
