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
	
	void updateReadCnt(int commuId); 

 	
	
	int selectMaxPost();
	
	void insertPost(Commu commu);
	void insertFileData(CommuFile file);
	CommuFile getFile(int commuFileId);

	
	
	
}