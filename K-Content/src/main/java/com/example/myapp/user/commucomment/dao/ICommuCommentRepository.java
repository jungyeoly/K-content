package com.example.myapp.user.commucomment.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.example.myapp.user.commucomment.model.CommuComment;

import java.util.List;

@Repository
@Mapper
public interface ICommuCommentRepository {

	void insertCommuComment(@Param("commuComment") CommuComment cc);
	List<CommuComment> selectCommuCommentsByCommuCommentCommuId(int commuId);
	CommuComment selectCommuCommentById(int commentId);

	void deleteCommuComment(@Param("commentId") int commentId);
	void deleteCommuCommentRefAll(@Param("commentId") int commentId);
	void updateCommuComment(@Param("commentId") int commentId, @Param("commuCommentCntnt") String commuCommentCntnt);
	List<Integer> selectAllCommentIdsByPostId(int commuId);
}
