package com.example.myapp.cms.content.service;

import com.example.myapp.cms.content.dao.IContentRepository;
import com.example.myapp.cms.content.model.CmsContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContentService implements IContentService {

    @Autowired
    IContentRepository contentRepository;

    @Override
    public List<CmsContent> getAllContent() {
        return contentRepository.getAllContent();
    }

    @Override
    public CmsContent getAContent(int id) {
        return contentRepository.getAContent(id);
    }



//    @Scheduled(cron="0/20 * * * * ?")
//    public void crol(){

//    }



}
