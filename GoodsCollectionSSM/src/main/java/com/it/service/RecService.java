package com.it.service;

import com.it.entity.Goods;

import java.util.List;
import java.util.Map;

public interface RecService {

    /**
     * 完成基于内容的推荐功能
     * @return
     */
    List<Goods> recommendGoods(String userId);
}
