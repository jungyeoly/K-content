package com.example.myapp.cms.content.service;

import com.example.myapp.cms.goods.dao.IGoodsRepository;
import com.example.myapp.cms.goods.model.Goods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CSService implements ICSService{

    @Autowired
    IGoodsRepository goodsRepository;
    @Override
    public List<Goods> getAllGoods() {

        return goodsRepository.getAllGoods();
    }
}
