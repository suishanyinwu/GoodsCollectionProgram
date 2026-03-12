package com.it.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.it.entity.*;
import com.it.service.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Controller
@RequestMapping("/publish")
public class PublishController {
    @Autowired
    PublishService publishService;
    @Autowired
    GoodsService goodsService;
    @Autowired
    BrandService brandService;
    @Autowired
    IpService ipService;
    @Autowired
    PersonaService personaService;
    @Autowired
    NoticeService noticeService;

    /**
     * 请求发布周边列表信息
     * @param userId
     * @param power
     * @return
     */
    @RequestMapping("/queryPublishGoodsList")
    @ResponseBody
    public List<Map<String,Object>> findPublishGoodsList(@RequestParam("userId") String userId, @RequestParam("power")String power,@RequestParam("num") String num,@RequestParam("pageSize") String pageSize){
        //封装返回信息
        List<Map<String,Object>> goodsList=new ArrayList<Map<String,Object>>();

        //获取长度
        int length=Integer.parseInt(num);
        int size=Integer.parseInt(pageSize);

        List<String> goodsIdList=null;
        if("2"==power){  //商铺
            goodsIdList = publishService.findById(userId,length,size);
        }else {   //管理员
            goodsIdList = publishService.findAll(length,size);
        }

        //封装周边信息
        for (String s : goodsIdList) {
            //查询周边信息
            Map<String, Object> goodInfo = goodsService.findGoodsInfo(s);

            //周边图片
            List<String> imgUrl=goodsService.findImgUrlById(s);
            if(imgUrl!=null && imgUrl.size()>0){
                goodInfo.put("goodsImgUrl",imgUrl.get(0));
            }else {
                goodInfo.put("goodsImgUrl","");
            }
            goodsList.add(goodInfo);
        }

        return goodsList;
    }

    /**
     * 修改或发布周边信息
     * @param formInfo 表单信息
     * @return
     */
    @ResponseBody
    @RequestMapping("/storedGoodsInfo")
    public void publishGoods(@RequestBody String formInfo){
        //解析传输的字符串
        JSONObject jsonObject = JSON.parseObject(formInfo);

        //解析isAdd true为新增发布 false为编辑修改
        Boolean isAdd = (Boolean) jsonObject.get("isAdd");
        //解析周边图片
        List<Map<String,String>> imgUrlList = (List<Map<String, String>>) jsonObject.get("imgUrlList");

        //解析周边信息 并封装成goods类
        String goodsInfo = jsonObject.get("goodsInfo").toString();
        Goods goods = JSON.parseObject(goodsInfo, Goods.class);

        //解析关联角色信息
        List<String> personaList = (List<String>) jsonObject.get("personaList");

        //解析userId
        String userId = (String) jsonObject.get("userId");

        if(isAdd){   //新增发布
            //随机生成goodsID 新建周边
            String randomID=null;
            do {
                randomID= RandomStringUtils.random(14,false,true);
                randomID="goods-"+randomID;
            }while (null!=goodsService.findByGoodsId(randomID));
            goods.setGoodsId(randomID);

            //新增周边
            publishService.addNewGoodsInfo(goods,personaList,imgUrlList,userId);
        }else {//编辑修改
            publishService.updateGoodsInfo(goods,personaList,imgUrlList);
        }

    }

    /**
     * 删除发布的周边信息
     * @param goodsId
     */
    @ResponseBody
    @RequestMapping("/delGoodsInfo")
    public void delGoodsInfo(@RequestParam("goodsId") String goodsId){
        publishService.delGoodsInfo(goodsId);
    }

    /**
     * 根据ip返回对应的角色列表
     * @return
     */
    @ResponseBody
    @RequestMapping("/queryPersonaList")
     public List<Map<String,String>> findIpPersonaList(String ipId){
        return personaService.findBriefByIpId(ipId);
     }

    /**
     * 根据goodsId返回对应的角色列表
     * @param goodsId
     * @return
     */
     @ResponseBody
     @RequestMapping("/queryGoodsPersona")
     public List<String> findPersonaByGoods(String goodsId){
        return personaService.findByGoodsId(goodsId);
     }

    /**
     * 获取所有的图片
     * @return
     */
    @ResponseBody
    @RequestMapping("/queryGoodsImg")
    public List<String> findAllImg(String goodsId){
        return goodsService.findImgUrlById(goodsId);
    }

    /**
     * 获取所有brand列表
     * @return
     */
    @ResponseBody
    @RequestMapping("/queryAllBrand")
    public List<Brand> findAllBrand(){
        return brandService.findAllBrand();
    }

    /**
     * 获取所有IP列表
     * @return
     */
    @ResponseBody
    @RequestMapping("/queryAllIP")
    public List<Ip> findAllIP(){
        return ipService.findAll();
    }


}
