package com.vksfeng.quan.objectivehub_pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ObjectiveVO {
    private Long id;
    private Long userId;
    private String name;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean visibility;
    private Double progress;
    private Double weight;
    private LocalDateTime createdAt;
    private List<ObjectiveVO> children;
    private List<TaskVO> tasks;
}
