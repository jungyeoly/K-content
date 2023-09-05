package com.example.myapp.user.content.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.example.myapp.user.content.model.Content;

@Repository
@Mapper
public interface IContentUserRepository {
	List<Content> selectUserContent();
	List<Content> selectUserCateContent(String commonCodeVal);
}
