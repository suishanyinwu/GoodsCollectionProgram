package com.it.mapper;

import com.it.entity.Brand;

import java.util.List;

public interface BrandMapper {
    //根据brandId搜索brand信息
    Brand findByBrandId(String brandId);

    //获取所有的brand信息
    List<Brand> findAllBrand();
}
