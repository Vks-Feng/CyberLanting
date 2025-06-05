package com.vksfeng.quan.controller;


import com.vksfeng.quan.analysis_pojo.vo.HeatMapVO;
import com.vksfeng.quan.analysis_pojo.vo.LeaderboardCountVO;
import com.vksfeng.quan.analysis_pojo.vo.TaskCompletionRateVO;
import com.vksfeng.quan.analysis_pojo.vo.UserProfileForPeerVO;
import com.vksfeng.quan.objectivehub_pojo.vo.TaskVO;
import com.vksfeng.quan.result.Result;
import com.vksfeng.quan.service.AnalysisService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "分析统计模块")
@RestController
@RequestMapping("/analysis")
@Slf4j
public class AnalysisController {

    @Autowired
    private AnalysisService analysisService;

    @Operation(summary = "获取月度任务日历")
    @GetMapping("/calendar/tasks/month")
    public Result<List<TaskVO>> getMonthlyTasksCalendar(@RequestParam Long userId, @RequestParam int year, @RequestParam int month) {
        if (month < 1 || month > 12) {
            return Result.error("月份不合法");
        }
        List<TaskVO> tasks = analysisService.getMonthlyTasksCalendar(userId, year, month);
        return Result.success(tasks);
    }

    @Operation(summary = "获取年度任务热力图")
    @GetMapping("/heatmap/tasks")
    public Result<List<HeatMapVO>> getYearlyTasksHeatMap(@RequestParam Long userId, @RequestParam int year) {
        return analysisService.getYearlyTasksHeatMap(userId, year);
    }

    @Operation(summary = "获取任务排行榜")
    @GetMapping("/leaderboard/task")
    public Result<List<LeaderboardCountVO>> getTaskLeaderboard(@RequestParam String period) {
        if (period.equals("weekly")) {
            return Result.success(analysisService.getWeeklyTaskLeaderboard());
        } else if (period.equals("monthly")) {
            return Result.success(analysisService.getMonthlyTaskLeaderboard());
        } else {
            return Result.error("参数不合法");
        }
    }

    @Operation(summary = "获取好友任务目标状态")
    @GetMapping("/friend/{friendId}/objhubstatus")
    public Result<UserProfileForPeerVO> getFriendObjectiveHubStatus(@PathVariable Long friendId) {
        UserProfileForPeerVO userProfileForPeerVO = analysisService.getFriendObjectiveHubStatus(friendId);
        return Result.success(userProfileForPeerVO);
    }

    @Operation(summary = "获取近一周每日任务完成率")
    @GetMapping("/completion/task/weekly")
    public Result<List<TaskCompletionRateVO>> getWeeklyTaskCompletionRate() {
        return analysisService.getWeeklyTaskCompletionRate();
    }

    @Operation(summary = "用户活跃度信息")
    @GetMapping("/activity")
    public Result getUserActivity() {
        return analysisService.getUserActivity();
    }

    @Operation(summary = "获取用户社交成就")
    @GetMapping("/social/achievement")
    public Result getSocialAchievement() {
        return analysisService.getUserSocialAchievement();
    }
}
