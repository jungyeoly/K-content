package com.example.myapp.service;

import com.example.myapp.dao.IGoodsRepository;
import com.example.myapp.model.Goods;
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
