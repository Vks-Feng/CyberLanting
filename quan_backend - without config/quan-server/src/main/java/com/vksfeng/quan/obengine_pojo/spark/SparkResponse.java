package com.vksfeng.quan.obengine_pojo.spark;

import lombok.Data;

import java.util.List;

@Data
public class SparkResponse {
    private List<Choice> choices;

    @Data
    public static class Choice {
        private Message message;

    }

    @Data
    public static class Message {
        private String content;

    }
}
