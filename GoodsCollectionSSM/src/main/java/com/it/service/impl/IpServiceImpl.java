package com.it.service.impl;

import com.it.entity.Ip;
import com.it.mapper.IpMapper;
import com.it.service.IpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class IpServiceImpl implements IpService {

    @Autowired
    IpMapper ipMapper;

    @Override
    public List<Ip> findAll() {
        return ipMapper.findAll();
    }

    @Override
    public String findById(String ipId) {
        return ipMapper.findById(ipId);
    }
}
