package com.example.myapp.user.commucomment.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.example.myapp.user.commucomment.model.CommuComment;

@Repository
@Mapper
public interface ICommuCommentRepository {
	void insertCommuComment(CommuComment commucomment);  //댓글 작성
	
	void updateCommuComment(CommuComment commucomment); // 댓글 수정
	
	void deleteCommuCommentByCommuCommentRefId(int commuCommentRefId); //게시글의 달린 대댓글 삭제
	
	void deleteCommuCommentByCommuCommentId(int commuCommentId); //게시글 원본댓글 삭제 

	void updateCommuCommentOrderAndDepth(int commuCommentRefId, int commuCommentDepth, int commuCommentOrder); // 댓글 관련 수 업데이트	
	
	List<CommuComment> selectCommuCommentsByCommuCommentCommuId(int commuCommentCommuId); //게시글에 따른 댓글 조회
	
	CommuComment selectCommuCommentById(int commuCommentId);  // 특정 ID를 가진 댓글의 정보를 조회

	int getLastCommuCommentOrder(int commuCommentCommuId);   // 게시글의 마지막 댓글 순서를 가져오기
	
	int getLastReplyOrder(int commuCommentRefId); // 원본 댓글에 대한 마지막 대댓글의 순서를 가져오기
	
	void deleteCommuCommentAndRepliesByMainRefId(int commuCommentMainRefId); //원본 댓글 삭제시 달린 답글 다 삭제
	

}
