package com.example.myapp.user.commucomment.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.example.myapp.user.commucomment.model.CommuComment;

@Repository
@Mapper
public interface ICommuCommentRepository {
	
	void insertcommuComment(CommuComment commuComment);
	
}
