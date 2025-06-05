package com.vksfeng.quan.mapper;

import com.vksfeng.quan.analysis_pojo.entity.TaskCompletionDay;
import com.vksfeng.quan.analysis_pojo.vo.HeatMapVO;
import com.vksfeng.quan.analysis_pojo.vo.LeaderboardCountVO;
import com.vksfeng.quan.analysis_pojo.vo.TaskCompletionRateVO;
import com.vksfeng.quan.objectivehub_pojo.entity.Task;
import com.vksfeng.quan.objectivehub_pojo.entity.TaskCompletion;
import com.vksfeng.quan.objectivehub_pojo.vo.TaskVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Mapper
public interface TaskMapper {

    void insert(Task task);

    void insertObjectiveTaskRelation(Long objectiveId, Long taskId);

    Task selectById(Long id);

    void updateById(Task task, Long id);

    void deleteById(Long id);

    void deleteObjectiveTaskRelationByTaskId(Long id);

    Long getObjectiveIdByTaskId(Long id);

    List<TaskVO> getTodayTasks(Long userId, LocalDate today);

    List<Long> getAllTasksIdByObjectiveId(Long objectiveId);

    void deleteObjectiveTaskRelationByObjectiveId(Long objectiveId);

    void createObjectiveTaskRelation(Long objectiveId, Long taskId);

    void deleteObjectiveTaskRelation(Long objectiveId, Long taskId);

    List<TaskVO> getMonthlyTasksCalendar(Long userId, int year, int month);

    List<TaskCompletionDay> selectDailyCompletion(Long userId, int year, int month);

    @Insert("INSERT INTO task_completion (task_id, user_id, completed_at) VALUES (#{taskId}, #{userId}, #{completedAt})")
    void addTaskCompletion(TaskCompletion taskCompletion);

    void deleteTaskCompletionByTaskId(Long taskId);

    void deleteTaskCompletionByRecordId(Long id);

    Long getLatestCompletionRecordId(Long taskId);

    List<LeaderboardCountVO> getWeeklyTaskLeaderboard(List<Long> userIds);

    List<LeaderboardCountVO> getMonthlyTaskLeaderboard(List<Long> userIds);

    Integer getTaskCount(Long userId);

    Integer getCompletedTaskCount(Long userId);

    List<TaskCompletionRateVO> getTotalCount(Long userId, LocalDate startDate, LocalDate endDate);

    List<TaskCompletionRateVO> getCompleteCount(Long userId, LocalDate startDate, LocalDate endDate);

    List<Task> findRecurringCompletedTasks();

    List<HeatMapVO> getYearlyTasksHeatMap(Long userId, int year);
}
