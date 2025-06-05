package com.vksfeng.quan.controller;

import com.vksfeng.quan.objectivehub_pojo.dto.ObjectiveDTO;
import com.vksfeng.quan.objectivehub_pojo.dto.RelationDTO;
import com.vksfeng.quan.objectivehub_pojo.vo.ObjectiveVO;
import com.vksfeng.quan.result.Result;
import com.vksfeng.quan.service.ObjectiveHubService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Tag(name = "ObjHub模块")
@RestController
@RequestMapping("/objectives")
@Slf4j
public class ObjectiveController {

    @Autowired
    private ObjectiveHubService objectiveHubService;

    /**
     * 创建目标，用于父目标的创建
     * @param objectiveDTO 目标信息
     * @return 目标信息
     */
    @Operation(summary = "创建目标")
    @PostMapping
    public Result<ObjectiveVO> createObjective(@RequestBody ObjectiveDTO objectiveDTO) {
        ObjectiveVO objective = objectiveHubService.createObjective(objectiveDTO);
        return Result.success(objective);
    }

    /**
     * 创建目标，用于子目表的创建
     * @param objectiveDTO
     * @param parentObjectiveId
     * @return
     */
    @Operation(summary = "创建目标(自动关联目标关系版)")
    @PostMapping("/parent/{id}/children")
    public Result<ObjectiveVO> createObjectiveWithRelation(@RequestBody ObjectiveDTO objectiveDTO, @PathVariable("id") Long parentObjectiveId) {
        ObjectiveVO objective = objectiveHubService.createObjectiveWithRelation(objectiveDTO, parentObjectiveId);
        return Result.success(objective);
    }

    /**
     * 获取用户的结构化的全部目标信息
     * @param userId 用户id
     * @return 用户目标
     */
    @Operation(summary = "获取用户目标")
    @GetMapping("/user/{id}")
    public Result<List<ObjectiveVO>> getUserObjectives(@PathVariable("id") Long userId) {
        log.info("获取用户目标");
        List<ObjectiveVO> objectives = objectiveHubService.getUserObjectives(userId);
        return Result.success(objectives);
    }

    /**
     * 获取根据id获取目标信息
     * @param objectiveId 目标id
     * @return 目标信息
     */
    @Operation(summary = "根据id获取目标信息")
    @GetMapping("/{id}")
    public Result<ObjectiveVO> getObjectiveById(@PathVariable("id") Long objectiveId) {
        ObjectiveVO objective = objectiveHubService.getObjectiveById(objectiveId);
        if (objective == null) {
            return Result.error("目标不存在");
        }
        return Result.success(objective);
    }

    @Operation(summary = "获取AI指导")
    @GetMapping("/{id}/ai-guide")
    public Result getAiGuide(@PathVariable("id") Long objectiveId) {
        return objectiveHubService.getAiGuideForObjective(objectiveId);
    }

    /**
     * 更新目标信息
     * @param objectiveDTO 目标信息
     * @return 成功or失败
     */
    @Operation(summary = "更新目标信息")
    @PutMapping("/{id}")
    public Result updateObjective(@RequestBody ObjectiveDTO objectiveDTO, @PathVariable("id") Long objectiveId) {
        objectiveHubService.updateObjective(objectiveDTO, objectiveId);
        return Result.success("目标修改成功");
    }

    /**
     * 删除目标
     * @param objectiveId 目标id
     * @return 成功or失败
     */
    @Operation(summary = "根据id删除目标")
    @DeleteMapping("/{id}")
    public Result deleteObjective(@PathVariable("id") Long objectiveId) {
        objectiveHubService.deleteObjective(objectiveId);
        return Result.success("目标删除成功");
    }

//    @Operation(summary = "添加子目标")
//    @PostMapping("/{id}/children")
//    public Result addSubObjective(@RequestBody RelationDTO childObjectiveId, @PathVariable("id") Long parentObjectiveId) {
//        objectiveHubService.createObjectiveRelation(parentObjectiveId, childObjectiveId.getChildObjectiveId());
//        return Result.success("子目标关联成功");
//    }

    @Operation(summary = "为子目标添加任务")
    @PostMapping("{id}/tasks")
    public Result addTask(@RequestBody RelationDTO taskId, @PathVariable("id") Long objectiveId) {
        objectiveHubService.createObjectiveTaskRelation(objectiveId, taskId.getTaskId());
        return Result.success("任务关联成功");
    }

    @Operation(summary = "删除子目标")
    @DeleteMapping("/{fatherObjectiveId}/children/{childObjectiveId}")
    public Result deleteSubObjective(@PathVariable("fatherObjectiveId") Long fatherObjectiveId, @PathVariable("childObjectiveId") Long childObjectiveId) {
        objectiveHubService.deleteObjectiveRelation(fatherObjectiveId, childObjectiveId);
        return Result.success("子目标移除成功");
    }

    @Operation(summary = "为子目标删除任务")
    @DeleteMapping("{objectiveId}/tasks/{taskId}")
    public Result deleteTask(@PathVariable("objectiveId") Long objectiveId, @PathVariable("taskId") Long taskId) {
        objectiveHubService.deleteObjectiveTaskRelation(objectiveId, taskId);
        return Result.success("任务移除成功");
    }

}
