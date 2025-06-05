package com.vksfeng.quan.service.impl;

import com.vksfeng.quan.analysis_pojo.entity.TaskCompletionDay;
import com.vksfeng.quan.analysis_pojo.vo.HeatMapVO;
import com.vksfeng.quan.analysis_pojo.vo.LeaderboardCountVO;
import com.vksfeng.quan.analysis_pojo.vo.TaskCompletionRateVO;
import com.vksfeng.quan.analysis_pojo.vo.UserProfileForPeerVO;
import com.vksfeng.quan.context.BaseContext;
import com.vksfeng.quan.exception.FriendshipAlreadyExistException;
import com.vksfeng.quan.exception.FriendshipNotExistException;
import com.vksfeng.quan.exception.NotLoginException;
import com.vksfeng.quan.mapper.FriendshipMapper;
import com.vksfeng.quan.mapper.TaskMapper;
import com.vksfeng.quan.objectivehub_pojo.vo.TaskVO;
import com.vksfeng.quan.result.Result;
import com.vksfeng.quan.service.*;
import com.vksfeng.quan.user_pojo.vo.UserActivityVO;
import com.vksfeng.quan.user_pojo.vo.UserSocialAchievementVO;
import com.vksfeng.quan.user_pojo.vo.UserVO;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Slf4j
public class AnalysisServiceImpl implements AnalysisService {

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private ObjectiveHubService objectiveHubService;

    @Autowired
    private ResourceHubService resourceHubService;

    @Autowired
    private FriendshipService friendshipService;

    public Long getUserId() {
        Long userId = BaseContext.getCurrentId();
        if (userId == null) {
            throw new NotLoginException("用户未登录");
        }
        return userId;
    }

    public List<TaskVO> getMonthlyTasksCalendar(Long userId, int year, int month) {
        List<TaskVO> tasks = taskMapper.getMonthlyTasksCalendar(userId, year, month);
        return tasks;
    }

//    public List<HeatMapVO> getMonthlyTasksHeatMap(Long userId, int year, int month) {
//        // 1. 获取数据库记录
//        List<TaskCompletionDay> dbData = taskMapper.selectDailyCompletion(userId, year, month);
//
//        // 2. 创建当月日期模板
//        YearMonth yearMonth = YearMonth.of(year,  month);
//        Map<Integer, Integer> countMap = dbData.stream()
//                .collect(Collectors.toMap(TaskCompletionDay::getDay,  TaskCompletionDay::getCount));
//
//        // 3. 生成完整日期数据
//        return IntStream.rangeClosed(1,  yearMonth.lengthOfMonth())
//                .mapToObj(day -> {
//                    LocalDate date = yearMonth.atDay(day);
//                    return new HeatMapVO(
//                            date.format(DateTimeFormatter.ISO_DATE),
//                            countMap.getOrDefault(day,  0)
//                    );
//                })
//                .collect(Collectors.toList());
//    }

    @Override
    public List<LeaderboardCountVO> getWeeklyTaskLeaderboard() {
        Long userId = getUserId();
        List<UserVO> friendId = friendshipService.getFriends(userId);
        List<Long> userIds = new ArrayList<>(friendId.stream().map(UserVO::getId).toList());
        userIds.add(userId);
        return taskMapper.getWeeklyTaskLeaderboard(userIds);
    }

    @Override
    public List<LeaderboardCountVO> getMonthlyTaskLeaderboard() {
        Long userId = getUserId();
        List<UserVO> friendId = friendshipService.getFriends(userId);
        List<Long> userIds = new ArrayList<>(friendId.stream().map(UserVO::getId).toList());
        userIds.add(userId);
        return taskMapper.getMonthlyTaskLeaderboard(userIds);
    }

    @Override
    public UserProfileForPeerVO getFriendObjectiveHubStatus(Long friendId) {
        Long userId = getUserId();
        if (!friendshipService.isExist(userId, friendId) && friendId != userId) {
            throw new FriendshipNotExistException("对方还不是你的好友哦");
        }
        Integer objectiveCount = objectiveHubService.getObjectiveCount(friendId);
        Integer completedObjectiveCount = objectiveHubService.getCompletedObjectiveCount(friendId);
        Integer taskCount = objectiveHubService.getTaskCount(friendId);
        Integer completedTaskCount = objectiveHubService.getCompletedTaskCount(friendId);
        return new UserProfileForPeerVO(
                objectiveCount,
                completedObjectiveCount,
                taskCount,
                completedTaskCount
        );
    }

    @Override
    public Result<List<TaskCompletionRateVO>> getWeeklyTaskCompletionRate() {
        Long userId = getUserId();
        List<LocalDate> last7Days = getLast7Days();
        LocalDate startDate = last7Days.get(last7Days.size() - 1);
        LocalDate endDate = last7Days.get(0);

        // 获取 totalCount
        Map<LocalDate, Integer> totalCountMap = taskMapper.getTotalCount(userId, startDate, endDate)
                .stream().collect(Collectors.toMap(TaskCompletionRateVO::getDate, TaskCompletionRateVO::getTotalCount));

        // 获取 completeCount
        Map<LocalDate, Integer> completeCountMap = taskMapper.getCompleteCount(userId, startDate, endDate)
                .stream().collect(Collectors.toMap(TaskCompletionRateVO::getDate, TaskCompletionRateVO::getCompleteCount));

        // 组装最终结果
        List<TaskCompletionRateVO> result = new ArrayList<>();
        for (LocalDate date : last7Days) {
            int totalCount = totalCountMap.getOrDefault(date, 0);
            int completeCount = completeCountMap.getOrDefault(date, 0);
            double completionRate = (totalCount == 0) ? 0 : (completeCount * 100.0 / totalCount);

            TaskCompletionRateVO vo = new TaskCompletionRateVO();
            vo.setDate(date);
            vo.setTotalCount(totalCount);
            vo.setCompleteCount(completeCount);
            vo.setCompletionRate(completionRate);
            result.add(vo);
        }

        return Result.success(result);
    }

    public List<LocalDate> getLast7Days() {
        List<LocalDate> last7Days = new ArrayList<>();
        LocalDate today = LocalDate.now();
        for (int i = 0; i < 7; i++) {
            last7Days.add(today.minusDays(i));
        }
        return last7Days;
    }

    @Override
    public Result getUserActivity() {
        Long userId = getUserId();
        Integer taskCompletedCount = taskMapper.getCompletedTaskCount(userId);
        Integer objectiveCompletedCount = objectiveHubService.getCompletedObjectiveCount(userId);
        Integer resourcePostedCount = resourceHubService.getUserResourceCount(userId);
        UserActivityVO userActivityVO = new UserActivityVO(taskCompletedCount, objectiveCompletedCount, resourcePostedCount);
        return Result.success(userActivityVO);
    }

    @Override
    public Result getUserSocialAchievement() {
        Long userId = getUserId();
        Integer receivedLikes = resourceHubService.getUserReceivedLikes(userId);
        Integer receivedComments = resourceHubService.getUserReceivedComments(userId);
        Integer receivedFavorites = resourceHubService.getUserReceivedFavorites(userId);
        UserSocialAchievementVO userSocialAchievementVO = new UserSocialAchievementVO(receivedLikes, receivedComments, receivedFavorites);
        return Result.success(userSocialAchievementVO);
    }

    @Override
    public Result<List<HeatMapVO>> getYearlyTasksHeatMap(Long userId, int year) {
        List<HeatMapVO> heatMap = taskMapper.getYearlyTasksHeatMap(userId, year);
        Map<LocalDate, Integer> map = heatMap.stream()
                .collect(Collectors.toMap(HeatMapVO::getDate, HeatMapVO::getCount));

        List<HeatMapVO> fullYear = new ArrayList<>();
        LocalDate start = LocalDate.of(year, 1, 1);
        LocalDate end = LocalDate.of(year, 12, 31);
        for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
            fullYear.add(new HeatMapVO(date, map.getOrDefault(date, 0)));
        }
        return Result.success(fullYear);
    }




}
