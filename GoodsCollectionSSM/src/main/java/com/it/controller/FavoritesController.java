package com.it.controller;

import com.it.entity.Brand;
import com.it.entity.Goods;
import com.it.service.FavoritesService;
import com.it.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Controller
@RequestMapping("/favorites")
public class FavoritesController {
    @Autowired
    FavoritesService favoritesService;
    @Autowired
    GoodsService goodsService;

    /**
     * 搜索用户收藏的周边信息
     * @param userId 用户信息
     * @param num 已获取的数量
     * @return 周边信息列表
     */
    @ResponseBody
    @RequestMapping(value = "/queryFavGoods",method= RequestMethod.POST)
    public List<Map<String,Object>> findFavGoodsList(@RequestParam("userId") String userId, @RequestParam("num") String num){
        //获取长度
        int listLength=Integer.parseInt(num);
        List<String> goodsInitList=favoritesService.findGoodsListByUserID(userId,listLength);
        return goodsService.goodsBriefInfoByString(goodsInitList);
    }

    /**
     * 搜索用户收藏的店铺信息
     * @param userId 用户id
     * @param num 已获取的数量
     * @return 店铺信息列表
     */
    @ResponseBody
    @RequestMapping(value = "/queryFavBrands",method = RequestMethod.POST)
    public List<Brand> findFavBrandsList(@RequestParam("userId") String userId, @RequestParam("num") String num){
        //获取长度
        int listLength=Integer.parseInt(num);
        return favoritesService.findBrandListByUserID(userId,listLength);
    }

}
