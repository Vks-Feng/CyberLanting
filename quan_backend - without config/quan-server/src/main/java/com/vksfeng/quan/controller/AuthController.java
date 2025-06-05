package com.vksfeng.quan.controller;

import com.vksfeng.quan.service.CaptchaService;
import com.vksfeng.quan.service.EmailService;
import com.vksfeng.quan.user_pojo.dto.RegisterDTO;
import com.vksfeng.quan.user_pojo.dto.UserDTO;
import com.vksfeng.quan.user_pojo.entity.User;
import com.vksfeng.quan.user_pojo.vo.LoginResponse;
import com.vksfeng.quan.user_pojo.vo.UserVO;
import com.vksfeng.quan.result.Result;
import com.vksfeng.quan.service.UserService;
import com.vksfeng.quan.util.JwtUtils;
import com.vksfeng.quan.util.RegexUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@CrossOrigin(origins = "http://localhost:5173")
@Tag(name = "登陆注册模块")
@RestController
@Slf4j
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private CaptchaService captchaService;

    @Autowired
    private EmailService emailService;

    @Value("${jwt.secret-key}")
    private String SECRET_KEY;

    @Operation(summary = "用户注册")
    @PostMapping("/register")
    public Result register(@RequestBody RegisterDTO registerDTO) {
        log.info("用户注册");
        if (!captchaService.validateCaptcha(registerDTO.getEmail(), registerDTO.getCaptcha())) {
            return Result.error("验证码错误");
        }
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(registerDTO, userDTO);
        userService.register(userDTO);
        return Result.success();
    }

    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public Result login(@RequestBody UserDTO userDTO) {
        log.info("用户登录");
        User user = userService.login(userDTO);
        if (user == null) {
            return Result.error("用户不存在");
        }
        // 登录成功，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        String token = JwtUtils.createJWT(SECRET_KEY, 3600000, claims);
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        LoginResponse loginResponse = new LoginResponse(userVO, token);
        return Result.success(loginResponse);
    }

    @Operation(summary = "发送验证码")
    @PostMapping("/send-code")
    public Result sendCode(@RequestParam String email) throws MessagingException {

        if (!RegexUtils.isEmailValid(email)) {
            return Result.error("邮箱格式非法");
        }
        if (userService.getUserByEmail(email) != null) {
            return Result.error("邮箱已被注册");
        }

        String code = captchaService.generateCaptcha(email);
        emailService.sendCaptchaEmail(email, code);
        return Result.success();
    }


    @PostMapping("/send-test")
    public Result sendCodeTest(@RequestParam String email) throws MessagingException {
        if (!RegexUtils.isEmailValid(email)) {
            return Result.error("邮箱格式非法");
        }
        String code = "666666";
        emailService.sendCaptchaEmail(email, code);
        return Result.success();
    }
}
