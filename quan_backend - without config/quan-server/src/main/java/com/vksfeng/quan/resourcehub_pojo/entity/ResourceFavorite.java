package com.vksfeng.quan.resourcehub_pojo.entity;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ResourceFavorite {
    private Long id;
    private Long userId;
    private Long resourceId;
    private LocalDateTime createdAt;
}
