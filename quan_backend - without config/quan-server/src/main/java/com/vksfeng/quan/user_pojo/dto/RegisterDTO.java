package com.vksfeng.quan.user_pojo.dto;

import lombok.Data;

@Data
public class RegisterDTO {
    private Long id;
    private String username;
    private String password;
    private String email;
    private String nickname;
    private String avatarUrl;
    private String captcha;
}
