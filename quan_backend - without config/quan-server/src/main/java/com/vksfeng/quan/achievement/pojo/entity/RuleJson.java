package com.vksfeng.quan.achievement.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RuleJson {
    private String type;
    private List<RuleCondition> conditions;
    private String logic = "AND"; // 默认为AND
}
