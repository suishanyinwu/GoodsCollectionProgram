package com.it.controller;

import com.it.entity.Goods;
import com.it.service.GoodsService;
import com.it.service.RecService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Controller
@RequestMapping("/recommend")
public class RecController {

    @Autowired
    RecService recService;
    @Autowired
    GoodsService goodsService;

    @ResponseBody
    @RequestMapping("/goodsList")
    public List<Map<String,Object>> recommendGoods(String userId){
        List<Goods> goodsInitList=recService.recommendGoods(userId);
        if(goodsInitList==null){
            return null;
        }
        return goodsService.goodsBriefInfo(goodsInitList);
    }
}
