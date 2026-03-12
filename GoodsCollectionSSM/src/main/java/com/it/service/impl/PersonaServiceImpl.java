package com.it.service.impl;

import com.it.entity.Persona;
import com.it.entity.ProductAssociations;
import com.it.mapper.PersonaMapper;
import com.it.service.PersonaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class PersonaServiceImpl implements PersonaService {
    @Autowired
    PersonaMapper personaMapper;

    @Override
    public List<Persona> findByIpId(String ipId) {
        return personaMapper.findByIpId(ipId);
    }

    @Override
    public List<Map<String, String>> findBriefByIpId(String ipId) {
        return personaMapper.findBriefByIpId(ipId);
    }

    @Override
    public List<String> findByGoodsId(String goodsId) {
        return personaMapper.findByGoodsId(goodsId);
    }

}
