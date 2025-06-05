package com.vksfeng.quan.analysis_pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserProfileForPeerVO {
    private Integer objectiveCount;
    private Integer objectiveCompletedCount;
    private Integer taskCount;
    private Integer taskDoneCompletedCount;
}
