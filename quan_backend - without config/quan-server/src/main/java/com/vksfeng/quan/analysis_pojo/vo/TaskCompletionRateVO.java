package com.vksfeng.quan.analysis_pojo.vo;

import lombok.Data;

import java.time.LocalDate;

@Data
public class TaskCompletionRateVO {
    private Integer completeCount;
    private Integer totalCount;
    private Double completionRate;
    private LocalDate date;
}
