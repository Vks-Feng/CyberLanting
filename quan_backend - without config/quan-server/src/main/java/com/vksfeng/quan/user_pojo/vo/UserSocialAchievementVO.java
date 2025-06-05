package com.vksfeng.quan.user_pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSocialAchievementVO {
    private Integer receivedLikes;
    private Integer receivedComments;
    private Integer receivedFavorites;
}
