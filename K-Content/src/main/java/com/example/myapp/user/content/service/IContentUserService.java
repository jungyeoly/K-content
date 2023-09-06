package com.example.myapp.user.content.service;

import java.util.List;

import com.example.myapp.user.content.model.Content;


public interface IContentUserService {
	List<Content> selectUserContent(String commonCodeVal);
}
