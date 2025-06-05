package com.vksfeng.quan.peerhub_pojo.dto;

import lombok.Data;

@Data
public class FriendshipDTO {
    private Long userId;
    private Long friendId;
    private String status;  // 'pending', 'accepted', 'blocked'
}
