package com.vksfeng.quan.service;

import com.vksfeng.quan.peerhub_pojo.dto.FriendshipDTO;
import com.vksfeng.quan.peerhub_pojo.vo.UserSearchVO;
import com.vksfeng.quan.result.Result;
import com.vksfeng.quan.user_pojo.vo.UserVO;

import java.util.List;

public interface FriendshipService {
    List<UserVO> getFriends(Long userId);

    void addFriendRequest(FriendshipDTO friendshipDTO);

    List<UserVO> getRequests(Long userId);

    void acceptFriendRequest(FriendshipDTO friendshipDTO);

    void removeFriend(FriendshipDTO friendshipDTO);

    public boolean isExist(Long aId, Long bId);

    Result<UserSearchVO> searchUser(Long id);

    Result<List<UserSearchVO>> getNearbyUsers();
}
