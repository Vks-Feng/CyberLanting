package com.vksfeng.quan.resourcehub_pojo.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ResourceCommentDTO {
    private Long id;
    private Long userId;
    private Long resourceId;
    private String content;
}
