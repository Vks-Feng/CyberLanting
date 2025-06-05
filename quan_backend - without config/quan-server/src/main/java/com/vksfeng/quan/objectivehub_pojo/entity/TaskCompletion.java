package com.vksfeng.quan.objectivehub_pojo.entity;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

/**
 * 任务完成记录表，用于重复任务
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskCompletion {
    private Long id;
    private Long taskId;
    private Long userId;
    private LocalDateTime completedAt;  //完成时间
}

