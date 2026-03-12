package com.it.service;

import com.it.entity.Goods;
import com.it.entity.Publish;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface PublishService {

    /**
     * 删除周边的所有信息
     * @param goodsId
     */
    void delGoodsInfo(String goodsId);


    /**
     * 发布新的周边
     * @param goods
     * @param personaList
     * @param imgUrlList
     * @param userId
     */
    void addNewGoodsInfo(Goods goods, List<String> personaList, List<Map<String,String>> imgUrlList,String userId);

    /**
     * 修改周边信息
     * @param goods
     * @param personaList
     * @param imgUrlList
     */
    void updateGoodsInfo(Goods goods, List<String> personaList, List<Map<String,String>> imgUrlList);

    /**
     * 管理者获取发布周边的信息
     * @param length 已获取的长度
     * @param pageSize
     * @return
     */
    List<String> findAll(int length, int pageSize);

    /**
     * 某个店铺获取发布周边的信息
     * @param userId 用户id
     * @param length 已获取的长度
     * @param pageSize
     * @return
     */
    List<String> findById(String userId,int length,int pageSize);
}
