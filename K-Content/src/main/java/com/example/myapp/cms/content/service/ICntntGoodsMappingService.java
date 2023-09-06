package com.example.myapp.cms.content.service;

import com.example.myapp.cms.content.model.CntntGoodsMapping;
import java.util.List;

public interface ICntntGoodsMappingService {
    List<CntntGoodsMapping> getAllGoodsByContent(int cntntId);
}
