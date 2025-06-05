package com.vksfeng.quan.service.impl;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.vksfeng.quan.context.BaseContext;
import com.vksfeng.quan.exception.NotLoginException;
import com.vksfeng.quan.exception.UnknownTaskTypeException;
import com.vksfeng.quan.obengine_pojo.dto.Feedback;
import com.vksfeng.quan.obengine_pojo.dto.ObjectiveWithFeedbackDTO;
import com.vksfeng.quan.obengine_pojo.dto.SubObjectiveDTO;
import com.vksfeng.quan.obengine_pojo.vo.FollowUpQuestionVO;
import com.vksfeng.quan.objectivehub_pojo.dto.ObjectiveDTO;
import com.vksfeng.quan.objectivehub_pojo.dto.RecurringTaskDTO;
import com.vksfeng.quan.objectivehub_pojo.dto.SingleTaskDTO;
import com.vksfeng.quan.objectivehub_pojo.entity.Task;
import com.vksfeng.quan.objectivehub_pojo.vo.ObjectiveVO;
import com.vksfeng.quan.result.Result;
import com.vksfeng.quan.service.ObjectiveHubService;
import com.vksfeng.quan.service.SparkAIService;
import com.vksfeng.quan.obengine_pojo.spark.SparkRequest;
import com.vksfeng.quan.obengine_pojo.spark.SparkResponse;
import com.vksfeng.quan.websocket.WebSocketServer;
import io.swagger.v3.core.util.Json;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@Slf4j
@EnableAsync // 开启 Spring 异步功能
public class SparkAIServiceImpl implements SparkAIService {

    @Value("${spark.api.url}")
    private String SPARK_API_URL;

    @Value("${spark.api.password}")
    private String API_PASSWORD;

//    @Value("${spark.api.max-url}")
//    private String MAX_SPARK_API_URL;

//    @Value("${spark.api.max-password}")
//    private String MAX_API_PASSWORD;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectiveHubService objectiveHubService;

    @Autowired
    private WebSocketServer webSocketServer;
//
//    private static final ExecutorService OBJECTIVES_EXECUTOR = Executors.newSingleThreadExecutor();
//
//    @PostConstruct
//    private void init() {
//        OBJECTIVES_EXECUTOR.submit(new ObjectiveHandler());
//    }
//
//    private class ObjectiveHandler implements Runnable {
//        @Override
//        public void run() {
//            String queueName = "stream.objectives";
//            while (true) {
//
//            }
//        }
//    }

    private ResponseEntity<SparkResponse> sendSparkRequest(String prompt) {
        // 设置请求头
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Authorization", "Bearer " + API_PASSWORD);

        // 构建请求体
        SparkRequest request = new SparkRequest();
//        request.setModel("generalv3.5"); // 根据需要选择模型版本
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

    public Long getUserId() {
        Long userId = BaseContext.getCurrentId();
        if (userId == null) {
            throw new NotLoginException("用户未登录");
        }
        return userId;
    }

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

    public String generateObjectivePrompt(ObjectiveWithFeedbackDTO objectiveWithFeedbackDTO) {
        // 构建用户输入部分
        StringBuilder feedbackStr = new StringBuilder();
        if (objectiveWithFeedbackDTO.getFeedback() != null && !objectiveWithFeedbackDTO.getFeedback().isEmpty()) {
            feedbackStr.append("\n用户反馈：\n");
            for (Feedback feedback : objectiveWithFeedbackDTO.getFeedback()) {
                feedbackStr.append("- ").append(feedback.getQuestion()).append(": ").append(feedback.getAnswer()).append("\n");
            }
        }

        // 构建最终的 prompt
        return String.format("""
        你是一个智能任务规划助手，专门帮助用户拆解目标并制定详细的执行计划。

        任务要求
        用户在**用户输入**部分提供一个 **主目标**，包含目标名称、描述、开始时间和结束时间。并请你根据该目标自动拆解成多个**子目标**，为每个子目标生成合适的**任务清单**(至少一到两个任务)，包括 **单次任务** 和 **重复任务**。

        ### **拆解逻辑**
        1. **时间分析**
          - 计算目标的总持续时间（结束时间 - 开始时间）。
          - 识别关键时间节点（如周末、月份切换）。
          - 根据时间跨度选择拆解策略：
            - ≤7天 → 按天拆解
            - 8 - 30天 → 按周拆解
            - >30天 → 按月拆解，并设定里程碑子目标

        2. **子目标拆解**
          - **流程拆解法**：如果目标包含明确的执行步骤（如"开发应用" → "设计UI" → "编写代码" → "测试"）。
          - **模块拆解法**：如果目标涉及多个领域（如"学习编程" → "学习Java" + "学习数据库" + "学习Spring"）。
          - **数值递减拆解法**：如果目标有量化指标（如"读书10本" → "每周2本"）。
          - **时间轴基准拆解法（默认）**：均匀分配任务。

        3. **任务生成**
          - **单次任务（SingleTask）**：
            - 有明确截止日期（DDL）
            - 需要特定资源调配
          - **重复任务（RecurringTask）**：
            - 适用于习惯养成和持续任务
            - 频率："daily"（每天）、"weekly"（每周）、"monthly"（每月）
            - 任务周期覆盖至少 30%% 的总时间

        4. **时间范围约束**
          - 所有子目标的时间范围（subObjectives.startDate 和 subObjectives.endDate）必须在主目标时间范围内：
            - subObjectives.startDate ≥ masterObjective.startDate
            - subObjectives.endDate ≤ masterObjective.endDate
          - **单次任务**的截止日期必须在其所属子目标的时间范围内。
          - **重复任务**的开始和结束日期必须在其所属子目标的时间范围内。
          - 在生成任务时，如计算得到的任务时间超出子目标范围，应自动调整为子目标时间范围的边界。

        ### **用户输入**
        - **主目标名称**: %s
        - **目标描述**: %s
        - **开始日期**: %s
        - **结束日期**: %s
        %s

        ### **输出格式**
        请严格按照以下 JSON 格式返回数据：
        ```json
        {
            "masterObjective": {
                "name": "%s",
                "description": "%s",
                "startDate": "%s",
                "endDate": "%s"
            },
            "subObjectives": [
                {
                    "name": "{子目标名称}",
                    "description": "{子目标描述}",
                    "startDate": "{YYYY-MM-DD}",
                    "endDate": "{YYYY-MM-DD}",
                    "tasks": [
                        {
                            "name": "{单次任务名称}",
                            "type": "single",
                            "description": "{任务描述}",
                            "ddl": "{YYYY-MM-DD}"
                        },
                        {
                            "name": "{重复任务名称}",
                            "type": "recurring",
                            "description": "{任务描述}",
                            "period": "daily",
                            "startDate": "{YYYY-MM-DD}",
                            "endDate": "{YYYY-MM-DD}"
                        }
                    ]
                }
            ],
            "aiGuide": "{智能建议：针对用户目标的优化或执行策略，100字左右}"
        }
        ```
        """,
                objectiveWithFeedbackDTO.getName(),
                objectiveWithFeedbackDTO.getDescription(),
                objectiveWithFeedbackDTO.getStartDate(),
                objectiveWithFeedbackDTO.getEndDate(),
                feedbackStr.toString(),

                objectiveWithFeedbackDTO.getName(),
                objectiveWithFeedbackDTO.getDescription(),
                objectiveWithFeedbackDTO.getStartDate(),
                objectiveWithFeedbackDTO.getEndDate()
        );
    }

    public String getAIDetailedPlan(String prompt) {
        ResponseEntity<SparkResponse> response = sendSparkRequest(prompt);

        // 处理响应
        if (response.getStatusCode().is2xxSuccessful()) {
            SparkResponse sparkResponse = response.getBody();
            if (sparkResponse != null && !sparkResponse.getChoices().isEmpty()) {
                String aiResponse = sparkResponse.getChoices().get(0).getMessage().getContent();
                aiResponse = aiResponse.replaceAll("```json", "").replaceAll("```", "").trim();
                return aiResponse;
            }
        }
        return null;
    }

    public void createObjectives(String detail, Long userId) {
        log.info("AI 生成的详细计划：{}", detail);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        ObjectiveDTO masterObjective;
        List<SubObjectiveDTO> subObjectives;
        String aiGuide;
        try {
            JsonNode rootNode = objectMapper.readTree(detail);
            log.info(rootNode.toPrettyString());
            JsonNode masterObjectiveNode = rootNode.get("masterObjective");
            log.info("解析的主目标：{}", masterObjectiveNode);
            masterObjective = objectMapper.treeToValue(masterObjectiveNode, ObjectiveDTO.class);
            subObjectives = new ArrayList<>();
            JsonNode subObjectivesNode = rootNode.get("subObjectives");
            if (subObjectivesNode.isArray()) {
                for (JsonNode subObjectiveNode : subObjectivesNode) {
                    SubObjectiveDTO subObjective = objectMapper.treeToValue(subObjectiveNode, SubObjectiveDTO.class);
                    log.info("解析的子目标：{}", subObjective);
                    subObjectives.add(subObjective);
                }
            }
            JsonNode aiGuideNode = rootNode.get("aiGuide");
            log.info("解析的主目标：{}", masterObjectiveNode);
            aiGuide = aiGuideNode.asText();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        saveObjectives(masterObjective, subObjectives, aiGuide, userId);
    }

    @Transactional
    public void saveObjectives(ObjectiveDTO masterObjective, List<SubObjectiveDTO> subObjectives, String aiGuide, Long userId) {
        masterObjective.setUserId(userId);
        ObjectiveVO master;
        // 创建主目标
        try {
            master = objectiveHubService.createObjective(masterObjective);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        Long masterId = master.getId();
        // 创建子目标
        for (SubObjectiveDTO subObjective : subObjectives) {
            ObjectiveDTO subObjectiveDTO = new ObjectiveDTO();
            BeanUtils.copyProperties(subObjective, subObjectiveDTO);
            subObjectiveDTO.setUserId(userId);
            ObjectiveVO subObjectiveVO;
            try {
                subObjectiveVO = objectiveHubService.createObjectiveWithRelation(subObjectiveDTO, masterId);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
            if (subObjective.getTasks() == null) {
                continue;
            }
            for (Task task : subObjective.getTasks()) {
                if (task.getType().equals("single")) {
                    SingleTaskDTO taskDTO = new SingleTaskDTO();
                    BeanUtils.copyProperties(task, taskDTO);
                    taskDTO.setUserId(userId);
                    taskDTO.setObjectiveId(subObjectiveVO.getId());
                    try {
                        objectiveHubService.createSingleTask(taskDTO);
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                } else if (task.getType().equals("recurring")) {
                    RecurringTaskDTO taskDTO = new RecurringTaskDTO();
                    BeanUtils.copyProperties(task, taskDTO);
                    taskDTO.setObjectiveId(subObjectiveVO.getId());
                    taskDTO.setUserId(userId);
                    try {
                        objectiveHubService.createRecurringTask(taskDTO);
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                } else {
                    throw new UnknownTaskTypeException("未知的任务类型");
                }
            }
        }
        // 添加AI指导
        try {
            objectiveHubService.addAiGuideForObjective(masterId, aiGuide);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        webSocketServer.sendToClient(userId.toString(), "AI智能目标创建成功");
    }

    @Async
    public void processObjectives(ObjectiveWithFeedbackDTO objectiveWithFeedbackDTO, Long userId) {
        String prompt = generateObjectivePrompt(objectiveWithFeedbackDTO);
        String details = getAIDetailedPlan(prompt);
        createObjectives(details, userId);
    }

    public void generateDetailedPlan(ObjectiveWithFeedbackDTO objectiveWithFeedbackDTO) {
        Long userId = getUserId();
        // 立即返回一个任务提交成功的响应（此处假设用 HTTP Controller 调用）
        try {
            CompletableFuture.runAsync(() -> processObjectives(objectiveWithFeedbackDTO, userId));
        } catch (Exception e) {
            log.info("智能目标解析任务失败，用户id：{}", userId);
            webSocketServer.sendToClient(userId.toString(), "智能目标解析任务失败");
            webSocketServer.onClose(userId.toString());
        }
//        processObjectives(objectiveWithFeedbackDTO, userId);
    }
}