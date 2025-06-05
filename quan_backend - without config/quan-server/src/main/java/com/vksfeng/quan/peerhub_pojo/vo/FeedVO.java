package com.vksfeng.quan.peerhub_pojo.vo;

import com.vksfeng.quan.user_pojo.vo.UserVO;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FeedVO {
    private Long id;
    private Long userId;
    private UserVO user;
    private String type; // task_completed, objective_completed, 'post'
    private Long relatedId;
    private String content;
    private Boolean isLiked;
    private Long likeCount;
    private Long commentCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
