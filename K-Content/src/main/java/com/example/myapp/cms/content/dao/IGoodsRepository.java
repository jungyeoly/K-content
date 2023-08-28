package com.example.myapp.cms.content.dao;

import com.example.myapp.cms.content.model.Goods;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface IGoodsRepository {
 List<Goods> getAllGoods();
}
