package com.example.myapp.cms.goods.dao;

import com.example.myapp.cms.goods.model.Goods;
import com.example.myapp.cms.goods.model.GoodsFile;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface IGoodsRepository {
    List<Goods> getAllGoods();

    Goods getAGoods(@Param("goodsId") int goodsId);

    List<Goods> getAllGoodsJFile();

    Goods getGoodsJFileByGoodsId(@Param("goodsId") int goodsId);

    Goods getAGoodsJFile(@Param("goodsId") int goodsId);

    List<Goods> getSearchGoodsJFile(@Param("search") String search);

    int insertGoods(Goods goods);

    int insertGoodsFile(@Param("goodsFile") GoodsFile goodsFile);

    void updateDelYnGoods(@Param("goodsId") int goodsId);
}
