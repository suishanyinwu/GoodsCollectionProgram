package com.it.service.impl;

import com.it.entity.Ip;
import com.it.entity.NoticeList;
import com.it.entity.TagList;
import com.it.mapper.NoticeListMapper;
import com.it.mapper.TagListMapper;
import com.it.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;

@Service
@Transactional
public class NoticeServiceImpl implements NoticeService {
    @Autowired
    NoticeListMapper noticeListMapper;
    @Autowired
    TagListMapper tagListMapper;



    @Override
    public List<NoticeList> findByUserId(String userId,int length,int pageSize) {
        return noticeListMapper.findByUserId(userId,length,pageSize);
    }

    @Override
    public void addNewUserTag(TagList tagList) {
        tagListMapper.addNewUserTag(tagList);
    }

    @Override
    public void deleteUserTag(TagList tagList) {
        tagListMapper.deleteUserTag(tagList);
    }

    @Override
    public List<Ip> findUserTag(String userId) {
        return tagListMapper.findUserTag(userId);
    }

    @Override
    public List<Ip> findUnTagIP(String userId) {
        return tagListMapper.findUnTagIP(userId);
    }
}
