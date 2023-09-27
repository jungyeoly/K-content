package com.example.myapp.user.commucomment.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.myapp.user.commucomment.dao.ICommuCommentRepository;
import com.example.myapp.user.commucomment.model.CommuComment;

@Service
public class CommuCommentService implements ICommuCommentService {

	@Autowired
	ICommuCommentRepository commucommentRepository;

	// 댓글 쓰기
	@Override
	@Transactional
	public CommuComment insertCommuComment(CommuComment commucomment) {
	    String currentTimestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
	    if (commucomment.getCommuCommentRegistDate() == null) {
	        commucomment.setCommuCommentRegistDate(currentTimestamp);
	        
	        // 댓글 삽입
	        commucommentRepository.insertCommuComment(commucomment);
	        System.out.println(commucomment);
	    }

	    return commucomment;
	}


	@Transactional
	public void updateCommuComment(CommuComment commucomment) {
		String currentTimestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		commucomment.setCommuCommentUpdateDate(currentTimestamp);
		commucommentRepository.updateCommuComment(commucomment);
	}

	@Override
	public void deleteCommuCommentAndComments(int commuCommentId) {
		  // 먼저 대댓글들을 삭제
	    commucommentRepository.deleteCommuCommentByCommuCommentRefId(commuCommentId);
	    // 그 다음 원본 댓글 삭제
	    commucommentRepository.deleteCommuCommentByCommuCommentId(commuCommentId);	 
	}

	@Transactional
	public void updateCommuCommentId(int commuCommentRefId, int commuCommentDepth, int commuCommentOrder) {
		commucommentRepository.updateCommuCommentId(commuCommentRefId, commuCommentDepth, commuCommentOrder);
	}
	
	@Override
	public List<CommuComment> selectCommuCommentsByCommuCommentId(int commuCommentId) {
		return commucommentRepository.selectCommuCommentsByCommuCommentId(commuCommentId);
	}
}
