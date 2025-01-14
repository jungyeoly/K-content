package com.example.myapp.user.commu.dao;

import java.util.List;

import com.example.myapp.user.commu.model.CommuFile;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.example.myapp.user.commu.model.Commu;

@Repository
@Mapper
public interface ICommuRepository {

	int totalCommu(); //총 게시물 개수

	int totalCommuByCategory(String commuCateCode); //총 카테고리별 게시글 개수

	int selectMaxPost(); // 가장 최근 게시판글

	int selectMaxCommuFileCommuId(); // 가장 최근 첨부파일이 있는 게시글 번호

	void insertPost(Commu commu); // 커뮤니티 글쓰기

	void insertFileData(CommuFile file); // 커뮤니티 글쓰기에 첨부파일

	List<Commu> selectAllPost(@Param("start") int start, @Param("end") int end); // 커뮤니티 전체글 다 보기

	Commu selectPost(int commuId); // 커뮤니티 게시글 상세조회

	List<CommuFile> selectFilesByPostId(int commuId); //커뮤니티 게시글 첨부파일 상세조회

	List<Commu> selectPostListByCategory(@Param("commuCateCode") String commuCateCode, @Param("start") int start, @Param("end") int end);  //카테고리별 게시글 조회

	CommuFile getFile(String commuFileId);

	void updateReadCnt(int commuId);// 커뮤니티 게시글 조회수 업데이트

	void updatePost(Commu commu); // 커뮤니티 게시글 업데이트
	void updateFiledata(CommuFile file);// 커뮤니티 게시글에 첨부파일 업데이트


	void deletePostStatus(int commuId); //커뮤니티 게시글 삭제(삭제상태로 변경)
	void deleteFileById(String commuFileId); // 첨부파일의 ID를 기반으로 특정 첨부파일 삭제

	 List<CommuFile> getAllFilesByCommuId(int commuId); //모든 첨부파일

	 void reportPost(int commuId); //게시글 신고

	 int selectTotalPostCountByKeyWord(String Keyword); //키워드와 관련된 총 게시물 수

	 List<Commu> searchListByContentKeyWord(@Param("keyword") String keyword, @Param("start") int start, @Param("end") int end); //관련된 게시물 목록 검색

	 List<Commu> selectRecentNotice(); // 가장 최근 공지사항

}
