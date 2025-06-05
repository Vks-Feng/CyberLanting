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
public class ObjectiveDTO {
    private Long id;
    private Long objectiveId;
    private Long userId;
    private String name;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean visibility;
    private Double weight;
}
