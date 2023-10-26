package com.example.myapp.user.commucomment.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.myapp.user.commu.model.Commu;
import com.example.myapp.user.commu.service.ICommuService;
import com.example.myapp.commoncode.model.CommonCode;
import com.example.myapp.commoncode.service.ICommonCodeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.myapp.user.commucomment.model.CommuComment;
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

        model.addAttribute("commuCateCodeList", commuCateCodeList);
        Commu commu = commuService.selectPostWithoutIncreasingReadCnt(commuId);
        model.addAttribute("commu", commu);

        List<CommuComment> comments = commuCommentService.selectCommuCommentsByCommuCommentCommuId(commuId);

        List<Integer> keys = new ArrayList<>();
        List<CommuComment> refComment = new ArrayList<>();
        List<CommuComment> values = new ArrayList<>();


        for (int i = 0; i < comments.size(); i++) {
            if (comments.get(i).getCommuCommentRefId() == 0) {
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
        Map<Integer, Integer> replyCountMap = new HashMap<>(); //개별 댓글의 답글  확인하기
        for (int key : keys) {
        	int count = 0;
        	for(CommuComment ref : refComment) {
        		if(ref.getCommuCommentRefId() == key) {
        			count++;
        		}
        	}
        	replyCountMap.put(key,count);

        }

        model.addAttribute("replyCountMap", replyCountMap);
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

    	// 댓글 수정
    @PostMapping("/commu/comment/update")
    @ResponseBody
    public String updateCommuComment(@RequestParam("commuCommentId") int commuCommentId,
                                     @RequestParam("commuCommentCntnt") String commuCommentCntnt) {
        commuCommentService.updateCommuComment(commuCommentId, commuCommentCntnt);
        return "ok";

    }

	// 댓글 삭제
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

}
