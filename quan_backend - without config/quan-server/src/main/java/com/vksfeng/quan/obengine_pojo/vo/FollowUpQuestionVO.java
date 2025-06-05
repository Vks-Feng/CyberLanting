package com.vksfeng.quan.obengine_pojo.vo;

import lombok.Data;

import java.util.List;

@Data
public class FollowUpQuestionVO {
    private String question;
    private List<String> options;
}
