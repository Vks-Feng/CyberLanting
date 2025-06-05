package com.vksfeng.quan.achievement.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RuleCondition {
    private String field;
    private String op;
    private Object value;
}
