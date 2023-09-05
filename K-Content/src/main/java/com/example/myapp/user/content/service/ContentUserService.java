package com.example.myapp.user.content.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.myapp.user.content.dao.IContentUserRepository;
import com.example.myapp.user.content.model.Content;

@Service
public class ContentUserService implements IContentUserService {

	@Autowired
	IContentUserRepository contentRepository;
	
	@Override
	public List<Content> selectUserContent(String commonCodeVal) {
		return contentRepository.selectUserContent(commonCodeVal);
	}
}
