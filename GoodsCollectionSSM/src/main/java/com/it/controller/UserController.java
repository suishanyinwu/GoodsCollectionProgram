package com.it.controller;

import com.it.entity.User;
import com.it.entity.WXuser;
import com.it.service.UserService;
import com.it.service.WeiChatService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private WeiChatService weiChatService;


    /**
     * 修改用户的个人资料
     * @param userId 用户id
     * @param userName 修改后的用户名
     * @param userImgUrl 修改后的用户头像
     * @return 新的用户信息
     */
    @RequestMapping(value = "/updateUserInfo",method = RequestMethod.POST)
    @ResponseBody
    public User updateUserInfo(@RequestParam String userId,@RequestParam(required = false) String userName,@RequestParam(required = false) String userImgUrl){
        //修改用户信息
        User newUser = new User();
        newUser.setUserId(userId);
        newUser.setUserName(userName);
        newUser.setUserImgUrl(userImgUrl);

        userService.updateUser(newUser);

        //返回新的用户信息
        User newUserInfo = userService.findById(userId);
        return newUserInfo;
    }

    /**
     * 微信小程序授权登录
     * @param code 临时令牌
     * @return 用户信息和登录信息
     */
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> login(String code){

        //封装返回值
        Map<String,Object> map=new TreeMap<>();

        //获取openid和session_key或者异常信息 用map封装
        Map<String, Object> result = weiChatService.login(code);
        if(result.get("openid")==null){  //没有正常获取值 返回登录失败信息
            map.put("loginmsg",false);
            map.put("errcode",result.get("errcode"));
            map.put("errmsg",result.get("errmsg"));
            map.put("rid",result.get("rid"));
            return map;
        }


        WXuser wxUser = new WXuser();
        wxUser.setOpenid((String) result.get("openid"));
        wxUser.setSession_key((String) result.get("session_key"));
        map.put("loginmsg",true);

        //查询是否有该用户
        WXuser trueWXUser = weiChatService.findByopenid(wxUser.getOpenid());
        if(trueWXUser!=null){  //用户已存在
            //搜索用户信息
            wxUser.setUserId(trueWXUser.getUserId());
            weiChatService.updateWXUserInfo(wxUser); //更新session_key

        }else{  //用户不存在 新增用户
            //新增用户信息
            User user = new User();

            //随机生成userID 新建用户
            String randomID=null;
            do{
                randomID=RandomStringUtils.random(15, true, false);
            }while(null!=userService.findById(randomID));
            //新增wx用户
            user.setUserId(randomID);
            user.setUserName("默认用户名");
            wxUser.setUserId(user.getUserId());
            weiChatService.addWXUser(user,wxUser);
        }

        //查询并封装用户信息和openid,session_key
        User userInfo = userService.findById(wxUser.getUserId());
        if(userInfo!=null){
            map.put("userId",userInfo.getUserId());
            map.put("userName",userInfo.getUserName());
            map.put("userImgUrl",userInfo.getUserImgUrl());
            map.put("phone",userInfo.getPhone());
            map.put("power",userInfo.getPower());
        }
        map.put("openid",wxUser.getOpenid());
        map.put("session_key",wxUser.getSession_key());
        return map;
    }


}
