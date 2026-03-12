package com.it.controller;

import com.it.entity.Ip;
import com.it.entity.NoticeList;
import com.it.entity.TagList;
import com.it.service.IpService;
import com.it.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/notice")
public class NoticeController {
    @Autowired
    NoticeService noticeService;

    /**
     * 根据用户id返回通知信息
     * @param userId 用户id
     * @return 通知信息
     */
    @RequestMapping(value = "/queryNoticeList",method = RequestMethod.POST)
    @ResponseBody
    public List<NoticeList> findNoticeList(@RequestParam("userId") String userId,@RequestParam("num") String num,@RequestParam("pageSize") String pageSize){
        //获取长度
        int length=Integer.parseInt(num);
        int size=Integer.parseInt(pageSize);
        return noticeService.findByUserId(userId,length,size);
    }

    /**
     * 根据用户id返回用户tag
     * @param userId 用户id
     * @return 用户tag
     */
    @RequestMapping("/queryUserTag")
    @ResponseBody
    public List<Ip> findUserTag(String userId){
        return noticeService.findUserTag(userId);
    }

    /**
     * 添加用户tag
     * @param userId 用户id
     * @param ipId ipId
     */
    @RequestMapping("/addNewUserTag")
    @ResponseBody
    public void addNewUserTag(@RequestParam("userId") String userId,@RequestParam("ipId") String ipId){
        TagList tagList = new TagList();
        tagList.setUserId(userId);
        tagList.setIpId(ipId);

        noticeService.addNewUserTag(tagList);
    }

    /**
     * 删除用户tag
     * @param userId 用户id
     * @param ipId ipID
     */
    @RequestMapping("/deleteUserTag")
    @ResponseBody
    public void deleteUserTag(@RequestParam("userId") String userId,@RequestParam("ipId") String ipId){
        TagList tagList = new TagList();
        tagList.setUserId(userId);
        tagList.setIpId(ipId);

        noticeService.deleteUserTag(tagList);
    }

    /**
     * 获取未被收藏的IP
     * @return IP列表
     */
    @RequestMapping("/queryIP")
    @ResponseBody
    public List<Ip> findIP(@RequestParam("userId")String userId){
        return noticeService.findUnTagIP(userId);
    }


}
