package com.it.controller;

import com.it.entity.Message;
import com.it.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/index")
public class MessageController {
    @Autowired
    private MessageService messageService;

    /**
     * 获取最新的四条资讯
     * @return 四条咨询信息
     */
    @ResponseBody
    @RequestMapping(value = "/newInfoList")
    public List<Message> find(){
        return messageService.findTopFour();
    }
}
