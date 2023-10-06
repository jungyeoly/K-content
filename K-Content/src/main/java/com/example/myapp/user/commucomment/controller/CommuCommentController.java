package com.example.myapp.user.commucomment.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
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

	// 댓글 쓰기
	@PostMapping("/commu/comment")
	public ResponseEntity<Map<String, Object>> insertcommucomment(@RequestBody CommuComment commucomment) {
		logger.info("Received request to post a comment: " + commucomment.toString());
		System.out.println(commucomment);

		Map<String, Object> response = new HashMap<>();

		try {
			CommuComment savedCommuComment = commucommentService.insertCommuComment(commucomment);

			response.put("message", "Comment successfully saved.");
			response.put("comment", savedCommuComment);
			return ResponseEntity.ok(response);

		} catch (Exception e) {
			logger.error("Error during posting a comment:", e);
			response.put("message", "Error during posting a comment.");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}

	// 댓글 수정
	@PostMapping("/commu/comment/update/{commucommentId}")
	public ResponseEntity<Map<String, Object>> updateCommuComment(@PathVariable int commucommentId,
			@RequestBody CommuComment commucomment) {
		Map<String, Object> response = new HashMap<>();
		logger.info("Received request to update a comment: " + commucomment.toString());

		try {
			commucommentService.updateCommuComment(commucomment);
			System.out.println(commucomment);
			logger.info("Comment updated successfully.");

			response.put("status", "success");
			response.put("message", "댓글이 성공적으로 수정되었습니다.");
			response.put("updatedComment", commucomment.getCommuCommentCntnt()); // 수정된 댓글 내용
			response.put("commuCommentUpdateDate", commucomment.getCommuCommentUpdateDate()); // 수정된 댓글 날짜 업데이트
			System.out.println(commucomment);
			return ResponseEntity.ok(response);

		} catch (Exception e) {
			logger.error("Error during updating a reply:", e);
			response.put("status", "error");
			response.put("message", "댓글 수정 중 오류가 발생하였습니다.");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}

	/// 댓글 삭제
	@PostMapping("/commu/comment/delete/{commucommentId}")
	public ResponseEntity<Map<String, Object>> deleteCommuComment(@PathVariable int commucommentId) {
		Map<String, Object> response = new HashMap<>();
		logger.info("Received request to delete a comment with commucommentId: " + commucommentId);

		try {
			// 원본 댓글과 그에 해당하는 대댓글들을 삭제
			commucommentService.deleteCommuCommentAndComments(commucommentId);
			logger.info("Comment and its replies deleted successfully.");

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

	@GetMapping("/commu/comment/{commuCommentId}")
	public ResponseEntity<Map<String, Object>> getCommuCommentWithReplies(@PathVariable int commuCommentId) {
	    Map<String, Object> response = new HashMap<>();
	    System.out.println("Started: getCommuCommentWithReplies"); // <-- 추가
	    logger.info("Received request to get a comment with replies for ID: " + commuCommentId);

	    try {
	        System.out.println("Trying to fetch comment with replies for ID: " + commuCommentId); // <-- 추가
	        // getCommuCommentWithReplies 메서드를 호출하여 댓글과 대댓글을 함께 조회
	        CommuComment commuCommentWithReplies = commucommentService.getCommuCommentWithReplies(commuCommentId);
	        
	        System.out.println("Successfully fetched comment with replies for ID: " + commuCommentId); // <-- 추가

	        response.put("status", "success");
	        response.put("message", "댓글 및 대댓글 조회 성공.");
	        response.put("commuCommentWithReplies", commuCommentWithReplies);
	        return ResponseEntity.ok(response);
	    } catch (Exception e) {
	        System.out.println("Error while fetching comment with replies for ID: " + commuCommentId); // <-- 추가
	        logger.error("Error during getting a comment with replies:", e);
	        response.put("status", "error");
	        response.put("message", "댓글 및 대댓글 조회 중 오류가 발생하였습니다.");
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	    }
	}

}
