package com.vksfeng.quan.analysis_pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
@AllArgsConstructor
public class HeatMapVO {
    private LocalDate date;
    private Integer count;
}
