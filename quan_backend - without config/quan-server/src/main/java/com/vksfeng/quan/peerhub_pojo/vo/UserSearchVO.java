package com.vksfeng.quan.peerhub_pojo.vo;

import lombok.Data;

@Data
public class UserSearchVO {
    private Long id;
    private String username;
    private String email;
    private String nickname;
    private String avatarUrl;
    private Boolean isFriend;
}
