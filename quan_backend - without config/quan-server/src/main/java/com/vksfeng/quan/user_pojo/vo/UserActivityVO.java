package com.vksfeng.quan.user_pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserActivityVO {
    private Integer taskCompletedCount;
    private Integer objectiveCompletedCount;
    private Integer resourcePostedCount;
}
