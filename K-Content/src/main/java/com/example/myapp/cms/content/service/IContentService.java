package com.example.myapp.cms.content.service;

import com.example.myapp.cms.content.model.CmsContent;
import java.util.List;

public interface IContentService {
    List<CmsContent> getAllContent(int page);

    CmsContent getAContent(int id);

    boolean insertAContent(CmsContent content, List<Integer> goodsList);

    boolean updateAContent(CmsContent content, List<Integer> goodsList);

    List<CmsContent> getContentByKeyword(List<String> keywordList);

    void updateDelStat(int cntntId);
    
    int totalCntnt();
}

