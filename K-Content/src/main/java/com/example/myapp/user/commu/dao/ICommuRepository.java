package com.example.myapp.user.commu.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.example.myapp.user.commu.model.Commu;
import com.example.myapp.user.commu.model.CommuFile;

@Repository
@Mapper
public interface ICommuRepository {
  
	List<Commu> selectAllPost(); //커뮤니티 전체글 다 보기
	
	Commu selectPost(int commuId); //커뮤니티 게시글 상세조회(댓글포함)
	
	void updateReadCnt(int commuId);//커뮤니티 게시글 조회수 업데이트
	
	void insertPost(Commu commu); //커뮤니티 글쓰기
	void insertFileData(CommuFile file); //커뮤니티 글쓰기에 첨부파일
	
	/*
	 * void updatePost(Commu commu); // 커뮤니티 글쓴 글 업데이트 void updateFiledata(CommuFile
	 * file);// 커뮤니티 글쓴 글에 첨부파일 업데이트
	 */
	int selectMaxPost();
	
	
	CommuFile getFile(int commuFileId);
}
