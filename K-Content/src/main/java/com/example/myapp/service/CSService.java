package com.example.myapp.service;

import com.example.myapp.model.Goods;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CSService implements ICSService{

    @Override
    public List<Goods> getAllGoods() {
        return null;
    }
}
