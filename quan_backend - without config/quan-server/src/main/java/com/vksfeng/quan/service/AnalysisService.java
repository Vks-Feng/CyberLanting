package com.vksfeng.quan.service;

import com.vksfeng.quan.analysis_pojo.vo.HeatMapVO;
import com.vksfeng.quan.analysis_pojo.vo.LeaderboardCountVO;
import com.vksfeng.quan.analysis_pojo.vo.TaskCompletionRateVO;
import com.vksfeng.quan.analysis_pojo.vo.UserProfileForPeerVO;
import com.vksfeng.quan.objectivehub_pojo.vo.TaskVO;
import com.vksfeng.quan.result.Result;

import java.util.List;

public interface AnalysisService {
    List<TaskVO> getMonthlyTasksCalendar(Long userId, int year, int month);

    List<LeaderboardCountVO> getWeeklyTaskLeaderboard();

    List<LeaderboardCountVO> getMonthlyTaskLeaderboard();

    UserProfileForPeerVO getFriendObjectiveHubStatus(Long friendId);

    Result<List<TaskCompletionRateVO>> getWeeklyTaskCompletionRate();

    Result getUserActivity();

    Result getUserSocialAchievement();

    Result<List<HeatMapVO>> getYearlyTasksHeatMap(Long userId, int year);
}
