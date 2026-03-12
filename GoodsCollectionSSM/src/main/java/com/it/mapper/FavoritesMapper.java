package com.it.mapper;

import com.it.entity.Brand;
import com.it.entity.BrandCollList;
import com.it.entity.GoodsCollList;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FavoritesMapper {
    /**
     * 根据userID寻找用户收藏的店铺id列表
     * @param userId 用户ID
     * @return 店铺id列表
     */
    List<Brand> findBrandListByUserID(@Param("userId") String userId, @Param("listLength") int listLength);

    /**
     * 根据userID寻找用户收藏的周边id列表
     * @param userId 用户ID
     * @return 周边id列表
     */
    List<String> findGoodsListByUserID(@Param("userId") String userId, @Param("listLength") int listLength);

    /**
     * 搜索收藏内容
     * @param goodsId 周边id
     * @param userId 用户id
     * @return
     */
    GoodsCollList findFavInfo(@Param("goodsId")String goodsId,@Param("userId")String userId);

    /**
     * 搜索用户所有的收藏内容
     * @param userId 用户id
     * @return
     */
    List<GoodsCollList> findFavInfoByUserId(String userId);

    /**
     * 增加收藏内容
     * @param goodsId 周边id
     * @param userId 用户id
     */
    void addFavInfo(@Param("goodsId")String goodsId,@Param("userId")String userId);


    /**
     * 删除收藏内容
     * @param goodsCollList 收藏信息
     */
    void deleteFavInfo(GoodsCollList goodsCollList);

    /**
     * 删除所有人对某个周边的收藏信息
     * @param goodsId
     */
    void delFavGoodsByID(String goodsId);


    /**
     * 搜索收藏内容
     * @param brandId 店铺id
     * @param userId 用户id
     * @return
     */
    BrandCollList findFavBrandInfo(@Param("brandId")String brandId,@Param("userId")String userId);

    /**
     * 增加收藏内容
     * @param brandId 店铺id
     * @param userId 用户id
     */
    void addFavBrandInfo(@Param("brandId")String brandId,@Param("userId")String userId);


    /**
     * 删除收藏内容
     * @param brandCollList 店铺信息
     */
    void deleteFavBrandInfo(BrandCollList brandCollList);
}
