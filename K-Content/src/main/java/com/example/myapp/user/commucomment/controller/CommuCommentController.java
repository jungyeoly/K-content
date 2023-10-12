package com.example.myapp.user.commucomment.controller;

import java.security.Principal;
import java.util.ArrayList;
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

//        List<List<Object>> alignmentComments = new ArrayList<>();
        List<Integer> keys = new ArrayList<>();
        List<CommuComment> refComment = new ArrayList<>();
        List<CommuComment> values = new ArrayList<>();
//계층형 쿼리 쓰면 1번만 써도됨
//        for (int i = 0; i < comments.size(); i++) {
//            if (comments.get(i).getCommuCommentRefId() == 0) {
////                int key = comments.get(i).getCommuCommentId();
//                keys.add(comments.get(i).getCommuCommentId());
//            } else {
//                for (int j = 0; j < keys.size(); j++) {
//                    if (keys.get(j) == comments.get(i).getCommuCommentRefId()) {
//                        refComment.add(comments.get(i));
//                    }
//                }
//            }
//        }
        for (int i = 0; i < comments.size(); i++) {
            if (comments.get(i).getCommuCommentRefId() == 0) {
//                int key = comments.get(i).getCommuCommentId();
                keys.add(comments.get(i).getCommuCommentId());
            } else {
                values.add(comments.get(i));
            }
        }

        for (int i = 0; i < values.size(); i++) {
            for (int j = 0; j < keys.size(); j++) {
                if (keys.get(j) == values.get(i).getCommuCommentRefId()) {
                    refComment.add(values.get(i));
                }
            }
        }

        for (int i = 0; i < refComment.size(); i++) {
            System.out.println(refComment.get(i).getCommuCommentId());
        }


//        model.addAttribute("keys", keys);
        model.addAttribute("refComments", refComment);
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
                                   @RequestParam("coCntnt") String cntnt,
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
    @PostMapping("/commu/comment/update")
    @ResponseBody
    public String updateCommuComment(@RequestParam("commuCommentId") int commuCommentId,
                                     @RequestParam("commuCommentCntnt") String commuCommentCntnt) {
        commuCommentService.updateCommuComment(commuCommentId, commuCommentCntnt);
        return "ok";

    }

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
