package com.example.myapp.cms.content.service;

import com.example.myapp.cms.content.model.CmsContent;
import com.example.myapp.cms.content.model.CntntInsertForm;

import java.util.List;

public interface IContentService {
    List<CmsContent> getAllContent();
    CmsContent getAContent(int id);

    boolean insertAContent(CmsContent content,List<Integer> goodsList );
}

