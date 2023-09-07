package com.example.myapp.cms.content.dao;

import com.example.myapp.cms.content.model.CntntGoodsMapping;
import com.example.myapp.cms.content.model.CmsContent;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface IContentRepository {
    List<CmsContent> getAllContent();

    CmsContent getAContent(@Param("cntntId") int cntntId);

    List<CntntGoodsMapping> getGoodsByContent(@Param("cntntId") int cntntId);

    int insertAContent(CmsContent content);

}
