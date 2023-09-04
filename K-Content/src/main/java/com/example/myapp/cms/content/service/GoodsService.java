package com.example.myapp.cms.content.service;

import com.example.myapp.cms.content.dao.IGoodsRepository;
import com.example.myapp.cms.content.model.Goods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodsService implements IGoodsService{
    @Autowired
    IGoodsRepository goodsRepository;

    @Override
    public Goods getAGoods(int goodsId) {
        return  goodsRepository.getAGoods(goodsId);
    }

    @Override
    public Goods getAGoodsJFile(int goodsId) {
        return  goodsRepository.getAGoodsJFile(goodsId);
    }
    @Override
    public Goods getGoodsJFileByGoodsId(int goodsId) {
        return  goodsRepository.getGoodsJFileByGoodsId(goodsId);
    }

}
