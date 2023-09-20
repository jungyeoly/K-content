package com.example.myapp.user.commucomment.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.myapp.user.commucomment.model.CommuComment;
import com.example.myapp.user.commucomment.service.ICommuCommentService;

@Controller
public class CommuCommentController {
	static final Logger logger = LoggerFactory.getLogger(CommuCommentController.class);
	@Autowired
	ICommuCommentService commucommentService;
	


	// 댓글 작성
	@PostMapping("/commu/comment")
	public ResponseEntity<CommuComment> insertcommucomment(@RequestBody CommuComment commucomment) {
	    logger.info("Received request to post a comment: " + commucomment.toString());

	    try {
	        CommuComment savedCommuComment = commucommentService.insertCommuComment(commucomment);
	        logger.info("Comment saved successfully.");
	        return ResponseEntity.ok(savedCommuComment);

	    } catch (Exception e) {
	        logger.error("Error during posting a comment:", e);
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	    }
	}


	// 댓글 수정
	@PostMapping("/commu/comment/update/{commucommentId}")
	public ResponseEntity<Map<String, Object>> updateCommuComment(@PathVariable int commucommentId, @RequestBody CommuComment commucomment) {
		Map<String, Object> response = new HashMap<>();
		logger.info("Received request to update a comment: " + commucomment.toString());

		try {
			commucommentService.updateCommuComment(commucomment); 
			logger.info("Comment updated successfully.");

			response.put("status", "success");
			response.put("message", "댓글이 성공적으로 수정되었습니다.");
			return ResponseEntity.ok(response);

		} catch (Exception e) {
			logger.error("Error during updating a reply:", e);
			response.put("status", "error");
			response.put("message", "댓글 수정 중 오류가 발생하였습니다.");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}

	// 댓글 삭제
	@PostMapping("/commu/comment/delete/{commucommentRefId}")
	public ResponseEntity<Map<String, Object>> deleteCommuComment(@PathVariable int commucommentRefId) {
		Map<String, Object> response = new HashMap<>();
		logger.info("Received request to delete a reply with commucommentRefId: " + commucommentRefId);

		try {
			commucommentService.deleteCommuCommentByCommuCommentRefId(commucommentRefId);  
			logger.info("Reply deleted successfully.");

			response.put("status", "success");
			response.put("message", "댓글이 성공적으로 삭제되었습니다.");
			return ResponseEntity.ok(response);

		} catch (Exception e) {
			logger.error("Error during deleting a comment:", e);
			response.put("status", "error");
			response.put("message", "댓글 삭제 중 오류가 발생하였습니다.");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}


}
