package com.vksfeng.quan.service;

import jakarta.mail.MessagingException;

public interface EmailService {
    void sendCaptchaEmail(String email, String authCode) throws MessagingException;
}
