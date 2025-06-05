package com.vksfeng.quan.controller;

import com.vksfeng.quan.user_pojo.dto.UserDTO;
import com.vksfeng.quan.user_pojo.entity.User;
import com.vksfeng.quan.user_pojo.entity.UserLocation;
import com.vksfeng.quan.user_pojo.vo.UserVO;
import com.vksfeng.quan.result.Result;
import com.vksfeng.quan.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "用户模块")
@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "http://localhost:5173")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(summary = "根据id获取用户信息")
    @GetMapping("/{id}")
    public Result getUserById(@PathVariable Long id) {
        log.info("获取用户信息，id:{}", id);
        User user = userService.getUserById(id);
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return Result.success(userVO);
    }

    @Operation(summary = "更新用户信息")
    @PutMapping
    public Result update(@RequestBody UserDTO userDTO) {
        log.info("更新用户信息，id:{}", userDTO.getId());
        userService.update(userDTO);
        return Result.success();
    }

    @Operation(summary = "用户更新位置信息")
    @PutMapping("/location")
    public Result updateLocation(@RequestBody UserLocation userLocationDTO) {
        return userService.setUserLocation(userLocationDTO);
    }



}
