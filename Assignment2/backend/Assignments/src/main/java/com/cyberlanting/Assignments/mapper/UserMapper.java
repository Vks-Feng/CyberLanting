package com.cyberlanting.Assignments.mapper;

import com.cyberlanting.Assignments.pojo.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {

    @Select("select * from user")
    public List<User> getUserList();

    @Insert("insert into user(name, age) values(#{name}, #{age})")
    public void save(User user);

    @Select("select * from user where id = #{id}")
    public User getUser(Long id);

    public void update(Long id, User user);

    @Delete("delete from user where id = #{id}")
    public void remove(Long id);
}
