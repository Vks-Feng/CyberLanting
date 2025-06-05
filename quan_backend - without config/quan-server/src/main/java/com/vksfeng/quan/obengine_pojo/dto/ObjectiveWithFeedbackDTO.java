package com.vksfeng.quan.obengine_pojo.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class ObjectiveWithFeedbackDTO {
    private Long id;
    private String name;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<Feedback> feedback;
}
