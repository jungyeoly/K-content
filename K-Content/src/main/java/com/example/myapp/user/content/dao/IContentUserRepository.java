package com.example.myapp.user.content.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.example.myapp.user.content.model.Content;

@Repository
@Mapper
public interface IContentUserRepository {
	//전체조회(카테고리)
	List<Content> selectUserContent(String commonCodeVal);
	
	// 검색
	List<Content> searchUserContent(String keyword);
}
