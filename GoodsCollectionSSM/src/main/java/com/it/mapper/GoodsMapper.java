package com.it.mapper;

import com.it.entity.Goods;
import com.it.entity.GoodsImg;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface GoodsMapper {
    //新增一个
    void addNewGoods(Goods newGoods);

    //删除一个
    void deleteById(String goodsId);

    //修改内容
    void updateGoods(Goods goods);

    //查询所有内容
    List<Goods> findAll();

    //根据goodsId查询简略信息 不包括ip名和店铺名
    Goods findByGoodsId(String goodsId);

    //根据goodsId查询详细信息 包括ip名和店铺名
    Map<String,Object> findGoodsInfo(String goodsId);

    //查找所有周边的详细信息
    List<Map<String,Object>> findAllGoodsInfo();

    //根据ipId查询
    Goods findByIpId(String ipId);

    //根据personaId查询
    List<Goods> findByPersonaId(String personaId);

    //根据brandId查询
    List<Goods> findByBrandId(String brandId);

    //根据userId查询用户发布的goods信息
    List<Goods> findUserPublish(String userId);

    //根据ipid和时间搜索周边列表
    List<Goods> findByIPTime(@Param("ipId") String ipId,@Param("dateTime") String dateTime);

    //搜索功能：
    //根据ip名字 或 系列名 或 角色名字 或 店铺名字 进行模糊查询
    List<Goods> searchByText(String text);

    //根据goodsId搜索imgUrl
    List<String> findImgUrlById(String goodsId);

    //新增imgUrl
    void addImgUrl(GoodsImg goodsImg);

    //删除周边的所有url
    void delImgUrlList(String goodsId);

}
