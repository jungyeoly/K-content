package com.example.myapp.cms.content.service;

import com.example.myapp.cms.content.model.Content;

import java.util.List;

public interface IContentService {
    List<Content> getAllContent();
    Content getAContent(int id);
}

