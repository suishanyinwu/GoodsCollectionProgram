package com.it.controller;

import com.it.entity.Ip;
import com.it.entity.Persona;
import com.it.service.IpService;
import com.it.service.PersonaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    IpService ipService;
    @Autowired
    PersonaService personaService;

    /**
     * 请求ip列表
     * @return ip的id和ipName列表
     */
    @RequestMapping("/queryIpList")
    @ResponseBody
    public List<Ip> findIP(){
        return ipService.findAll();
    }

    /**
     * 请求ip对应的角色列表
     * @param ipId ip的id
     * @return 角色id和角色name列表
     */
    @RequestMapping("/queryPersona")
    @ResponseBody
    public List<Persona> findPersonaList(String ipId){
        return personaService.findByIpId(ipId);
    }
}
