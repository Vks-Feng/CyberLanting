package com.vksfeng.quan.objectivehub_pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecurringTaskDTO {
    // task共有
    private Long id;
    private Long userId;
    private Long objectiveId;
    private String name;
    private String description;
    private String status; // 'pending' or 'completed'
    private Boolean visibility;
    private String type;   // 'single' or 'recurring'

    // recurring_task类特有
    private String period; // 周期（"daily", "weekly", "monthly"）
    private LocalDate startDate; // 周期性任务的开始日期
    private LocalDate endDate; // 周期性任务的结束日期
}
