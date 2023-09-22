package com.example.myapp.user.bkmk.dao;

import java.util.List;

import com.example.myapp.user.bkmk.model.GoodsJFileJBklkList;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.example.myapp.user.bkmk.model.CntntBkmk;

@Repository
@Mapper
public interface IBkmkRepository {
    List<CntntBkmk> selectCntntBkmkList(@Param("mberId") String mberId);

    void insertCntntBkmk(@Param("mberId") String mberId, @Param("cntntId") int cntntId);

    void deleteCntntBkmk(@Param("mberId") String mberId, @Param("cntntId") int cntntId);

    int selectCntntBkmk(@Param("mberId") String mberId, @Param("cntntId") int cntntId);

    void insertGoodsBkmk(@Param("mberId") String mberId, @Param("goodsId") int goodsId);
    void deleteGoodstBkmk(@Param("mberId") String mberId, @Param("goodsId") int goodsId);
    GoodsJFileJBklkList selectGoodsJBkmk(@Param("mberId") String mberId, @Param("goodsId") int goodsId);
}
