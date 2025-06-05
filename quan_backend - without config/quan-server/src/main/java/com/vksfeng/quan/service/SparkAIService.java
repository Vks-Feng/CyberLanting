package com.vksfeng.quan.service;

import com.vksfeng.quan.obengine_pojo.dto.ObjectiveWithFeedbackDTO;
import com.vksfeng.quan.obengine_pojo.vo.FollowUpQuestionVO;
import com.vksfeng.quan.objectivehub_pojo.dto.ObjectiveDTO;
import com.vksfeng.quan.result.Result;

import java.util.List;

public interface SparkAIService {
    Result<List<FollowUpQuestionVO>> generateFollowupQuestions(ObjectiveDTO objectiveDTO);

    void generateDetailedPlan(ObjectiveWithFeedbackDTO objectiveWithFeedbackDTO);
}
