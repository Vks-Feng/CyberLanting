package com.vksfeng.quan;

import com.vksfeng.quan.achievement.pojo.entity.RuleCondition;
import com.vksfeng.quan.achievement.pojo.entity.RuleJson;
import com.vksfeng.quan.achievement.util.AchievementEventPublisher;
import com.vksfeng.quan.achievement.util.AchievementListener;
import com.vksfeng.quan.achievement.util.RuleEvaluator;
import com.vksfeng.quan.scheduler.TaskStatusScheduler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class QuanApplicationTests {

	@Test
	void contextLoads() {
	}

	@Autowired
	private RuleEvaluator ruleEvaluator;

	@Test
	void textRuleEvaluator() {
		RuleJson rule = new RuleJson();
		rule.setType("IS_ADULT_MALE");
		rule.setLogic("AND");
		rule.setConditions(List.of(
				new RuleCondition("age", ">", 18),
				new RuleCondition("gender", "==", "Male")
		));
		System.out.println(ruleEvaluator.evaluate(rule, Map.of("age", 20, "gender", "Male")));
	}

	@Autowired
	private AchievementEventPublisher achievementEventPublisher;

	@Test
	void testEventPublisher() {
//		achievementEventPublisher.publish(1L, "OBJECTIVE_ADDED", null);
		achievementEventPublisher.publish(1L, "TASK_COMPLETED", null);
	}


	@Autowired
	private TaskStatusScheduler taskStatusScheduler;

	@Test
	void testTaskStatusScheduler() {
		taskStatusScheduler.doRefresh();
	}


}
