package com.vksfeng.quan.achievement.util;

import com.vksfeng.quan.achievement.pojo.entity.RuleCondition;
import com.vksfeng.quan.achievement.pojo.entity.RuleJson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
@Slf4j
public class RuleEvaluator {

    public boolean evaluate(RuleJson rule, Map<String, Object> context) {

        List<Boolean> results = new ArrayList<>();

        for (RuleCondition cond : rule.getConditions()) {
            Object actual = context.get(cond.getField());
            Object expected = cond.getValue();
            boolean matched = compare(actual, cond.getOp(), expected);
            results.add(matched);
        }

        return "AND".equals(rule.getLogic())
                ? results.stream().allMatch(Boolean::booleanValue)
                : results.stream().anyMatch(Boolean::booleanValue);
    }

    private boolean compare(Object actual, String op, Object expected) {
        if (actual instanceof Number && expected instanceof Number) {
            double a = ((Number) actual).doubleValue();
            double b = ((Number) expected).doubleValue();
            return switch (op) {
                case "==" -> a == b;
                case "!=" -> a != b;
                case ">"  -> a > b;
                case ">=" -> a >= b;
                case "<"  -> a < b;
                case "<=" -> a <= b;
                default -> false;
            };
        } else {
            return switch (op) {
                case "==" -> Objects.equals(actual, expected);
                case "!=" -> !Objects.equals(actual, expected);
                default -> false;
            };
        }
    }
}
