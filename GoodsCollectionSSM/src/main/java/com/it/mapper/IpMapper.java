package com.it.mapper;

import com.it.entity.Ip;

import java.util.List;

public interface IpMapper {
    //获取所有ip信息
    List<Ip> findAll();

    //根据ipid返回ip名字
   String findById(String ipId);
}
