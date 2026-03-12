package com.it.service.impl;

import com.it.entity.Goods;
import com.it.entity.GoodsCollList;
import com.it.mapper.FavoritesMapper;
import com.it.mapper.GoodsMapper;
import com.it.mapper.PersonaMapper;
import com.it.service.FavoritesService;
import com.it.service.GoodsService;
import com.it.service.PersonaService;
import com.it.service.RecService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class RecServiceImpl implements RecService {
    @Autowired
    GoodsMapper goodsMapper;
    @Autowired
    PersonaMapper personaMapper;
    @Autowired
    FavoritesMapper favoritesMapper;

    @Override
    public List<Goods> recommendGoods(String userId) {
        //封装返回值
        List<Goods> recommendGoodsList=new ArrayList<>();

        //用户偏好标签向量
        Map<String,Integer> lovingTag=new HashMap<String,Integer>();

        //查询用户收藏的周边内容 构建偏好向量
        for (GoodsCollList favGoods : favoritesMapper.findFavInfoByUserId(userId)) {
            String goodsId = favGoods.getGoodsId();
            //简略的周边信息
            Goods goodsInfo = goodsMapper.findByGoodsId(goodsId);

            //IP
            if(lovingTag.get(goodsInfo.getIpId())!=null){
                lovingTag.put(goodsInfo.getIpId(),lovingTag.get(goodsInfo.getIpId())+1);
            }else {
                lovingTag.put(goodsInfo.getIpId(),1);
            }
            //店铺
            if(lovingTag.get(goodsInfo.getBrandId())!=null){
                lovingTag.put(goodsInfo.getBrandId(),lovingTag.get(goodsInfo.getBrandId())+1);
            }else {
                lovingTag.put(goodsInfo.getBrandId(),1);
            }
            //制品种类
            if(lovingTag.get(goodsInfo.getType())!=null){
                lovingTag.put(goodsInfo.getType(),lovingTag.get(goodsInfo.getType())+1);
            }else {
                lovingTag.put(goodsInfo.getType(),1);
            }
            //角色
            for (String personaId : personaMapper.findByGoodsId(goodsId)) {
                if(lovingTag.get(personaId)!=null){
                    lovingTag.put(personaId,lovingTag.get(personaId)+1);
                }else {
                    lovingTag.put(personaId,1);
                }
            }


        }

        //构建所有物品的相似度列表
        Map<String,Double> similarityList=new HashMap<String,Double>();
        for (Goods goods : goodsMapper.findAll()) {
            //判断是否是已收藏的周边
            boolean flag=true;
            for (GoodsCollList favGoods : favoritesMapper.findFavInfoByUserId(userId)) {
                if(favGoods.getGoodsId().equals(goods.getGoodsId())){
                    flag=false;
                    break;
                }
            }
            if(!flag){ //是已收藏的周边 不计算
                continue;
            }

            //物品标签向量
            Map<String,Integer> goodsTag=new TreeMap<String,Integer>();
            //IP
            goodsTag.put(goods.getIpId(),1);
            //店铺
            goodsTag.put(goods.getBrandId(),1);
            //制品种类
            if(goods.getType()!=null){
                goodsTag.put(goods.getType(),1);
            }
            //角色
            for (String personaId : personaMapper.findByGoodsId(goods.getGoodsId())) {
                goodsTag.put(personaId,1);
            }

            //计算相似度
            Double num= Double.valueOf(0);  //答案
            Double molecule= Double.valueOf(0);  //分子
            Double denominator= Double.valueOf(0); //分母
            //构建分子 一半的分母
            for (Map.Entry<String,Integer> entry:goodsTag.entrySet()) {
                if(lovingTag.get(entry.getKey())!=null){
                    molecule=molecule+lovingTag.get(entry.getKey());
                    denominator=denominator+1;
                }
            }
            //分母开根号
            if(denominator!=0){
                denominator=Math.sqrt(denominator);
            }
            //构建另一半分母
            Double sum= Double.valueOf(0);
            for (Map.Entry<String,Integer> entry:lovingTag.entrySet()) {
                sum=sum+Math.pow(entry.getValue(),2);
            }
            //开根号
            if(sum!=0){
                sum=Math.sqrt(sum);
            }
            denominator=denominator*sum;
            if(denominator!=0){
                num=molecule/denominator;
            }
            //存储相似度
            similarityList.put(goods.getGoodsId(),num);
        }

        //根据相似度列表选最大的十个 并封装周边信息
        int length=0;  //相似度列表长度
        int j=10;
        for (String key:similarityList.keySet()) {
            length++; //计算长度
        }
        if(j>length){
            j=length;
        }
        for (int i = 0; i < j; i++) {
            if(similarityList==null){
                break;
            }
            String max=null;
            for (String key:similarityList.keySet()) {
                //寻找最大值
                if(max==null){
                    max=key;
                }else {
                    if(similarityList.get(key)>similarityList.get(max)){
                        max=key;
                    }
                }
            }

            //删除列表数据中的最大值
            Double remove = similarityList.remove(max);
            //封装周边信息
            recommendGoodsList.add(goodsMapper.findByGoodsId(max));
        }

        return recommendGoodsList;
    }
}
