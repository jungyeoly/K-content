package com.example.myapp.user.commu.service;

import java.util.List;

import com.example.myapp.user.commu.model.Commu;
import com.example.myapp.user.commu.model.CommuFile;

public interface ICommuService {
	
	List<Commu> selectAllPost(); //커뮤니티 전체글 다 보기
	
	Commu selectPost(int commuId); //커뮤니티 게시글 상세조회(댓글포함)
	
	void insertPost(Commu commuId); //커뮤니티 글쓰기
	void insertPost(Commu commuId, CommuFile file);  //커뮤니티 글쓰기에 첨부파일
	
	CommuFile getFile(int commuFileid);
}

