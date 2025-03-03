package com.cyberlanting.Assignments.controller;

import com.cyberlanting.Assignments.pojo.User;
import com.cyberlanting.Assignments.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Tag(name = "用户模块")
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 处理"/users/"的GET请求，用来获取用户列表
     *
     * @return
     */
    @Operation(summary = "获取用户列表")
    @GetMapping
    public List<User> getUserList() {
        List<User> list = userService.getUserList();
        return list;
    }

    /**
     * 处理"/users/"的POST请求，用来创建User
     *
     * @param user
     * @return
     */
    @Operation(summary = "新建用户")
    @PostMapping
    public String postUser(@RequestBody User user) {
        userService.save(user);
        return "success";
    }

    /**
     * 处理"/users/{id}"的GET请求，用来获取url中id值的User信息
     *
     * @param id
     * @return
     */
    @Operation(summary = "根据id获取用户")
    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        return userService.getUser(id);
    }

    /**
     * 处理"/users/{id}"的PUT请求，用来更新User信息
     *
     * @param id
     * @param user
     * @return
     */
    @Operation(summary = "根据id修改用户")
    @PutMapping("/{id}")
    public String putUser(@PathVariable Long id, @RequestBody User user) {
        userService.update(id, user);
        return "success";
    }

    /**
     * 处理"/users/{id}"的DELETE请求，用来删除User
     *
     * @param id
     * @return
     */
    @Operation(summary = "删除用户")
    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.remove(id);
        return "success";
    }

}
