package com.vksfeng.quan.resourcehub_pojo.vo;

import com.vksfeng.quan.user_pojo.vo.UserVO;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ResourceVO {
    private Long id;
    private Long userId;
    private String title;
    private String description;
    private String category; // "article","book","course","insight"
    private String url;
    private String content;
    private LocalDateTime createdAt;
    private UserVO userVO;
    private Boolean isLiked;
    private Boolean isFavorite;
    private Long likeCount;
    private Long commentCount;
    private Long favoriteCount;
}
