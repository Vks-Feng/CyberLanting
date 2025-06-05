package com.vksfeng.quan.service;

import com.vksfeng.quan.obengine_pojo.dto.SubObjectiveDTO;
import com.vksfeng.quan.objectivehub_pojo.dto.ObjectiveDTO;
import com.vksfeng.quan.objectivehub_pojo.dto.RecurringTaskDTO;
import com.vksfeng.quan.objectivehub_pojo.dto.SingleTaskDTO;
import com.vksfeng.quan.objectivehub_pojo.vo.ObjectiveVO;
import com.vksfeng.quan.objectivehub_pojo.vo.TaskVO;
import com.vksfeng.quan.result.Result;

import java.util.List;

public interface ObjectiveHubService {

    // ------------------------------------------- 目标相关 -------------------------------------------

    // 创建目标
    ObjectiveVO createObjective(ObjectiveDTO objectiveDTO);

    // 创建子目标（创建目标同时关联其与父目标的关系）
    ObjectiveVO createObjectiveWithRelation(ObjectiveDTO objectiveDTO, Long parentObjectiveId);

    // 获取用户所有目标的结构化信息
    List<ObjectiveVO> getUserObjectives(Long userId);

    // 根据id获取目标信息
    ObjectiveVO getObjectiveById(Long objectiveId);

    // 更新目标信息
    void updateObjective(ObjectiveDTO objectiveDTO, Long objectiveId);

    // 删除目标，及目标所含有的信息
    void deleteObjective(Long objectiveId);

    void addAiGuideForObjective(Long objectiveId, String aiGuide);

    // 创建目标与目标的关系
    void createObjectiveRelation(Long parentObjectiveId, Long childObjectiveId);

    void createObjectiveTaskRelation(Long objectiveId, Long taskId);

    void deleteObjectiveRelation(Long parentObjectiveId, Long childObjectiveId);

    void deleteObjectiveTaskRelation(Long objectiveId, Long taskId);


    // ------------------------------------------- 任务相关 -------------------------------------------

    // 获取用户今日任务
    List<TaskVO> getUserTodayTasks(Long userId);

    // 创建单次任务（DTO中包含与目标的关联信息）
    TaskVO createSingleTask(SingleTaskDTO taskDTO);

    // 创建重复任务（DTO中包含与目标的关联信息）
    TaskVO createRecurringTask(RecurringTaskDTO taskDTO);

    // 根据id获取任务信息
    TaskVO getTaskById(Long id);

    // 更新单次任务信息
    void updateSingleTask(SingleTaskDTO taskDTO, Long id);

    // 更新重复任务信息
    void updateRecurringTask(RecurringTaskDTO taskDTO, Long id);

    // 删除任务，及其与目标之间的关联
    void deleteTask(Long id);

    // 完成任务
    Result completeTask(Long id);

    // 撤销任务完成
    void deleteTaskCompletion(Long id);

    // ------------------------------------------- 统计相关 -------------------------------------------

    Integer getObjectiveCount(Long userId);

    Integer getCompletedObjectiveCount(Long userId);

    Integer getTaskCount(Long userId);

    Integer getCompletedTaskCount(Long userId);

    Result getAiGuideForObjective(Long objectiveId);
}
