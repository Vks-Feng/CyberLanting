package com.vksfeng.quan.peerhub_pojo.vo;

import com.vksfeng.quan.user_pojo.vo.UserVO;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FeedCommentVO {
    private Long id;
    private Long feedId;
    private Long userId;
    private UserVO user;
    private String content;
    private LocalDateTime createdAt;
}
