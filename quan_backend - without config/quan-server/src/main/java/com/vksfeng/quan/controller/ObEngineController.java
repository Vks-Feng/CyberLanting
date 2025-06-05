package com.vksfeng.quan.controller;


import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;

import com.vksfeng.quan.obengine_pojo.dto.ObjectiveWithFeedbackDTO;
import com.vksfeng.quan.obengine_pojo.vo.FollowUpQuestionVO;
import com.vksfeng.quan.objectivehub_pojo.dto.ObjectiveDTO;
import com.vksfeng.quan.result.Result;
import com.vksfeng.quan.service.SparkAIService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "OB引擎模块")
@Slf4j
@RestController
@RequestMapping("/obengine")
public class ObEngineController {

    @Autowired
    private SparkAIService sparkAIService;

    @Operation(summary = "生成跟进问题")
    @PostMapping("/generateFollowup")
    public Result<List<FollowUpQuestionVO>> generateFollowup(@RequestBody ObjectiveDTO objectiveDTO) {
        return sparkAIService.generateFollowupQuestions(objectiveDTO);
    }

    @Operation(summary = "生成详细计划")
    @PostMapping("/generateDetailedPlan")
    public Result generateDetailedPlan(@RequestBody ObjectiveWithFeedbackDTO objectiveWithFeedbackDTO) {
        sparkAIService.generateDetailedPlan(objectiveWithFeedbackDTO);
        return Result.success();
    }



}


