package com.example.myapp.user.commucomment.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.myapp.user.commucomment.dao.ICommuCommentRepository;
import com.example.myapp.user.commucomment.model.CommuComment;

@Service
public class CommuCommentService implements ICommuCommentService {
	static final Logger logger = LoggerFactory.getLogger(CommuCommentService.class);
	@Autowired
	ICommuCommentRepository commucommentRepository;
	@Override
	public void insertcommuComment(CommuComment commuComment) { 
		commucommentRepository.insertcommuComment(commuComment);
	}

	
	


}
