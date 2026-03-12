package com.it.controller;

import com.it.entity.Goods;
import com.it.mapper.GoodsMapper;
import com.it.service.GoodsService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Controller
@RequestMapping("/search")
public class SearchController {
    @Autowired
    GoodsService goodsService;

    /**
     * 搜索角色相关的周边信息
     * @param personaId
     * @return 周边详细信息
     */
    @ResponseBody
    @RequestMapping("/queryPersonaGoodsList")
    public List<Map<String,Object>> getPersonaGoodsList(String personaId){
        List<Goods> goodsInitList=goodsService.findByPersonaId(personaId);
        return goodsService.goodsBriefInfo(goodsInitList);
    }

    /**
     * 根据通知列表展示的周边信息
     * @param ipId  ip信息
     * @param dateTime 发布时间
     * @return 周边信息列表
     */
    @ResponseBody
    @RequestMapping("/queryIPTimeGoodsList")
    public List<Map<String,Object>> getIPTimeGoodsList(String ipId,String dateTime){
        List<Goods> goodsInitList=goodsService.findByIPTime(ipId, dateTime);
        return goodsService.goodsBriefInfo(goodsInitList);
    }

    /**
     * 搜索店铺下的所有周边信息
     * @param brandId 店铺id
     * @return 周边列表信息
     */
    @RequestMapping("/queryBrandGoodsList")
    @ResponseBody
    public List<Map<String,Object>> getBrandGoodsList(String brandId){
        List<Goods> goodsInitList=goodsService.findByBrandId(brandId);
        return goodsService.goodsBriefInfo(goodsInitList);
    }


    /**
     * 模糊搜索得到谷子列表
     * @param searchValue
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/querySearchGoodsList",method = RequestMethod.POST)
    public List<Map<String,Object>> getSearchGoodsList(@RequestParam("searchValue") String searchValue){
        List<Goods> goodsInitList=goodsService.searchByText(searchValue);
        return goodsService.goodsBriefInfo(goodsInitList);
    }

}
