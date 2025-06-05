package com.vksfeng.quan.service;

import com.vksfeng.quan.peerhub_pojo.vo.UserSearchVO;
import com.vksfeng.quan.result.Result;
import com.vksfeng.quan.user_pojo.dto.UserDTO;
import com.vksfeng.quan.user_pojo.entity.User;
import com.vksfeng.quan.user_pojo.entity.UserLocation;
import com.vksfeng.quan.user_pojo.vo.UserVO;

import java.util.List;

public interface UserService {
    void register(UserDTO userDTO);

    User login(UserDTO userDTO);

    User getUserById(Long id);

    void update(UserDTO userDTO);

    User getUserByEmail(String email);

    Result setUserLocation(UserLocation userLocationDTO);

    List<UserSearchVO> searchUsers(List<Long> userIds, Long userId);

}
