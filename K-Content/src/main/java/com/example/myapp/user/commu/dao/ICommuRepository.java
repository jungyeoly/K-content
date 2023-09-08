package com.example.myapp.user.commu.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.example.myapp.user.commu.model.Commu;
import com.example.myapp.user.commu.model.CommuFile;

@Repository
@Mapper
public interface ICommuRepository {
  
	int selectMaxPost(); //가장 최근 게시판글
	int selectMaxCommuFileCommuId(); //가장 최근 첨부파일이 있는 게시글 번호
	
	
	void insertPost(Commu commu); //커뮤니티 글쓰기
	void insertFileData(CommuFile file); //커뮤니티 글쓰기에 첨부파일
	
	List<Commu> selectAllPost(); //커뮤니티 전체글 다 보기
	
	Commu selectPost(int commuId); //커뮤니티 게시글 상세조회(댓글포함)
	CommuFile getFile(int commuFileId);
	
	void updateReadCnt(int commuId);//커뮤니티 게시글 조회수 업데이트
	
	void updateReplyNumber(@Param("commuReadCnt") int commuReadCnt, @Param("commuRefId") int commuRefId, @Param("commuDepth") int commuDepth, @Param("commuOrder") int commuOrder);// 댓글 관련 업데이트 
	void updatePost(Commu commu); // 커뮤니티 게시글 업데이트
	void updateFiledata(CommuFile file);// 커뮤니티 게시글에 첨부파일 업데이트
	

	
	
	
	

	

	
}
