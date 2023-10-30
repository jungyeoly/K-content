package com.example.myapp.cms.goods.service;

import com.example.myapp.cms.content.dao.ICntntGoodsMappingRepository;
import com.example.myapp.cms.goods.dao.IGoodsRepository;
import com.example.myapp.cms.goods.model.Goods;
import com.example.myapp.cms.goods.model.GoodsFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GoodsService implements IGoodsService {
    @Autowired
    IGoodsRepository goodsRepository;
    @Autowired
    ICntntGoodsMappingRepository mappingRepository;
    @Override
    public Goods getAGoods(int goodsId) {
        return goodsRepository.getAGoods(goodsId);
    }

    @Override
    public Goods getAGoodsJFile(int goodsId) {
        return goodsRepository.getAGoodsJFile(goodsId);
    }

    @Override
    public Goods getGoodsJFileByGoodsId(int goodsId) {
        return goodsRepository.getGoodsJFileByGoodsId(goodsId);
    }

    @Override
    public List<Goods> getAllGoodsJFile(int page) {
        int start = (page-1)*9 + 1;
        return goodsRepository.getAllGoodsJFile(start, start+8);
    }
    @Override
    public int totalGoods() {
        return goodsRepository.totalGoods();
    }
    @Override
    public List<Goods> getSearchGoodsJFile(String search, int page) {
        int start = (page-1)*9 + 1;
        return goodsRepository.getSearchGoodsJFile(search, start, start+8);
    }

    @Override
    @Transactional
    public int insertGoods(Goods goods, GoodsFile goodsFile) {

        int rowsAffected =  goodsRepository.insertGoods(goods);
        int goodsId = goods.getGoodsId();
        goodsFile.setGoodsFileGoodsId(goodsId);
        goodsRepository.insertGoodsFile(goodsFile);

        return rowsAffected;
    }

    @Override
    @Transactional
    public void updateDelYnGoods(int goodsId) {
        goodsRepository.updateDelYnGoods(goodsId);
        mappingRepository.delMappingByGoodsId(goodsId);
    }

//    public void deleteGoodsFile(int goodsId) {
//
//    }
    @Override
    @Transactional
    public int updateGoods(Goods goods, GoodsFile goodsFile) {
        goodsRepository.deleteGoodsFile(goods.getGoodsId());
        int rowsAffected = goodsRepository.updateGoods(goods);
        int goodsId = goods.getGoodsId();
        goodsFile.setGoodsFileGoodsId(goodsId);
         goodsRepository.insertGoodsFile(goodsFile);

        return rowsAffected;
    }

    @Override
    public void updateGoods(Goods goods) {
        goodsRepository.updateGoods(goods);
    }

    @Override
    public int getSearchGoodsJFileCount(String search) {
        return goodsRepository.getSearchGoodsJFileCount(search);
    }
}
