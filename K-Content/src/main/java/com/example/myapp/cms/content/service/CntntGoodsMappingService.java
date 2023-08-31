package com.example.myapp.cms.content.service;

import com.example.myapp.cms.content.dao.ICntntGoodsMappingRepository;
import com.example.myapp.cms.content.model.CntntGoodsMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CntntGoodsMappingService implements ICntntGoodsMappingService{

    @Autowired
    ICntntGoodsMappingRepository cntntGoodsMappingRepository;

    @Override
    public List<CntntGoodsMapping> getAllGoodsByContent(int cntntId) {
        return cntntGoodsMappingRepository.getAllGoodsByContent(cntntId);
    }
}
