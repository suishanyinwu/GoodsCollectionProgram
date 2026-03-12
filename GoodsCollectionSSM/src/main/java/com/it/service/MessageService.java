package com.it.service;

import com.it.entity.Message;

import java.util.List;

public interface MessageService {
    /**
     * 根据时间排序，获取最新的四条资讯
     * @return 四条资讯
     */
    public List<Message> findTopFour();
}
