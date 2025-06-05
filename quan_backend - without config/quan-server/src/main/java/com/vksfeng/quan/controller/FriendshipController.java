package com.vksfeng.quan.controller;

import com.vksfeng.quan.context.BaseContext;
import com.vksfeng.quan.peerhub_pojo.dto.FriendshipDTO;
import com.vksfeng.quan.peerhub_pojo.vo.UserSearchVO;
import com.vksfeng.quan.result.Result;
import com.vksfeng.quan.service.FriendshipService;
import com.vksfeng.quan.user_pojo.vo.UserVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "好友管理")
@RestController
@RequestMapping("/friends")
@Slf4j
public class FriendshipController {

    @Autowired
    private FriendshipService friendshipService;

    @Operation(summary = "获取好友列表")
    @GetMapping("/list")
    public Result<List<UserVO>> getFriends() {
        Long userId = BaseContext.getCurrentId();
        if (userId == null) {
            return Result.error("用户未登录");
        }
        log.info("获取好友列表,userId:{}", userId);
        List<UserVO> friends = friendshipService.getFriends(userId);
        return Result.success(friends);
    }

    @Operation(summary = "获取好友申请信息")
    @GetMapping("/requestlist")
    public Result<List<UserVO>> getFriendRequests() {
        Long userId = BaseContext.getCurrentId();
        if (userId == null) {
            return Result.error("用户未登录");
        }
        List<UserVO> request = friendshipService.getRequests(userId);
        return Result.success(request);
    }

    @Operation(summary = "搜索用户以添加好友")
    @GetMapping("/search")
    public Result<UserSearchVO> searchUser(@RequestParam Long id) {
        return friendshipService.searchUser(id);
    }

    @Operation(summary = "添加好友")
    @PostMapping("/request")
    public Result<String> addFriendRequest(@RequestBody FriendshipDTO friendshipDTO) {
        Long userId = BaseContext.getCurrentId();
        if (userId == null) {
            return Result.error("用户未登录");
        }
        if (userId.equals(friendshipDTO.getUserId())) {
            return Result.error("你已经是自己的好朋友啦，就不要加自己了");
        }
        friendshipDTO.setUserId(userId);
        friendshipDTO.setStatus("pending");
        friendshipService.addFriendRequest(friendshipDTO);
        return Result.success("添加申请已发送");
    }

    @Operation(summary = "同意好友请求")
    @PostMapping("/accept")
    public Result<String> acceptFriend(@RequestBody FriendshipDTO friendshipDTO) {
        Long userId = BaseContext.getCurrentId();
        if (userId == null) {
            return Result.error("用户未登录");
        }
        friendshipDTO.setFriendId(userId);
        friendshipDTO.setStatus("accepted");
        friendshipService.acceptFriendRequest(friendshipDTO);
        return Result.success("好友申请已同意");
    }

    @Operation(summary = "拒绝好友请求/删除好友")
    @PostMapping("/remove")
    public Result<String> removeFriend(@RequestBody FriendshipDTO friendshipDTO) {
        Long userId = BaseContext.getCurrentId();
        if (userId == null) {
            return Result.error("用户未登录");
        }
        friendshipDTO.setUserId(userId);
        friendshipService.removeFriend(friendshipDTO);
        return Result.success("好友申请已拒绝");
    }

    @Operation(summary = "附近的人推荐")
    @GetMapping("/nearby")
    public Result<List<UserSearchVO>> nearbyUsers() {
        return friendshipService.getNearbyUsers();
    }
}
