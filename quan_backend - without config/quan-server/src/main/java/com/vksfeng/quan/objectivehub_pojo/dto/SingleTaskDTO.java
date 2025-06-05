package com.vksfeng.quan.objectivehub_pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SingleTaskDTO {
    // task共有
    private Long id;
    private Long userId;
    private Long objectiveId;
    private String name;
    private String description;
    private String status; // 'pending' or 'completed'
    private Boolean visibility;
    private String type;   // 'single' or 'recurring'

    // single_task类特有
    private LocalDate ddl; // 单次任务的截止日期
    private LocalDateTime completedAt; // 单次任务的完成时间
}

