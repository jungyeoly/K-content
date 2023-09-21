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
	


	@PostMapping("/commu/comment")
	public ResponseEntity<Map<String, Object>> insertcommucomment(@RequestBody CommuComment commucomment) {
	    logger.info("Received request to post a comment: " + commucomment.toString());
	    System.out.println(commucomment);

	    Map<String, Object> response = new HashMap<>();

	    try {
	        commucommentService.insertCommuComment(commucomment); // 반환값이 없으므로 바로 호출
	        logger.info("Comment saved successfully.");

	        // 예: 성공 메시지 추가
	        response.put("message", "Comment saved successfully.");
	        return ResponseEntity.ok(response);

	    } catch (Exception e) {
	        logger.error("Error during posting a comment:", e);

	        // 에러 발생 시 메시지 추가 (선택적)
	        response.put("message", "Error during posting a comment.");
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
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
