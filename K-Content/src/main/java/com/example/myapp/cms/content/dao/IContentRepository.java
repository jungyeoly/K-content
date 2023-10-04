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
    List<CmsContent> getAllContent(@Param("commonCodeVal") String commonCodeVal, @Param("start") int start, @Param("end") int end);

    CmsContent getAContent(@Param("cntntId") int cntntId);

    List<CntntGoodsMapping> getGoodsByContent(@Param("cntntId") int cntntId);

    int insertAContent(CmsContent content);

    //TODO 이거 수정
    int updateAContent(CmsContent contentForm);

    List<CmsContent> getContentByKeyword(@Param("keywordList") List<String> keywordList);

    void updateDelStat(@Param("cntntId") int cntntId);
    
    int totalContent(@Param("commonCodeVal") String commonCodeVal);
}
