package com.vksfeng.quan.service;

public interface CaptchaService {
    String generateCaptcha(String email);

    boolean validateCaptcha(String email, String code);
}
