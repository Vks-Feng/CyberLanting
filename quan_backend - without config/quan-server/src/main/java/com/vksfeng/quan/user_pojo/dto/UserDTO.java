package com.vksfeng.quan.user_pojo.dto;

import lombok.Data;

// User.java
@Data
public class UserDTO {
    private Long id;
    private String username;
    private String password;
    private String email;
    private String nickname;
    private String avatarUrl;
}
