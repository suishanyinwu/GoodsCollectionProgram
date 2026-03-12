package com.it.mapper;

import com.it.entity.Message;

import java.util.List;

public interface MessageMapper {
    public List<Message> findTopFour();
}
