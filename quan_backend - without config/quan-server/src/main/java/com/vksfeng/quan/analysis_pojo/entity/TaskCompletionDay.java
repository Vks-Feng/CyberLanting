package com.vksfeng.quan.analysis_pojo.entity;

import lombok.Data;

import java.time.LocalDate;

@Data
public class TaskCompletionDay {
    private LocalDate date;      // 日期
    private Integer count;    // 当天完成任务次数
}
