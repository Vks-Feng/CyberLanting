package com.cyberlanting.Assignments.service;

import com.cyberlanting.Assignments.mapper.UserMapper;
import com.cyberlanting.Assignments.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;
    public List<User> getUserList() {
        return userMapper.getUserList();
    }

    public void save(User user) {
        userMapper.save(user);
    }

    public User getUser(Long id) {
        return userMapper.getUser(id);
    }

    public void update(Long id, User user) {
        userMapper.update(id, user);
    }

    public void remove(Long id) {
        userMapper.remove(id);
    }
}
