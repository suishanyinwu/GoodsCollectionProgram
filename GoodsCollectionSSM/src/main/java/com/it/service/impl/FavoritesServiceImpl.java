package com.it.service.impl;

import com.it.entity.Brand;
import com.it.entity.BrandCollList;
import com.it.entity.GoodsCollList;
import com.it.mapper.FavoritesMapper;
import com.it.service.FavoritesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class FavoritesServiceImpl implements FavoritesService {
    @Autowired
    FavoritesMapper favoritesMapper;


    @Override
    public List<Brand> findBrandListByUserID(String userId, int listLength) {
        return favoritesMapper.findBrandListByUserID(userId,listLength);
    }

    @Override
    public List<String> findGoodsListByUserID(String userId,int listLength) {
        return favoritesMapper.findGoodsListByUserID(userId,listLength);
    }

    @Override
    public GoodsCollList findFavInfo(String goodsId, String userId) {
        return favoritesMapper.findFavInfo(goodsId,userId);
    }

    @Override
    public void addFavInfo(String goodsId, String userId) {
        favoritesMapper.addFavInfo(goodsId,userId);
    }

    @Override
    public void deleteFavInfo(GoodsCollList goodsCollList) {
        favoritesMapper.deleteFavInfo(goodsCollList);
    }

    @Override
    public BrandCollList findFavBrandInfo(String brandId, String userId) {
        return favoritesMapper.findFavBrandInfo(brandId, userId);
    }

    @Override
    public void addFavBrandInfo(String brandId, String userId) {
        favoritesMapper.addFavBrandInfo(brandId, userId);
    }

    @Override
    public void deleteFavBrandInfo(BrandCollList brandCollList) {
        favoritesMapper.deleteFavBrandInfo(brandCollList);
    }
}
