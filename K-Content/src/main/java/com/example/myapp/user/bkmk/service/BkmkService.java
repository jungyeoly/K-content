package com.example.myapp.user.bkmk.service;

import java.util.List;

import com.example.myapp.user.bkmk.model.GoodsJFileJBklkList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.myapp.user.bkmk.dao.IBkmkRepository;
import com.example.myapp.user.bkmk.model.CntntBkmk;

@Service
public class BkmkService implements IBkmkService {

    @Autowired
    IBkmkRepository bkmkRepository;

    @Override
    public List<CntntBkmk> selectCntntBkmkList(String mberId) {
        return bkmkRepository.selectCntntBkmkList(mberId);
    }
    
	@Override
	public List<GoodsJFileJBklkList> selectGoodsBkmkList(String mberId) {
		return bkmkRepository.selectGoodsBkmkList(mberId);
	}


    @Override
    public void insertCntntBkmk(String mberId, int cntntId) {
        bkmkRepository.insertCntntBkmk(mberId, cntntId);
    }

    @Override
    public void deleteCntntBkmk(String mberId, int cntntId) {
        bkmkRepository.deleteCntntBkmk(mberId, cntntId);
    }

    @Override
    public int selectCntntBkmk(String mberId, int cntntId) {
        return bkmkRepository.selectCntntBkmk(mberId, cntntId);
    }

    @Override
    public void insertGoodsBkmk(String mberId, int goodsId) {
        bkmkRepository.insertGoodsBkmk(mberId, goodsId);
    }

    @Override
    public void deleteGoodstBkmk(String mberId, int goodsId) {
        bkmkRepository.deleteGoodstBkmk(mberId, goodsId);
    }

    @Override
    public GoodsJFileJBklkList selectGoodsJBkmk(String mberId, int goodsId) {
       return bkmkRepository.selectGoodsJBkmk(mberId, goodsId);
    }

}
