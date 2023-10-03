package com.example.myapp.user.commucomment.service;

import java.util.List;

import com.example.myapp.user.commucomment.model.CommuComment;

public interface ICommuCommentService {
	
	CommuComment insertCommuComment(CommuComment commucomment);  //댓글 작성
	
	void updateCommuComment(CommuComment commucomment); // 댓글 수정
	 
	void deleteCommuCommentAndComments(int commuCommentId);//원본댓글과 대댓글 모두 삭제시
	
	void updateCommuCommentId(int commuCommentRefId, int commuCommentDepth, int commuCommentOrder); // 댓글 관련 수 업데이트
	
	List<CommuComment> selectCommuCommentsByCommuCommentCommuId(int commuCommentCommuId); //게시글에 따른 댓글 조회
	
	CommuComment getCommuCommentWithReplies(int commuCommentId); ; // 댓글과 대댓글을 함께 조회
}
