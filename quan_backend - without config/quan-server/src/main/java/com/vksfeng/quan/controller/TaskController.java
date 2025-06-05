package com.vksfeng.quan.controller;

import com.vksfeng.quan.objectivehub_pojo.dto.RecurringTaskDTO;
import com.vksfeng.quan.objectivehub_pojo.dto.SingleTaskDTO;
import com.vksfeng.quan.objectivehub_pojo.vo.TaskVO;
import com.vksfeng.quan.result.Result;
import com.vksfeng.quan.service.ObjectiveHubService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "ObjHub模块")
@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private ObjectiveHubService objectiveHubService;

    // ----------------------------------------创建---------------------------------------------

    @Operation(summary = "创建单次任务")
    @PostMapping("/single")
    public Result<TaskVO> createSingleTask(@RequestBody SingleTaskDTO taskDTO) {
        if (taskDTO.getObjectiveId() == null) {
            return Result.error("任务不能独立存在");
        }
        TaskVO taskVO = objectiveHubService.createSingleTask(taskDTO);
        return Result.success(taskVO);
    }

    @Operation(summary = "创建重复任务")
    @PostMapping("/recurring")
    public Result<TaskVO> createRecurringTask(@RequestBody RecurringTaskDTO taskDTO) {
        if (taskDTO.getObjectiveId() == null) {
            return Result.error("任务不能独立存在");
        }
        TaskVO taskVO = objectiveHubService.createRecurringTask(taskDTO);
        return Result.success(taskVO);
    }

    // ----------------------------------------查询---------------------------------------------

    @Operation(summary = "获取用户今日任务")
    @GetMapping("/user/{id}/today")
    public Result<List<TaskVO>> getUserTodayTasks(@PathVariable("id") Long userId) {
        return Result.success(objectiveHubService.getUserTodayTasks(userId));
    }

    @Operation(summary = "根据id获取任务信息")
    @GetMapping("/{id}")
    public Result<TaskVO> getTaskById(@PathVariable Long id) {
        return Result.success(objectiveHubService.getTaskById(id));
    }

    // ----------------------------------------更新---------------------------------------------

    @Operation(summary = "更新单次任务信息")
    @PutMapping("/single/{id}")
    public Result updateSingleTask(@RequestBody SingleTaskDTO taskDTO, @PathVariable Long id) {
        objectiveHubService.updateSingleTask(taskDTO, id);
        return Result.success("任务修改成功");
    }

    @Operation(summary = "更新重复任务信息")
    @PutMapping("/recurring/{id}")
    public Result<TaskVO> updateRecurringTask(@RequestBody RecurringTaskDTO taskDTO, @PathVariable Long id) {
        objectiveHubService.updateRecurringTask(taskDTO, id);
        return Result.success("任务修改成功");
    }


    @Operation(summary = "完成任务")
    @PostMapping("/completion")
    public Result completeTask(@RequestBody Map<String, Long> request) {
        Long id = request.get("id");
        return objectiveHubService.completeTask(id);
    }

    @Operation(summary = "撤销任务完成")
    @DeleteMapping("/completion/{id}")
    public Result deleteTaskCompletion(@PathVariable Long id) {
        objectiveHubService.deleteTaskCompletion(id);
        return Result.success("任务完成记录删除成功");
    }

    // ----------------------------------------删除---------------------------------------------
    @Operation(summary = "根据id删除任务")
    @DeleteMapping("/{id}")
    public Result deleteTask(@PathVariable Long id) {
        objectiveHubService.deleteTask(id);
        return Result.success("任务删除成功");
    }

}
