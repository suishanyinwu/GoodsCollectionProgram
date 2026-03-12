package com.it.controller;

import com.it.entity.BrandCollList;
import com.it.entity.GoodsCollList;
import com.it.mapper.FavoritesMapper;
import com.it.mapper.GoodsMapper;
import com.it.service.FavoritesService;
import com.it.service.GoodsService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/goods")
public class GoodsController {
    @Autowired
    GoodsService goodsService;
    @Autowired
    FavoritesService favoritesService;

    /**
     * 根据goodsId搜索周边具体信息（不含图片）
     * @param goodsId 周边id
     * @return 周边具体信息内容
     */
    @RequestMapping("/queryGoodsInfo")
    @ResponseBody
    public Map<String,Object> findGoodsInfo(String goodsId){
        return goodsService.findGoodsInfo(goodsId);
    }


    /**
     * 根据goodsId搜索周边图片
     * @param goodsId 周边id
     * @return 周边图片列表
     */
    @RequestMapping("/queryGoodsImg")
    @ResponseBody
    public List<String> findGoodsImgUrl(String goodsId){
        return goodsService.findImgUrlById(goodsId);
    }

    /**
     * 查询用户对某周边的收藏信息
     * @param goodsId 周边id
     * @param userId 用户id
     * @return 收藏信息
     */
    @RequestMapping("/queryFavInfo")
    @ResponseBody
    public String findFavInfo(String goodsId,String userId){
        GoodsCollList favInfo = favoritesService.findFavInfo(goodsId, userId);
        if(favInfo!=null){
            return "ture";
        }else {
            return "false";
        }
    }

    /**
     * 用户对某周边进行收藏
     * @param goodsId 周边id
     * @param userId 用户id
     */
    @RequestMapping("/addFavInfo")
    @ResponseBody
    public void addFavInfo(String goodsId, String userId){
        favoritesService.addFavInfo(goodsId,userId);
    }

    /**
     * 用户取消收藏某周边
     * @param goodsCollList 周边和用户的信息
     */
    @RequestMapping("/deleteFavInfo")
    @ResponseBody
    public void deleteFavInfo(GoodsCollList goodsCollList){
        favoritesService.deleteFavInfo(goodsCollList);
    }

    /**
     * 查询用户对某店铺的收藏信息
     * @param brandId 店铺id
     * @param userId 用户id
     * @return 收藏信息
     */
    @RequestMapping("/queryFavBrandInfo")
    @ResponseBody
    public String findFavBrandInfo(String brandId,String userId){
        BrandCollList brandInfo = favoritesService.findFavBrandInfo(brandId, userId);
        if(brandInfo!=null){
            return "ture";
        }else {
            return "false";
        }
    }

    /**
     * 用户对某店铺进行收藏
     * @param brandId 店铺id
     * @param userId 用户id
     */
    @RequestMapping("/addFavBrandInfo")
    @ResponseBody
    public void addFavBrandInfo(String brandId, String userId){
        favoritesService.addFavBrandInfo(brandId,userId);
    }

    /**
     * 用户取消收藏某店铺
     * @param brandCollList 需要删除的店铺信息和用户信息
     */
    @RequestMapping("/deleteFavBrandInfo")
    @ResponseBody
    public void deleteFavBrandInfo(BrandCollList brandCollList){
        favoritesService.deleteFavBrandInfo(brandCollList);
    }


}
