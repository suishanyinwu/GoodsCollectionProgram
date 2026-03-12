package com.it.service.impl;

import com.it.entity.Brand;
import com.it.mapper.BrandMapper;
import com.it.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class BrandServiceImpl implements BrandService {

    @Autowired
    private BrandMapper brandMapper;

    @Override
    public List<Brand> findAllBrand() {
        return brandMapper.findAllBrand();
    }
}
