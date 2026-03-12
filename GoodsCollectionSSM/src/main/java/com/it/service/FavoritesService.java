package com.it.service;

import com.it.entity.Brand;
import com.it.entity.BrandCollList;
import com.it.entity.GoodsCollList;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FavoritesService {

    /**
     * 根据userID寻找用户收藏的店铺id列表
     * @param userId 用户ID
     * @param listLength 已获取的长度
     * @return 店铺id列表
     */
    List<Brand> findBrandListByUserID(@Param("userId") String userId, @Param("listLength") int listLength);

    /**
     * 根据userID寻找用户收藏的周边id列表
     * @param userId 用户ID
     * @param listLength 已获取的长度
     * @return 周边id列表
     */
    List<String> findGoodsListByUserID(String userId,int listLength);

    /**
     * 搜索收藏内容
     * @param goodsId 周边id
     * @param userId 用户id
     * @return
     */
    GoodsCollList findFavInfo(String goodsId, String userId);

    /**
     * 增加收藏内容
     * @param goodsId 周边id
     * @param userId 用户id
     */
    void addFavInfo(String goodsId,String userId);

    /**
     * 删除收藏内容
     * @param goodsCollList 收藏信息
     */
    void deleteFavInfo(GoodsCollList goodsCollList);

    /**
     * 搜索收藏内容
     * @param brandId 店铺id
     * @param userId 用户id
     * @return
     */
    BrandCollList findFavBrandInfo(String brandId, String userId);

    /**
     * 增加收藏内容
     * @param brandId 店铺id
     * @param userId 用户id
     */
    void addFavBrandInfo(String brandId,String userId);


    /**
     * 删除收藏内容
     * @param brandCollList 店铺信息
     */
    void deleteFavBrandInfo(BrandCollList brandCollList);
}
