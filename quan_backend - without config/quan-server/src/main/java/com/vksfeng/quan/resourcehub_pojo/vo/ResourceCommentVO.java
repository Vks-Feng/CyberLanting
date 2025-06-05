package com.vksfeng.quan.resourcehub_pojo.vo;

import com.vksfeng.quan.user_pojo.vo.UserVO;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ResourceCommentVO {
    private Long id;
    private Long resourceId;
    private String content;
    private LocalDateTime createdAt;
    private UserVO userVO;
}
