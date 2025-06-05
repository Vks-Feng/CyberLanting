package com.vksfeng.quan.service.impl;

import com.vksfeng.quan.achievement.util.AchievementEventPublisher;
import com.vksfeng.quan.context.BaseContext;
import com.vksfeng.quan.exception.NotLoginException;
import com.vksfeng.quan.exception.ObjectiveCreationFailureException;
import com.vksfeng.quan.exception.TaskNotExistException;
import com.vksfeng.quan.mapper.ObjectiveMapper;
import com.vksfeng.quan.mapper.TaskMapper;
import com.vksfeng.quan.objectivehub_pojo.dto.ObjectiveDTO;
import com.vksfeng.quan.objectivehub_pojo.dto.RecurringTaskDTO;
import com.vksfeng.quan.objectivehub_pojo.dto.SingleTaskDTO;
import com.vksfeng.quan.objectivehub_pojo.entity.Objective;
import com.vksfeng.quan.objectivehub_pojo.entity.Task;
import com.vksfeng.quan.objectivehub_pojo.entity.TaskCompletion;
import com.vksfeng.quan.objectivehub_pojo.vo.ObjectiveVO;
import com.vksfeng.quan.objectivehub_pojo.vo.TaskVO;
import com.vksfeng.quan.result.Result;
import com.vksfeng.quan.service.FeedService;
import com.vksfeng.quan.service.ObjectiveHubService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static com.vksfeng.quan.constant.AchievementConstant.OBJECTIVE_ADDED;
import static com.vksfeng.quan.constant.AchievementConstant.TASK_COMPLETED;

@Service
@Slf4j
public class ObjectiveHubServiceImpl implements ObjectiveHubService {

    @Autowired
    private ObjectiveMapper objectiveMapper;

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private FeedService feedService;

    @Autowired
    private AchievementEventPublisher achievementEventPublisher;

    public Long getUserId() {
        Long userId = BaseContext.getCurrentId();
        if (userId == null) {
            throw new NotLoginException("用户未登录");
        }
        return userId;
    }

    /**
     * 新增目标
     * @param objectiveDTO
     * @return
     */
    public ObjectiveVO createObjective(ObjectiveDTO objectiveDTO) {
        Objective objective = new Objective();
//        log.info("传入的对象：{}", objectiveDTO);
        BeanUtils.copyProperties(objectiveDTO, objective);
        if (objective.getUserId() == null) {
            objective.setUserId(getUserId());
        }
        // 字段补充
        objective.setCreatedAt(LocalDateTime.now());
        objective.setUpdatedAt(LocalDateTime.now());
        // 默认字段
        objective.setVisibility(true);
        objective.setProgress(0.0);
        objective.setWeight(1.0);
        objectiveMapper.insert(objective);
        achievementEventPublisher.publish(objective.getUserId(), OBJECTIVE_ADDED, null);
        ObjectiveVO objectiveVO = new ObjectiveVO();
        BeanUtils.copyProperties(objective, objectiveVO);
        return objectiveVO;
    }

    /**
     * 获取用户目标及其结构信息
     * @param userId
     * @return
     */
    public List<ObjectiveVO> getUserObjectives(Long userId) {
        // 获取用户的所有主目标id
        List<Long> mainObjectivesIds = getAllMainObjectivesId(userId);
        if(mainObjectivesIds == null || mainObjectivesIds.isEmpty()) {
            // 如果没有主目标，则直接返回空
            return null;
        }
        // 建立主目标数组，查找所有主目标内容
        List<Objective> mainObjectives = objectiveMapper.getAllMainObjectives(mainObjectivesIds);
        List<ObjectiveVO> mainObjectiveVOs = new ArrayList<>();
        for (Objective mainObjective: mainObjectives) {
            mainObjectiveVOs.add(loadMainObjectiveDetails(mainObjective));
        }
        return mainObjectiveVOs;
    }

    /**
     * 获取用户所有主目标id
     * @param userId
     * @return
     */
    public List<Long> getAllMainObjectivesId(Long userId) {
        List<Long> mainObjectivesIds = objectiveMapper.getAllObjectivesId(userId);
        Iterator<Long> iterator = mainObjectivesIds.iterator();
        while (iterator.hasNext()) {
            Long objectiveId = iterator.next();
            if (isSubObjective(objectiveId)) {
                iterator.remove();
            }
        }
        return mainObjectivesIds;
    }

    /**
     * 判断是否为子目标，即只要没有出现在孩子列表中，则不是子目标（不能用是否出现在父目标中来判断，否则空目标不能找到）
     * @param objectiveId
     * @return
     */
    private boolean isSubObjective(Long objectiveId) {
        return objectiveMapper.isSubObjective(objectiveId);
    }

    /**
     * 根据目标 ID 获取目标信息。
     * 该方法支持主目标与子目标的判断及处理：
     *  - 若该目标没有子目标，视为子目标，直接加载相关任务信息；
     *  - 若该目标含有子目标，视为主目标，递归加载所有子目标和任务信息，并计算进度。
     * 主目标在计算进度后需要同步其完成状态，规则如下：
     * 1. 当目标未完成（即 completedAt == null）：
     *    - 若子目标或任务变动使得进度达到 1.0，视为完成，更新完成时间；
     * 2. 当目标已完成（即 completedAt != null）：
     *    - 若子目标或任务变动导致进度 < 1.0，视为未完成，清除完成时间；
     * 3. 无论目标是否完成：
     *    - 若本次计算出的进度与数据库中记录的进度不同，则更新数据库中的进度；
     *    - 否则不做更新操作，避免不必要的写操作。
     * 注意：子目标和任务的完成或取消完成会引起主目标进度的实时变动，
     * 因此每次获取主目标信息时需重新计算以确保一致性。
     * @param objectiveId
     * @return
     */
    @Transactional
    public ObjectiveVO getObjectiveById(Long objectiveId) {
        Objective objective = objectiveMapper.selectById(objectiveId);
        if (objective == null) {
            return null;
        }
        // 获取其所有子目标
        List<Long> subObjectivesIds = objectiveMapper.getAllSubObjectivesId(objectiveId);
        if (subObjectivesIds.isEmpty()) {
            // 子目标为空，则其本身是子目标，只需补全所有任务信息
            return loadSubObjectiveDetails(objective);
        } else {
            // 有子目标，则为主目标，先补全子目标信息，再补全自己的信息，包含进度计算
            ObjectiveVO objectiveVO = loadMainObjectiveDetails(objective);
            Double progress = calculateParentObjectiveProgress(objectiveVO);
            if (objective.getCompletedAt() != null) {
                // 目标完成时间不为null，则证明目标已完成
                if (progress < 1.0) {
                    // 证明任务有变动，取消完成状态
                    objectiveMapper.completeObjectiveById(null, objectiveId);
                }
            } else {
                // 目标时间为null，则证明目标未完成
                if (progress >= 1.0) {
                    // 证明任务已完成，设置完成时间
                    objectiveMapper.completeObjectiveById(LocalDateTime.now(), objectiveId);
                    feedService.autoPostObjectiveCompletion(objective);
                }
            }
            if (!Objects.equals(objective.getProgress(), progress)) {
                objectiveMapper.updateProgress(progress, objectiveId);
                objectiveVO.setProgress(progress);
            }
            return objectiveVO;
        }
    }

    /**
     * 为主目标填充信息
     * @param mainObjective
     * @return
     */
    private ObjectiveVO loadMainObjectiveDetails(Objective mainObjective) {
        List<ObjectiveVO> children = new ArrayList<>();
        List<Long> subObjectivesIds = objectiveMapper.getAllSubObjectivesId(mainObjective.getId());
        for (Long subObjectiveId : subObjectivesIds) {
            Objective subObjective = objectiveMapper.selectById(subObjectiveId);
            children.add(loadSubObjectiveDetails(subObjective));
        }
        ObjectiveVO mainObjectiveVO = new ObjectiveVO();
        BeanUtils.copyProperties(mainObjective, mainObjectiveVO);
        mainObjectiveVO.setChildren(children);
        return mainObjectiveVO;
    }

    /**
     * 为子目标补全信息
     * @param subObjective
     * @return
     */
    public ObjectiveVO loadSubObjectiveDetails(Objective subObjective) {
        ObjectiveVO subObjectiveVO = new ObjectiveVO();
        BeanUtils.copyProperties(subObjective, subObjectiveVO);
        // 补全所有任务信息
        List<TaskVO> tasks = new ArrayList<>();
        List<Long> taskIds = taskMapper.getAllTasksIdByObjectiveId(subObjective.getId());
        for (Long taskId : taskIds) {
            Task task = taskMapper.selectById(taskId);
            TaskVO taskVO = new TaskVO();
            BeanUtils.copyProperties(task, taskVO);
            tasks.add(taskVO);
        }
        subObjectiveVO.setTasks(tasks);
        return subObjectiveVO;
    }

    /**
     * 根据id更新目标
     * @param objectiveDTO
     * @param objectiveId
     */
    public void updateObjective(ObjectiveDTO objectiveDTO, Long objectiveId) {
        Objective objective = new Objective();
        BeanUtils.copyProperties(objectiveDTO, objective);
        objectiveMapper.updateById(objective, objectiveId);
    }

    /**
     * 根据id删除目标
     * @param objectiveId
     */
    @Transactional
    public void deleteObjective(Long objectiveId) {
        // 获取其所有子目标
        List<Long> subObjectivesIds = objectiveMapper.getAllSubObjectivesId(objectiveId);
        if (subObjectivesIds.isEmpty()) {
            // 子目标为空，则其本身是子目标，删除子目标与任务的关系及任务本身
            deleteSubObjective(objectiveId);
        } else {
            // 有子目标，则自己为父目标
            // 先删除所有子目标，再删除本身
            for (Long subObjectiveId : subObjectivesIds) {
                deleteSubObjective(subObjectiveId);
            }
            // 把自己和所有子目标与的关系删除掉
            objectiveMapper.deleteRelationByFatherId(objectiveId);
            // 删除ai指导
            objectiveMapper.deleteAiGuideForObjective(objectiveId);
            // 再删除自己
            objectiveMapper.deleteById(objectiveId);
        }
    }

    @Override
    public void addAiGuideForObjective(Long objectiveId, String aiGuide) {
        objectiveMapper.addAiGuideForObjective(objectiveId, aiGuide);
    }

    /**
     * 根据id删除子目标
     * @param objectiveId
     */
    @Transactional
    public void deleteSubObjective(Long objectiveId) {

        // 获取其所有任务id
        List<Long> tasksIds = taskMapper.getAllTasksIdByObjectiveId(objectiveId);
        // 先删除该子目标与任务的关系
        for (Long taskId : tasksIds) {
            // 再删除所有任务
            deleteTask(taskId);
        }
        // 先删除子目标与父目标的关联关系
        objectiveMapper.deleteRelationByChildId(objectiveId);
        // 再删除目标本身
        objectiveMapper.deleteById(objectiveId);
    }

    /**
     * 获取用户今日需要完成的任务
     * @param userId
     * @return
     */
    public List<TaskVO> getUserTodayTasks(Long userId) {
        List<TaskVO> task = taskMapper.getTodayTasks(userId, LocalDate.now());
        return task;
    }

    /**
     * 创建单次任务
     * @param taskDTO
     * @return
     */
    @Transactional
    public TaskVO createSingleTask(SingleTaskDTO taskDTO) {
        // 补充字段，准备插入
        Task task = new Task();
        BeanUtils.copyProperties(taskDTO, task);
        if (task.getUserId() == null) {
            task.setUserId(getUserId());
        }
        task.setCreatedAt(LocalDateTime.now());
        task.setUpdatedAt(LocalDateTime.now());
        task.setStatus("pending");
        task.setVisibility(true);
        task.setType("single");
        // 这俩字段对于单次任务来说，都是1和0，无意义，后续也不改动
        task.setTotalCount(1);
        task.setCompletedCount(0);
        taskMapper.insert(task);

        // 获取目标id，建立与目标的关系
        Long objectiveId = taskDTO.getObjectiveId();
        Long taskId = task.getId();
        if (objectiveId != null) {
            // 建立关系
            taskMapper.insertObjectiveTaskRelation(objectiveId, taskId);
        } else {
            throw new IllegalArgumentException("任务不能独立存在");
        }

        // 返回VO
        TaskVO taskVO = new TaskVO();
        BeanUtils.copyProperties(task, taskVO);
        taskVO.setObjectiveId(objectiveId);

        return taskVO;
    }

    public Integer calculateTaskTotalCount(LocalDate start, LocalDate end, String period) {
        // period 为 daily weekly monthly
        if (start.isAfter(end)) {
            throw new IllegalArgumentException("Start date must be before or equal to end date.");
        }

        switch (period.toLowerCase()) {
            case "daily":
                return (int) ChronoUnit.DAYS.between(start, end) + 1; // 包含起始和结束日期
            case "weekly":
                long weeks = ChronoUnit.WEEKS.between(start, end);
                // 如果起始日期和结束日期在同一周，也算作一周
                if (weeks == 0) {
                    return 1;
                }
                // 计算完整的周数，并加上起始日期所在的那周
                return (int) (weeks + 1);
            case "monthly":
                long months = ChronoUnit.MONTHS.between(start, end);
                // 如果起始日期和结束日期在同一个月，也算作一个月
                if (months == 0) {
                    return 1;
                }
                // 计算完整的月数，并加上起始日期所在的那个月
                return (int) (months + 1);
            default:
                throw new IllegalArgumentException("Invalid period. Period must be 'daily', 'weekly', or 'monthly'.");
        }
    }

    /**
     * 创建重复任务
     * @param taskDTO
     * @return
     */
    @Transactional
    public TaskVO createRecurringTask(RecurringTaskDTO taskDTO) {
        // 补充字段，准备插入
        Task task = new Task();
        BeanUtils.copyProperties(taskDTO, task);
        if (task.getUserId() == null) {
            task.setUserId(getUserId());
        }
        task.setCreatedAt(LocalDateTime.now());
        task.setUpdatedAt(LocalDateTime.now());
        task.setStatus("pending");
        task.setVisibility(true);
        task.setType("recurring");
        task.setTotalCount(calculateTaskTotalCount(taskDTO.getStartDate(),taskDTO.getEndDate(), taskDTO.getPeriod()));
        task.setCompletedCount(0);
        taskMapper.insert(task);

        // 获取目标id，建立与目标的关系
        Long objectiveId = taskDTO.getObjectiveId();
        Long taskId = task.getId();
        if (objectiveId != null) {
            // 建立关系
            taskMapper.insertObjectiveTaskRelation(objectiveId, taskId);
        } else {
            throw new IllegalArgumentException("任务不能独立存在");
        }

        // 返回VO
        TaskVO taskVO = new TaskVO();
        BeanUtils.copyProperties(task, taskVO);
        taskVO.setObjectiveId(objectiveId);

        return taskVO;
    }

    /**
     * 根据id获取任务信息
     * @param id
     * @return
     */
    public TaskVO getTaskById(Long id) {
        Task task = taskMapper.selectById(id);
        TaskVO taskVO = new TaskVO();
        BeanUtils.copyProperties(task, taskVO);
        Long objectiveId = taskMapper.getObjectiveIdByTaskId(id);
        taskVO.setObjectiveId(objectiveId);
        return taskVO;
    }

    /**
     * 更新单次任务
     * @param id
     */
    public void updateSingleTask(SingleTaskDTO taskDTO, Long id) {
        Task task = new Task();
        BeanUtils.copyProperties(taskDTO, task);
        task.setUpdatedAt(LocalDateTime.now());
        taskMapper.updateById(task, id);
    }

    /**
     * 更新重复任务
     * @param id
     */
    @Transactional
    public void updateRecurringTask(RecurringTaskDTO taskDTO, Long id) {
        Task task = new Task();
        BeanUtils.copyProperties(taskDTO, task);
        task.setUpdatedAt(LocalDateTime.now());
        if (taskDTO.getStartDate() != null || taskDTO.getEndDate() != null) {
            task.setTotalCount(calculateTaskTotalCount(taskDTO.getStartDate(), taskDTO.getEndDate(), taskDTO.getPeriod()));
        }
        taskMapper.updateById(task, id);
    }

    /**
     * 删除任务及其关联信息
     * @param id
     */
    @Transactional
    public void deleteTask(Long id) {
        // 先删关系
        taskMapper.deleteObjectiveTaskRelationByTaskId(id);
        taskMapper.deleteTaskCompletionByTaskId(id);
        // 再删任务
        taskMapper.deleteById(id);
    }

    public void createObjectiveRelation(Long parentObjectiveId, Long childObjectiveId) {
        objectiveMapper.createObjectiveRelation(parentObjectiveId, childObjectiveId);
    }

    public void createObjectiveTaskRelation(Long objectiveId, Long taskId) {
        taskMapper.createObjectiveTaskRelation(objectiveId, taskId);
    }

    public void deleteObjectiveRelation(Long parentObjectiveId, Long childObjectiveId) {
        objectiveMapper.deleteObjectiveRelation(parentObjectiveId, childObjectiveId);
    }

    public void deleteObjectiveTaskRelation(Long objectiveId, Long taskId) {
        taskMapper.deleteObjectiveTaskRelation(objectiveId, taskId);
    }

    @Transactional
    public Result completeTask(Long id) {
        // 判断任务类型
        Task task = taskMapper.selectById(id);
        if (task == null) {
            throw new TaskNotExistException("任务不存在");
        }
        LocalDateTime now = LocalDateTime.now();
        if (task.getType().equals("single")) {
            if (task.getStatus().equals("completed")) {
                // 任务已完成，不重复执行
                return Result.success("任务已经完成，无需重复操作");
            }
            task.setStatus("completed");
            task.setCompletedAt(now);
            task.setUpdatedAt(now);
            taskMapper.updateById(task, id);
        } else if (task.getType().equals("recurring")) {
            // TODO 下一周期自动刷新状态
            if (task.getStatus().equals("completed")) {
                // 任务已完成，不重复执行
                return Result.success("已完成本周期内的任务");
            }
            task.setCompletedCount(task.getCompletedCount() + 1);
            task.setStatus("completed");
            task.setCompletedAt(now);
            task.setUpdatedAt(now);
            taskMapper.updateById(task, id);
        }
        TaskCompletion taskCompletion = TaskCompletion.builder()
                .taskId(id)
                .userId(task.getUserId())
                .completedAt(now)
                .build();
        taskMapper.addTaskCompletion(taskCompletion);
        // 自动发布动态
        feedService.autoPostTaskCompletion(task);
        // 发布完成任务时间
        achievementEventPublisher.publish(task.getUserId(), TASK_COMPLETED, null);
//        Long objectiveId = taskMapper.getObjectiveIdByTaskId(id);
//        Long fatherObjectiveId = objectiveMapper.getFatherObjectiveIdBySubObjectiveId(objectiveId);
//        if (fatherObjectiveId != null) {
//            objectiveId = fatherObjectiveId;
//        }
//        calculateProgress();
        return Result.success();
    }

    @Transactional
    public void deleteTaskCompletion(Long id) {
        LocalDateTime now = LocalDateTime.now();
        Task task = taskMapper.selectById(id);
        if (task == null) {
            throw new TaskNotExistException("任务不存在");
        }
        if (task.getStatus().equals("pending")) {
            // 任务未完成，不执行取消操作
            return;
        }
        if (task.getType().equals("single")) {
            task.setStatus("pending");
            task.setCompletedAt(null);
            task.setUpdatedAt(now);
            taskMapper.updateById(task, id);
            taskMapper.deleteTaskCompletionByTaskId(id);
        } else if (task.getType().equals("recurring")) {
            task.setStatus("pending");
            task.setUpdatedAt(now);
            task.setCompletedCount(task.getCompletedCount() - 1);
            taskMapper.updateById(task, id);
            // 获取最新的一条完成记录并删除
            Long recordId = taskMapper.getLatestCompletionRecordId(id);
            taskMapper.deleteTaskCompletionByRecordId(recordId);
        }
    }

    @Transactional
    public ObjectiveVO createObjectiveWithRelation(ObjectiveDTO objectiveDTO, Long parentObjectiveId) {
        ObjectiveVO objectiveVO = createObjective(objectiveDTO);
        Long childId = objectiveVO.getId();
        if (parentObjectiveId == null) {
            throw new ObjectiveCreationFailureException("未指定父任务");
        }
        if (parentObjectiveId.equals(childId)) {
            throw new ObjectiveCreationFailureException("父任务不能是子任务");
        }
        createObjectiveRelation(parentObjectiveId, objectiveVO.getId());

        return objectiveVO;
    }

    @Override
    public Integer getObjectiveCount(Long userId) {
        return objectiveMapper.getObjectiveCount(userId);
    }

    @Override
    public Integer getCompletedObjectiveCount(Long userId) {
        return objectiveMapper.getCompletedObjectiveCount(userId);
    }

    @Override
    public Integer getTaskCount(Long userId) {
        return taskMapper.getTaskCount(userId);
    }

    @Override
    public Integer getCompletedTaskCount(Long userId) {
        return taskMapper.getCompletedTaskCount(userId);
    }

    @Override
    public Result getAiGuideForObjective(Long objectiveId) {
        String aiGuide = objectiveMapper.getAiGuideForObjective(objectiveId);
        return Result.success((Object) aiGuide);
    }

    public double calculateTaskProgress(TaskVO task) {
        if ("single".equals(task.getType())) {
            return "completed".equals(task.getStatus()) ? 1.0 : 0.0;
        } else if ("recurring".equals(task.getType())) {
            return task.getTotalCount() > 0 ?
                    (double) task.getCompletedCount() / task.getTotalCount() : 0.0;
        }
        return 0.0;
    }

    public Double calculateProgress(ObjectiveVO objectiveVO) {
        if (objectiveVO == null) {
            return 1.0;
        }

        List<TaskVO> tasks = objectiveVO.getTasks();
        if (tasks == null || tasks.isEmpty()) {
            return 1.0;
        }

        double totalProgress = 0.0;
        int taskCount = 0;

        for (TaskVO task : tasks) {
            totalProgress += calculateTaskProgress(task);
            taskCount++;
        }

        return taskCount > 0 ? totalProgress / taskCount : 0.0;
    }

    /**
     * 计算父目标，真正用于展示的数据
     * @param parentObjective
     * @return
     */
    public Double calculateParentObjectiveProgress(ObjectiveVO parentObjective) {
        if (parentObjective == null || parentObjective.getChildren() == null) {
            return 1.0;
        }
//        log.info("正在计算{}的进度", parentObjective.getName());
        double totalProgress = 0.0;
        int childCount = 0;

        for (ObjectiveVO child : parentObjective.getChildren()) {
//            log.info("{}的进度为{}", child.getName(), calculateProgress(child));
            totalProgress += calculateProgress(child);
            childCount++;
        }
        return childCount > 0 ? totalProgress / childCount : 0.0;
    }



}
