package com.vksfeng.quan.obengine_pojo.spark;

import lombok.Data;

import java.util.List;

@Data
public class SparkRequest {
    private String model;
    private List<Message> messages;

    @Data
    public static class Message {
        private String role;
        private String content;

    }
}
