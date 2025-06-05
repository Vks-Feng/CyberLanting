package com.vksfeng.quan.user_pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
    UserVO userVO;
    String token;
}
