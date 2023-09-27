package com.example.myapp.user.bkmk.service;

import java.util.List;

import com.example.myapp.user.bkmk.model.CntntBkmk;
import com.example.myapp.user.bkmk.model.GoodsJFileJBklkList;

public interface IBkmkService {
	List<CntntBkmk> selectCntntBkmkList(String mberId);
	List<GoodsJFileJBklkList> selectGoodsBkmkList(String mberId);
	void insertCntntBkmk(String mberId, int cntntId);
	void deleteCntntBkmk(String mberId, int cntntId);
	int selectCntntBkmk(String mberId, int cntntId);
	void insertGoodsBkmk(String mberId, int goodsId);
	void deleteGoodstBkmk(String mberId, int goodsId);

	GoodsJFileJBklkList selectGoodsJBkmk (String mberId, int goodsId);


}
