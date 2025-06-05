package com.vksfeng.quan.scheduler;

import com.vksfeng.quan.mapper.TaskMapper;
import com.vksfeng.quan.objectivehub_pojo.entity.Task;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
public class TaskStatusScheduler {

    @Autowired
    private TaskMapper taskMapper;

    @Scheduled(cron = "0 0 0 * * ?") // 每天凌晨执行
    public void refreshRecurringTasks() {
        doRefresh();
    }

    public void doRefresh() {
        log.info("开始刷新周期任务状态...");
        List<Task> tasks = taskMapper.findRecurringCompletedTasks();

        LocalDateTime now = LocalDateTime.now();
        for (Task task : tasks) {
            LocalDateTime completedAt = task.getCompletedAt();
            if (completedAt == null) {
                continue;
            }
            boolean needReset = switch (task.getPeriod()) {
//                case "daily" -> Duration.between(completedAt, now).toDays() >= 1;
//                case "weekly" -> Duration.between(completedAt, now).toDays() >= 7;
                case "daily" -> now.isAfter(completedAt.plusDays(1));
                case "weekly" -> now.isAfter(completedAt.plusWeeks(1));
                case "monthly" -> now.isAfter(completedAt.plusMonths(1));
                default -> false;
            };
            if (needReset) {
                task.setStatus("pending");
                task.setUpdatedAt(now);
                task.setCompletedAt(null);
                taskMapper.updateById(task, task.getId());
                log.info("任务 [{}] 状态已重置为 pending", task.getId());
            }
        }
        log.info("周期任务状态刷新完毕");
    }
}
