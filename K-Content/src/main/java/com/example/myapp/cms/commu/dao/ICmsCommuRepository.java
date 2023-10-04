package com.example.myapp.cms.commu.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.example.myapp.cms.commu.model.CmsCommu;

@Repository
@Mapper
public interface ICmsCommuRepository {
	List<CmsCommu> selectAllUserPost(String commonCodeVal, @Param("start") int start, @Param("end") int end); // 커뮤니티 전체글 다 보기
	
	CmsCommu selectUserPost(int commuId); // 커뮤니티 게시글 상세조회
	
	void insertAdminPost(CmsCommu cmsCommu); //커뮤니티 글쓰기

}
