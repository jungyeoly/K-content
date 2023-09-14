package com.example.myapp.user.commu.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.example.myapp.user.commu.model.Commu;
import com.example.myapp.user.commu.model.CommuFile;

public interface ICommuService {

	List<Commu> selectAllPost(); // 커뮤니티 전체글 다 보기

	Commu selectPost(int commuId); // 커뮤니티 게시글 상세조회
	
	List<CommuFile> selectFilesByPostId(int commuId); //커뮤니티 게시글 첨부파일 상세조회
	
	List<Commu> selectPostListByCategory(@Param("commuCateCode") String commuCateCode, int page);  //카테고리에 따른 게시글 상세조회

	void insertPost(Commu commu); // 커뮤니티 글쓰기

	void insertPost(Commu commu, List<CommuFile> files); // 커뮤니티 글쓰기에 첨부파일

	void updatePost(Commu commu); // 커뮤니티 게시글 업데이트
 
	void updatePost(Commu commu, List<CommuFile> files); // 커뮤니티 게시글에 첨부파일 업데이트

	CommuFile getFile(int commuFileid);
	
	

}
