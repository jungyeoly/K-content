package com.example.myapp.user.commucomment.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.example.myapp.user.commucomment.model.CommuComment;

@Repository
@Mapper
public interface ICommuCommentRepository {
	CommuComment insertCommuComment(CommuComment commucomment); //댓글 처리
	
	void updateCommuComment(CommuComment commucomment); // 댓글 수정

	void deleteCommuCommentByCommuCommentRefId(int commuCommentRefId); //게시글의 달린 댓글 삭제

	void updateCommuCommentId(int commuCommentRefId, int commuCommentDepth, int commuCommentOrder); // 댓글 관련 수 업데이트	
	
	List<CommuComment> selectCommuCommentsByCommuCommentId(int commuCommentId); //게시글에 따른 댓글 조회

	

}
