package com.it.service;

import com.it.entity.User;
import com.it.entity.WXuser;

public interface UserService {
    /**
     * 根据用户id查找用户信息
     * @param userId 用户id
     * @return user用户信息
     */
    public User findById(String userId);


    /**
     * 更新用户信息
     * @param user 用户信息
     */
    public void updateUser(User user);


}
