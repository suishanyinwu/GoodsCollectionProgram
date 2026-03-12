package com.it.mapper;

import com.it.entity.Publish;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PublishMapper {

    /**
     * 管理者获取发布周边的信息
     * @param length 已获取的长度
     * @param pageSize
     * @return
     */
    List<String> findAll(@Param("length") int length, @Param("pageSize") int pageSize);

    /**
     * 某个店铺获取发布周边的信息
     * @param userId 用户id
     * @param length 已获取的长度
     * @param pageSize
     * @return
     */
    List<String> findById(@Param("userId") String userId,@Param("length") int length, @Param("pageSize") int pageSize);

    void deleteById(Publish publish);

    /**
     * 删除周边 根据周边id
     * @param goodsId
     */
    void delByGoodsId(String goodsId);

    void addNew(Publish publish);
}
