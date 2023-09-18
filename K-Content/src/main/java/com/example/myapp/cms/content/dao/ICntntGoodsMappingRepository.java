package com.example.myapp.cms.content.dao;

import com.example.myapp.cms.content.model.CntntGoodsMapping;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface ICntntGoodsMappingRepository {
    List<CntntGoodsMapping> getAllGoodsByContent(@Param("cntntId") int cntntId);

    int insertMappingDate(@Param("cntntId") int cntntId, @Param("goodsId") int goodsId);

    //TODO 이거 수정
    int delMappingDate(@Param("contentId") int contentId);
}
