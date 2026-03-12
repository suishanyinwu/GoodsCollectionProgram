package com.it.service;

import com.it.entity.Brand;

import java.util.List;

public interface BrandService {

    //获取所有的brand信息
    List<Brand> findAllBrand();
}
