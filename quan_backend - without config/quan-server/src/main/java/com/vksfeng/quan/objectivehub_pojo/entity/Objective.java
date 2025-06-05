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
public class Objective {

    private Long id; // 主键
    private Long userId; // 用户ID
    private String name; // 目标名称
    private String description; // 目标描述
    private LocalDate startDate; // 开始日期
    private LocalDate endDate; // 结束日期
    private Boolean visibility; // 可见性
    private Double progress; // 进度
    private Double weight; // 权重
    private LocalDateTime completedAt; // 完成时间
    private LocalDateTime createdAt; // 创建时间
    private LocalDateTime updatedAt; // 更新时间

}