package com.vksfeng.quan.obengine_pojo.dto;

import com.vksfeng.quan.objectivehub_pojo.entity.Task;
import com.vksfeng.quan.objectivehub_pojo.vo.ObjectiveVO;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class SubObjectiveDTO {
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
    private List<Task> tasks;
}
