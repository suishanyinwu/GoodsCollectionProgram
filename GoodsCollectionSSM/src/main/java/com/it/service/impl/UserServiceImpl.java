package com.it.service.impl;

import com.it.entity.User;
import com.it.entity.WXuser;
import com.it.mapper.UserMapper;
import com.it.mapper.WXuserMapper;
import com.it.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public User findById(String userId){
        return userMapper.findById(userId);
    }
    @Override
    public void updateUser(User user) {
        userMapper.updateUser(user);
    }

}
