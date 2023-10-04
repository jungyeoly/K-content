package com.example.myapp.user.commucomment.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.myapp.user.commucomment.dao.ICommuCommentRepository;
import com.example.myapp.user.commucomment.model.CommuComment;

@Service
public class CommuCommentService implements ICommuCommentService {
	static final Logger logger = LoggerFactory.getLogger(CommuCommentService.class);
	@Autowired
	ICommuCommentRepository commucommentRepository;

	// 댓글 쓰기
	@Override
	@Transactional
	public CommuComment insertCommuComment(CommuComment commucomment) {
	    String currentTimestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
	    
	    if (commucomment.getCommuCommentRegistDate() == null) {
	        commucomment.setCommuCommentRegistDate(currentTimestamp);
	    }

	    // 원본 댓글인 경우
	    if(commucomment.getCommuCommentRefId() == 0) {
	        int lastOrder = commucommentRepository.getLastCommuCommentOrder(commucomment.getCommuCommentCommuId());
	        commucomment.setCommuCommentOrder(lastOrder + 1);
	        System.out.println("commuCommentCommuId value: " + commucomment.getCommuCommentCommuId());
	        commucomment.setCommuCommentDepth(0); 
	    }
	    // 대댓글인 경우
	    else {
	        int lastOrderForReply = commucommentRepository.getLastReplyOrder(commucomment.getCommuCommentRefId());
	        commucomment.setCommuCommentOrder(lastOrderForReply + 1);
	        CommuComment parentComment = commucommentRepository.selectCommuCommentById(commucomment.getCommuCommentRefId());
	        commucomment.setCommuCommentDepth(parentComment.getCommuCommentDepth() + 1);
	    }
	    
	    commucommentRepository.insertCommuComment(commucomment);
	    System.out.println(commucomment);

	    return commucomment;
	}



	@Transactional
	public void updateCommuComment(CommuComment commucomment) {
		String currentTimestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		commucomment.setCommuCommentUpdateDate(currentTimestamp);
		commucommentRepository.updateCommuComment(commucomment);
	}

	@Transactional
	public void deleteCommuCommentAndComments(int commuCommentId) {
		  // 먼저 대댓글들을 삭제
	    commucommentRepository.deleteCommuCommentByCommuCommentRefId(commuCommentId);
	    // 그 다음 원본 댓글 삭제
	    commucommentRepository.deleteCommuCommentByCommuCommentId(commuCommentId);	 
	}

	@Transactional
	public void updateCommuCommentId(int commuCommentRefId, int commuCommentDepth, int commuCommentOrder) {
		commucommentRepository.updateCommuCommentOrderAndDepth(commuCommentRefId, commuCommentDepth, commuCommentOrder);
	}
	
	@Override
	public List<CommuComment> selectCommuCommentsByCommuCommentCommuId(int commuCommentId) {
		return commucommentRepository.selectCommuCommentsByCommuCommentCommuId(commuCommentId);
	}



	@Override
	public CommuComment getCommuCommentWithReplies(int commuCommentId) {
	    // 로그를 남겨서 어떤 commuCommentId로 함수가 호출되었는지 확인
	    logger.info("getCommuCommentWithReplies method called with commuCommentId: " + commuCommentId);
	    
	    // 주어진 ID로 원본 댓글 조회
	    CommuComment originalComment = commucommentRepository.selectCommuCommentById(commuCommentId);
	    System.out.println("originalComment: " + originalComment);
	    
	    if(originalComment != null) {
	        // 해당 게시글에 작성된 모든 댓글 및 대댓글을 조회
	        List<CommuComment> allCommentsForPost = commucommentRepository.selectCommuCommentsByCommuCommentCommuId(originalComment.getCommuCommentCommuId());
	        
	        // 원본 댓글에 해당하는 모든 대댓글을 재귀적으로 설정
	        setRepliesRecursively(originalComment, allCommentsForPost);
	    }
	    
	    return originalComment;
	}

	private void setRepliesRecursively(CommuComment parentComment, List<CommuComment> allComments) {
	    // 주어진 parentComment의 직접적인 대댓글들만을 필터링
	    List<CommuComment> directReplies = allComments.stream()
	                                        .filter(c -> c.getCommuCommentRefId() == parentComment.getCommuCommentId())
	                                        .collect(Collectors.toList());

	    // 각 대댓글에 대해 재귀적으로 이 함수를 호출하여 그 대댓글의 대댓글들도 찾아 설정
	    for (CommuComment reply : directReplies) {
	        setRepliesRecursively(reply, allComments);
	    }

	    // 필터링된 직접적인 대댓글들을 parentComment의 replies 리스트에 설정
	    parentComment.setReplies(directReplies);
	}

}
