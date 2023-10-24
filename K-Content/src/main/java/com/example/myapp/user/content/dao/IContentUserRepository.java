package com.example.myapp.user.content.dao;

import java.util.List;

import com.example.myapp.user.content.model.Content;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface IContentUserRepository {
	//전체조회(카테고리)
	List<Content> selectUserContent(String commonCodeVal, @Param("start") int start, @Param("end") int end);

	// 검색
	List<Content> searchUserContent(String keyword);
}
