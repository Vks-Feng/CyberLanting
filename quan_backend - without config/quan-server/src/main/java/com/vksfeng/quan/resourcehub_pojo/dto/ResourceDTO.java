package com.vksfeng.quan.resourcehub_pojo.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ResourceDTO {
    private Long id;
    private String title;
    private String description;
    private String category; // "article","book","course","insight"
    private String url;
    private String content;
}
