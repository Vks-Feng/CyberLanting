package com.vksfeng.quan.resourcehub_pojo.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Resource {
    private Long id;
    private Long userId;
    private String title;
    private String description;
    private String category; // "article","book","course","tool","insight"
    private String url;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
