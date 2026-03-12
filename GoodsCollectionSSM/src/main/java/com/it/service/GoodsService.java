package com.it.service;

import com.it.entity.Goods;
import com.it.entity.GoodsImg;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface GoodsService {

    /**
     * 封装周边简略信息 基础信息仅包含周边名 周边id 周边价格+周边图片列表
     * @param goodsInitList
     * @return
     */
    List<Map<String,Object>> goodsBriefInfo(List<Goods> goodsInitList);
    /**
     * 封装周边简略信息 基础信息仅包含周边名 周边id 周边价格+周边图片列表
     * @param goodsInitList
     * @return
     */
    List<Map<String,Object>> goodsBriefInfoByString(List<String> goodsInitList);

    /**
     * 获取所有周边信息
     * @return 返回值为Goods类
     */
    List<Goods> findAll();

    /**
     * 根据周边Id查询周边信息
     * @param goodsId 周边id
     * @return Goods类 周边简略信息
     */
    Goods findByGoodsId(String goodsId);

    /**
     * 根据周边Id查询周边信息
     * @param goodsId 周边id
     * @return Goods类 周边详细信息
     */
    Map<String,Object> findGoodsInfo(String goodsId);

    /**
     * 根据角色Id查询周边信息
     * @param personaId 角色id
     * @return Goods类 周边信息
     */
    List<Goods> findByPersonaId(String personaId);

    /**
     * 根据店铺Id查询周边信息
     * @param brandId 店铺id
     * @return Goods类 周边信息
     */
    List<Goods> findByBrandId(String brandId);

    /**
     * 根据ipid和时间搜索周边列表
     * @param ipId ipid
     * @param dateTime 发布时间
     * @return 周边列表
     */
    List<Goods> findByIPTime(String ipId, String dateTime);

    /**
     * 搜索功能：
     *  根据ip名字 或 系列名 或 角色名字 进行模糊查询
     * @param text
     * @return
     */
    List<Goods> searchByText(String text);

    /**
     * 根据goodsId搜索imgUrl
     * @param goodsId 周边id
     * @return 图像url列表
     */
    List<String> findImgUrlById(String goodsId);

}

