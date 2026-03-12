package com.it.service.impl;

import com.it.entity.Goods;
import com.it.entity.GoodsImg;
import com.it.mapper.GoodsMapper;
import com.it.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
@Transactional
public class GoodsServiceImpl implements GoodsService {
    @Autowired
    GoodsMapper goodsMapper;

    //封装周边简略信息 基础信息仅包含周边名 周边id 周边价格+周边图片列表
    @Override
    public List<Map<String, Object>> goodsBriefInfo(List<Goods> goodsInitList) {
        //封装返回信息
        List<Map<String,Object>> goodsList=new ArrayList<Map<String,Object>>();

        for (Goods goods : goodsInitList) {
            Map<String,Object> goodInfo=new TreeMap<String,Object>();
            goodInfo.put("goodsId",goods.getGoodsId());
            goodInfo.put("goodsSeries",goods.getGoodsSeries());
            goodInfo.put("price",goods.getPrice());

            //周边图片
            List<String> imgUrl=goodsMapper.findImgUrlById(goods.getGoodsId());
            if(imgUrl!=null && imgUrl.size()>0){
                goodInfo.put("goodsImgUrl",imgUrl.get(0));
            }else {
                goodInfo.put("goodsImgUrl","");
            }
            goodsList.add(goodInfo);
        }

        return goodsList;
    }

    //封装周边简略信息 基础信息仅包含周边名 周边id 周边价格+周边图片列表
    @Override
    public List<Map<String, Object>> goodsBriefInfoByString(List<String> goodsInitList) {
        List<Map<String,Object>> goodsList=new ArrayList<Map<String,Object>>();
        for (String goodsId : goodsInitList) {
            Goods good = goodsMapper.findByGoodsId(goodsId);
            Map<String,Object> goodInfo=new TreeMap<String,Object>();
            goodInfo.put("goodsId",good.getGoodsId());
            goodInfo.put("goodsSeries",good.getGoodsSeries());
            goodInfo.put("price",good.getPrice());

            //周边图片
            List<String> imgUrl=goodsMapper.findImgUrlById(goodsId);
            if(imgUrl!=null && imgUrl.size()>0){
                goodInfo.put("goodsImgUrl",imgUrl.get(0));
            }else {
                goodInfo.put("goodsImgUrl","");
            }
            goodsList.add(goodInfo);
        }

        return goodsList;
    }

    @Override
    public List<Goods> findAll() {
        return goodsMapper.findAll();
    }


    @Override
    public Goods findByGoodsId(String goodsId) {
        return goodsMapper.findByGoodsId(goodsId);
    }

    @Override
    public Map<String, Object> findGoodsInfo(String goodsId) {
        return goodsMapper.findGoodsInfo(goodsId);
    }

    @Override
    public List<Goods> findByPersonaId(String personaId) {
        return goodsMapper.findByPersonaId(personaId);
    }

    @Override
    public List<Goods> findByBrandId(String brandId) {
        return goodsMapper.findByBrandId(brandId);
    }

    @Override
    public List<Goods> findByIPTime(String ipId, String dateTime) {
        return goodsMapper.findByIPTime(ipId,dateTime);
    }

    @Override
    public List<Goods> searchByText(String text) {
        return goodsMapper.searchByText(text);
    }

    @Override
    public List<String> findImgUrlById(String goodsId) {
        return goodsMapper.findImgUrlById(goodsId);
    }

}
