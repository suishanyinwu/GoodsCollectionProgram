package com.it.service;

import com.it.entity.Persona;
import com.it.entity.ProductAssociations;

import java.util.List;
import java.util.Map;

public interface PersonaService {
    //根据ipId搜索角色列表
    List<Persona> findByIpId(String ipId);

    //根据ipId搜索角色列表 只展示id和name
    List<Map<String,String>> findBriefByIpId(String ipId);

    //根据goodsId搜索角色列表
    List<String> findByGoodsId(String goodsId);
}
