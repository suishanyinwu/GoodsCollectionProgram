package com.it.service.impl;

import com.it.entity.*;
import com.it.mapper.*;
import com.it.service.PersonaService;
import com.it.service.PublishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class PublishServiceImpl implements PublishService {
    @Autowired
    PublishMapper publishMapper;
    @Autowired
    GoodsMapper goodsMapper;
    @Autowired
    FavoritesMapper favoritesMapper;
    @Autowired
    PersonaMapper personaMapper;
    @Autowired
    NoticeListMapper noticeListMapper;
    @Autowired
    IpMapper ipMapper;
    @Autowired
    BrandMapper brandMapper;

    //发布新的周边
    @Override
    public void addNewGoodsInfo(Goods goods, List<String> personaList, List<Map<String, String>> imgUrlList,String userId) {
        //添加周边信息
        goodsMapper.addNewGoods(goods);

        //添加周边发布信息
        Publish publish = new Publish();
        publish.setGoodsId(goods.getGoodsId());
        publish.setUserId(userId);
        publishMapper.addNew(publish);

        //添加周边上新通知信息
        if(null==noticeListMapper.findByIpAndTime(goods.getIpId(),goods.getDateTime())){
            NoticeList noticeList = new NoticeList();
            noticeList.setIpId(goods.getIpId());
            noticeList.setDateTime(goods.getDateTime());
            String title="【周边上新】您关注的“"+ipMapper.findById(goods.getIpId())+"”已有新的周边上新";
            Brand brandInfo = brandMapper.findByBrandId(goods.getBrandId());
            String body="已有"+ipMapper.findById(goods.getIpId())+"的商品上新";
            noticeList.setNoticeTitle(title);
            noticeList.setNoticeContent(body);

            noticeListMapper.addNewNotice(noticeList);
        }

        //添加周边关联角色信息
        //清空原先的信息
        personaMapper.delAllByGoodsId(goods.getGoodsId());
        if(personaList!=null){
            //新增周边关联角色信息
            ProductAssociations productAssociations = new ProductAssociations();
            productAssociations.setGoodsId(goods.getGoodsId());
            for (int i = 0; i < personaList.size(); i++) {
                productAssociations.setPersonaId(personaList.get(i));
                personaMapper.addGoodsPersonaList(productAssociations);
            }
        }

        //添加周边图片信息
        if(goods.getGoodsId()!=null){
            //清空周边图片
            goodsMapper.delImgUrlList(goods.getGoodsId());

            GoodsImg goodsImg = new GoodsImg();
            goodsImg.setGoodsId(goods.getGoodsId());
            //添加周边图片
            if(imgUrlList!=null){  //传输的周边图片不为空 则新增
                for (int i = 0; i < imgUrlList.size(); i++) {
                    //获取图片路径
                    String url = imgUrlList.get(i).get("url");
                    goodsImg.setGoodsImgUrl(url);
                    //新增列表周边图片
                    goodsMapper.addImgUrl(goodsImg);
                }
            }

        }
    }

    //修改周边信息
    @Override
    public void updateGoodsInfo(Goods goods, List<String> personaList, List<Map<String, String>> imgUrlList) {
        //修改周边基础信息
        goodsMapper.updateGoods(goods);
        //添加周边关联角色信息
        //清空原先的信息
        personaMapper.delAllByGoodsId(goods.getGoodsId());
        if(personaList!=null){
            //新增周边关联角色信息
            ProductAssociations productAssociations = new ProductAssociations();
            productAssociations.setGoodsId(goods.getGoodsId());
            for (int i = 0; i < personaList.size(); i++) {
                productAssociations.setPersonaId(personaList.get(i));
                personaMapper.addGoodsPersonaList(productAssociations);
            }
        }

        //添加周边图片信息
        if(goods.getGoodsId()!=null){
            //清空周边图片
            goodsMapper.delImgUrlList(goods.getGoodsId());

            GoodsImg goodsImg = new GoodsImg();
            goodsImg.setGoodsId(goods.getGoodsId());
            //添加周边图片
            if(imgUrlList!=null){  //传输的周边图片不为空 则新增
                for (int i = 0; i < imgUrlList.size(); i++) {
                    //获取图片路径
                    String url = imgUrlList.get(i).get("url");
                    goodsImg.setGoodsImgUrl(url);
                    //新增列表周边图片
                    goodsMapper.addImgUrl(goodsImg);
                }
            }

        }
    }

    //删除周边的所有信息
    @Override
    public void delGoodsInfo(String goodsId) {
        //清空图片
        goodsMapper.delImgUrlList(goodsId);
        //清空收藏
        favoritesMapper.delFavGoodsByID(goodsId);
        //清空角色关联信息
        personaMapper.delAllByGoodsId(goodsId);
        //清空发布信息
        publishMapper.delByGoodsId(goodsId);
        //清空周边信息
        goodsMapper.deleteById(goodsId);
    }

    @Override
    public List<String> findAll(int length, int pageSize) {
        return publishMapper.findAll(length,pageSize);
    }

    @Override
    public List<String> findById(String userId, int length, int pageSize) {
        return publishMapper.findById(userId,length,pageSize);
    }
}
