package com.vksfeng.quan.analysis_pojo.vo;

import lombok.Data;

@Data
public class LeaderboardCountVO {
    private Long userId;
    private String username;
    private String avatarUrl;
    private Integer completeCount;
}
