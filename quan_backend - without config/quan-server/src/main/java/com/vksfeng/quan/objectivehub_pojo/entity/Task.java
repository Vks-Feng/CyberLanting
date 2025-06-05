package com.vksfeng.quan.objectivehub_pojo.entity;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Task {

    private Long id; // 主键
    private Long userId; // 用户ID
    private String name; // 任务名称
    private String description; // 任务描述
    private String status; // 'pending' or 'completed'
    private Boolean visibility; // 可见性
    private String type;   // 'single' or 'recurring'
    private LocalDateTime createdAt; // 创建时间
    private LocalDateTime updatedAt; // 更新时间

    private LocalDate ddl; // 单次任务的截止日期
    private LocalDateTime completedAt; // 单次任务的完成时间

    private String period; // 周期（"daily", "weekly", "monthly"）
    private LocalDate startDate; // 周期性任务的开始日期
    private LocalDate endDate; // 周期性任务的结束日期
    private Integer totalCount;
    private Integer completedCount;
}