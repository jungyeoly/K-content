package com.example.myapp.user.commu.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.example.myapp.user.commu.model.Commu;
import com.example.myapp.user.commu.model.CommuFile;

public interface ICommuService {
	
	
	int totalCommu(); //총 게시물 개수
	
	int totalCommuByCategory(String commuCateCode); //총 카테고리별 게시글 개수

	List<Commu> selectAllPost(int page); // 커뮤니티 전체글 다 보기

	Commu selectPost(int commuId); // 커뮤니티 게시글 상세조회

	List<CommuFile> selectFilesByPostId(int commuId); // 커뮤니티 게시글 첨부파일 상세조회

	void insertPost(Commu commu); // 커뮤니티 글쓰기
	
	void insertPostwithFiles(Commu commu, List<CommuFile> files); // 커뮤니티 글쓰기에 첨부파일

	void updatePost(Commu commu); // 커뮤니티 게시글 업데이트

	void updatePostAndFiles(Commu commu, List<CommuFile> files);// 커뮤니티 게시글에 첨부파일 업데이트

	void deletePost(int commuId); // 커뮤니티 게시글 삭제(삭제상태로 변경)

	void deleteFileById(String commuFileId); // 첨부파일의 ID를 기반으로 특정 첨부파일 삭제

	CommuFile getFile(String commufileId);
	
	List<CommuFile> getAllFilesByCommuId(int commuId); //모든 첨부파일 

	List<Commu> selectPostListByCategory(@Param("commuCateCode") String commuCateCode, int page);  //카테고리별 게시글 조회
	
	 void reportPost(int commuId); //게시글 신고

	 
	List<Commu> searchListByContentKeyword(String keyword, int page); //관련된 게시물 목록 검색
	int selectTotalPostCountByKeyWord(String keyword); //키워드와 관련된 총 게시물 수

}