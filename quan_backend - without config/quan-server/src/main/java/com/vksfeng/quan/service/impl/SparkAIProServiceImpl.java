package com.vksfeng.quan.service.impl;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.vksfeng.quan.constant.RedisConstant;
import com.vksfeng.quan.context.BaseContext;
import com.vksfeng.quan.exception.NotLoginException;
import com.vksfeng.quan.obengine_pojo.dto.ObjectiveWithFeedbackDTO;
import com.vksfeng.quan.obengine_pojo.spark.SparkRequest;
import com.vksfeng.quan.obengine_pojo.spark.SparkResponse;
import com.vksfeng.quan.obengine_pojo.vo.FollowUpQuestionVO;
import com.vksfeng.quan.objectivehub_pojo.dto.ObjectiveDTO;
import com.vksfeng.quan.result.Result;
import com.vksfeng.quan.service.ObjectiveHubService;
import com.vksfeng.quan.service.SparkAIService;
import com.vksfeng.quan.websocket.WebSocketServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.vksfeng.quan.constant.RedisConstant.PROMPT_DOMAIN_LIST_KEY;

@Slf4j
public class SparkAIProServiceImpl implements SparkAIService {


    private static final String DEFAULT_DOMAIN = "通用";

    @Value("${spark.api.url}")
    private String SPARK_API_URL;

    @Value("${spark.api.password}")
    private String API_PASSWORD;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectiveHubService objectiveHubService;

    @Autowired
    private WebSocketServer webSocketServer;

    @Autowired
    private RedisTemplate redisTemplate;



    /**
     * 生成跟进问题
     * @param objectiveDTO
     * @return
     */
    @Override
    public Result<List<FollowUpQuestionVO>> generateFollowupQuestions(ObjectiveDTO objectiveDTO) {
        // 构建 prompt
        String prompt = String.format(
                "请根据以下主目标信息生成 3 个跟进问题，每个问题请提供 4 个选项，以便更好地了解用户的实际情况。\n\n" +
                        "主目标名称：%s\n目标描述：%s\n开始日期：%s\n结束日期：%s\n\n" +
                        "输出要求：请按照以下 JSON 格式返回结果：\n" +
                        "{\n" +
                        "    \"questions\": [\n" +
                        "      {\n" +
                        "        \"question\": \"问题1内容\",\n" +
                        "        \"options\": [\"选项1\", \"选项2\", \"选项3\", \"选项4\"]\n" +
                        "      },\n" +
                        "      {\n" +
                        "        \"question\": \"问题2内容\",\n" +
                        "        \"options\": [\"选项1\", \"选项2\", \"选项3\", \"选项4\"]\n" +
                        "      },\n" +
                        "      {\n" +
                        "        \"question\": \"问题3内容\",\n" +
                        "        \"options\": [\"选项1\", \"选项2\", \"选项3\", \"选项4\"]\n" +
                        "      }\n" +
                        "    ]\n" +
                        "}"
                , objectiveDTO.getName(), objectiveDTO.getDescription(), objectiveDTO.getStartDate(), objectiveDTO.getEndDate());

        ResponseEntity<SparkResponse> response = sendSparkRequest(prompt);
        // 处理响应
        String aiResponse = null;
        if (response.getStatusCode().is2xxSuccessful()) {
            SparkResponse sparkResponse = response.getBody();
            if (sparkResponse != null && !sparkResponse.getChoices().isEmpty()) {
                aiResponse = sparkResponse.getChoices().get(0).getMessage().getContent();
            }
        }

        if (aiResponse == null || aiResponse.isEmpty()) {
            return Result.error("AI 生成失败");
        }

        // **去掉 JSON 代码块标记**
        aiResponse = aiResponse.replaceAll("```json", "").replaceAll("```", "").trim();

        // 解析 JSON 响应
        List<FollowUpQuestionVO> followUpQuestionVOList = new ArrayList<>();
        try {
            JSONObject jsonResponse = new JSONObject(aiResponse);
            JSONArray questionsArray = jsonResponse.getJSONArray("questions");

            for (int i = 0; i < questionsArray.size(); i++) {
                JSONObject questionObj = questionsArray.getJSONObject(i);
                FollowUpQuestionVO questionVO = new FollowUpQuestionVO();
                questionVO.setQuestion(questionObj.getStr("question"));
                questionVO.setOptions(questionObj.getJSONArray("options").toList(String.class));
                followUpQuestionVOList.add(questionVO);
            }
        } catch (Exception e) {
            return Result.error("解析 AI 返回结果失败: " + e.getMessage());
        }
        return Result.success(followUpQuestionVOList);
    }

    /**
     * 智能目标拆解，输入主目标信息，生成详细计划
     * @param objectiveWithFeedbackDTO
     */
    @Override
    public void generateDetailedPlan(ObjectiveWithFeedbackDTO objectiveWithFeedbackDTO) {
        // 1. 领域识别
        String domain = detectDomain(objectiveWithFeedbackDTO.getName(), objectiveWithFeedbackDTO.getDescription());
        // 2. 根据领域识别结果，选择合适的prompt模板
        String promptTemplate = getPromptTemplate(domain);
        // 3. 构造实际使用prompt

        // 4. 调用AI接口，获取详细计划
        // 5. 持久化
        // 6. websocket进行通知
    }

    private String getPromptTemplate(String domain) {
        return (String) redisTemplate.opsForValue().get(RedisConstant.PROMPT_TEMPLATE_KEY + ":" + domain);
    }

    /**
     * 获取Redis中的支持领域列表
     * @return
     */
    private List<String> getValidDomainsFromRedis() {
        List<String> domainList = redisTemplate.opsForList().range(RedisConstant.PROMPT_DOMAIN_LIST_KEY, 0, -1);
        return domainList == null ? Collections.emptyList() : domainList;
    }

    /**
     * 调用AI进行领域识别
     * @param objectiveName
     * @param objectiveDescription
     * @return
     */
    private String detectDomain(String objectiveName, String objectiveDescription) {
        try {
            List<String> domains = getValidDomainsFromRedis();
            domains.add("通用"); // 一方面不用存通用，另一方面可以保证list不为空
            String domainsString = String.join("、", domains);
            String prompt = String.format("""
                请根据下列用户目标判断它属于哪个领域，并且严格地只返回领域关键词）：
                可选领域：%s。
                如果不确定请返回“通用”。
                目标名称：%s
                目标描述：%s
                """, domainsString, objectiveName, objectiveDescription);

            // 从 Redis 校验是否是合法领域
            ResponseEntity<SparkResponse> response = sendSparkRequest(prompt);
            // 处理响应
            String result = null;
            if (response.getStatusCode().is2xxSuccessful()) {
                SparkResponse sparkResponse = response.getBody();
                if (sparkResponse != null && !sparkResponse.getChoices().isEmpty()) {
                    result = sparkResponse.getChoices().get(0).getMessage().getContent();
                }
            }
            if (domains != null && domains.contains(result)) {
                return result;
            }
        } catch (Exception e) {
            log.warn("AI 识别领域失败，使用默认通用模板", e);
        }
        return DEFAULT_DOMAIN;
    }

    public Long getUserId() {
        Long userId = BaseContext.getCurrentId();
        if (userId == null) {
            throw new NotLoginException("用户未登录");
        }
        return userId;
    }

    /**
     * 发送请求给 Spark API
     *
     * @param prompt 输入prompt
     * @return
     */
    private ResponseEntity<SparkResponse> sendSparkRequest(String prompt) {
        // 设置请求头
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Authorization", "Bearer " + API_PASSWORD);

        // 构建请求体
        SparkRequest request = new SparkRequest();
        // request.setModel("generalv3.5"); // 根据需要选择模型版本
        request.setModel("lite"); // 根据需要选择模型版本
        SparkRequest.Message message = new SparkRequest.Message();
        message.setRole("user");
        message.setContent(prompt);
        request.setMessages(List.of(message));

        // 发送请求
        HttpEntity<SparkRequest> entity = new HttpEntity<>(request, headers);
        ResponseEntity<SparkResponse> response = restTemplate.exchange(
                SPARK_API_URL,
                HttpMethod.POST,
                entity,
                SparkResponse.class
        );
        return response;
    }




}
