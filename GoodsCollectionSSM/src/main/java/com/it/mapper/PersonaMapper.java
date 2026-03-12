package com.it.mapper;

import com.it.entity.Persona;
import com.it.entity.ProductAssociations;

import java.util.List;
import java.util.Map;

public interface PersonaMapper {
    //根据ipId搜索角色列表
    List<Persona> findByIpId(String ipId);

    //根据ipId搜索角色列表 只展示id和name
    List<Map<String,String>> findBriefByIpId(String ipId);

    //清除周边关联的角色的信息
    void delAllByGoodsId(String goodsId);

    //根据goodsId搜索角色列表
    List<String> findByGoodsId(String goodsId);

    //新增周边与角色的关联信息
    void addGoodsPersonaList(ProductAssociations productAssociations);
}
