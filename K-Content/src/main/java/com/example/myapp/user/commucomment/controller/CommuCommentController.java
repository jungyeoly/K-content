package com.example.myapp.user.commucomment.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.myapp.commoncode.model.CommonCode;
import com.example.myapp.commoncode.service.ICommonCodeService;
import com.example.myapp.user.commu.model.Commu;
import com.example.myapp.user.commu.service.ICommuService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.myapp.user.commucomment.model.CommuComment;
import com.example.myapp.user.commucomment.service.CommuCommentService;
import com.example.myapp.user.commucomment.service.ICommuCommentService;

@Controller
public class CommuCommentController {
    static final Logger logger = LoggerFactory.getLogger(CommuCommentController.class);
    @Autowired
    ICommuService commuService;

    @Autowired
    ICommuCommentService commuCommentService;

    @Autowired
    private ICommonCodeService commonCodeService;

    // 커뮤니티 게시글 제목 누르면 상세보기
    @GetMapping("/commu/detail/comment")
    public String getCommuCommentDetails(@RequestParam("commuId") int commuId, Model model) {
        List<CommonCode> commuCateCodeList = commonCodeService.findCommonCateCodeByUpperCommonCode("C03");
        List<CommonCode> commonCodeVal = commonCodeService.findByCommonCodeVal("NOTICE");

        model.addAttribute("commonCodeVal", commonCodeVal);
        model.addAttribute("commuCateCodeList", commuCateCodeList);
        Commu commu = commuService.selectPost(commuId);
        model.addAttribute("commu", commu);

        List<CommuComment> comments = commuCommentService.selectCommuCommentsByCommuCommentCommuId(commuId);

        model.addAttribute("comments", comments);

        return "user/commu/comment";
    }

    // 댓글 쓰기
    @PostMapping("/commu/detail/comment")
    @ResponseBody
    public String insertCommuCommentP(@RequestParam("commuCommentCommuId") int commuId,
                                      @RequestParam("commuCommentCntnt") String cntnt,
                                      Principal principal) {
        CommuComment cc = new CommuComment();
        cc.setCommuCommentCommuId(commuId);
        cc.setCommuCommentCntnt(cntnt);
        cc.setCommuCommentMberId(principal.getName());
        cc.setCommuCommentRefId(0);
        commuCommentService.insertCommuComment(cc);
        return "ok";
    }

    //대댓글
    @PostMapping("/commu/detail/reply")
    @ResponseBody
    public String insertCommureply(@RequestParam("commuCommentCommuId") int commuId,
                                   @RequestParam("commuCommentCntnt") String cntnt,
                                   @RequestParam("commuCommentRefId") int replyId,
                                   Principal principal) {
        CommuComment cc = new CommuComment();
        cc.setCommuCommentCommuId(commuId);
        cc.setCommuCommentCntnt(cntnt);
        cc.setCommuCommentMberId(principal.getName());
        cc.setCommuCommentRefId(replyId);
        commuCommentService.insertCommuComment(cc);
        return "ok";
    }

    //	// 댓글 수정
//	@PostMapping("/commu/comment/update/{commucommentId}")
//	public ResponseEntity<Map<String, Object>> updateCommuComment(@PathVariable int commucommentId,
//			@RequestBody CommuComment commucomment) {
//		Map<String, Object> response = new HashMap<>();
//		logger.info("Received request to update a comment: " + commucomment.toString());
//
//		try {
//			commucommentService.updateCommuComment(commucomment);
//			System.out.println(commucomment);
//			logger.info("Comment updated successfully.");
//
//			response.put("status", "success");
//			response.put("message", "댓글이 성공적으로 수정되었습니다.");
//			response.put("updatedComment", commucomment.getCommuCommentCntnt()); // 수정된 댓글 내용
//			response.put("commuCommentUpdateDate", commucomment.getCommuCommentUpdateDate()); // 수정된 댓글 날짜 업데이트
//			System.out.println(commucomment);
//			return ResponseEntity.ok(response);
//
//		} catch (Exception e) {
//			logger.error("Error during updating a reply:", e);
//			response.put("status", "error");
//			response.put("message", "댓글 수정 중 오류가 발생하였습니다.");
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
//		}
//	}
//
//	// 댓글 삭제
    @PostMapping("/commu/comment/delete")
    @ResponseBody
    public String deleteCommuComment(@RequestParam("commuCommentId") int commuCommentId,
                                     @RequestParam("commuCommentRefId") int commuCommentRefId) {
        if (commuCommentRefId == 0) {
            commuCommentService.deleteCommuCommentAndRepliesByMainRefId(commuCommentId);
        } else {
            commuCommentService.deleteCommuCommentRepl(commuCommentId);
        }
        return "ok";
    }
//
//	@GetMapping("/commu/comment/{commuCommentId}")
//	public ResponseEntity<Map<String, Object>> getCommuCommentWithReplies(@PathVariable int commuCommentId) {
//		Map<String, Object> response = new HashMap<>();
//		System.out.println("Started: getCommuCommentWithReplies");
//		logger.info("Received request to get a comment with replies for ID: " + commuCommentId);
//
//		try {
//			System.out.println("Trying to fetch comment with replies for ID: " + commuCommentId);
//			// getCommuCommentWithReplies 메서드를 호출하여 댓글과 대댓글을 함께 조회
//			CommuComment commuCommentWithReplies = commucommentService.getCommuCommentWithReplies(commuCommentId);
//
//			System.out.println("Successfully fetched comment with replies for ID: " + commuCommentId);
//
//			response.put("status", "success");
//			response.put("message", "댓글 및 대댓글 조회 성공.");
//			response.put("commuCommentWithReplies", commuCommentWithReplies);
//			return ResponseEntity.ok(response);
//		} catch (Exception e) {
//			System.out.println("Error while fetching comment with replies for ID: " + commuCommentId);
//			logger.error("Error during getting a comment with replies:", e);
//			response.put("status", "error");
//			response.put("message", "댓글 및 대댓글 조회 중 오류가 발생하였습니다.");
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
//		}
//	}

}
