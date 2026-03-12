package com.it.mapper;

import com.it.entity.User;

public interface UserMapper {
    //根据用户id查询用户
    User findById(String userId);

    //增加用户
    void addUser(User user);

    //更新用户资料
    void updateUser(User user);

    //删除用户
    void deleteUser(String userId);

}
