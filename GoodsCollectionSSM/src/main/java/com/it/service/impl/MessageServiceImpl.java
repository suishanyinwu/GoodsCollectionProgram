package com.it.service.impl;

import com.it.entity.Message;
import com.it.mapper.MessageMapper;
import com.it.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageMapper mapper;

    @Override
    public List<Message> findTopFour() {
        return mapper.findTopFour();
    }
}
