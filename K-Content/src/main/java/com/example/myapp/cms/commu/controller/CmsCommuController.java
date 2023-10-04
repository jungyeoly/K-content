package com.example.myapp.cms.commu.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.myapp.cms.commu.model.CmsCommu;
import com.example.myapp.cms.commu.service.ICmsCommuService;
import com.example.myapp.commoncode.model.CommonCode;
import com.example.myapp.commoncode.service.ICommonCodeService;
import com.example.myapp.user.commu.model.Commu;
import com.example.myapp.user.commu.model.CommuFile;
import com.example.myapp.user.commu.service.ICommuService;
import com.example.myapp.user.commucomment.model.CommuComment;
import com.example.myapp.user.commucomment.service.ICommuCommentService;

import jakarta.servlet.http.HttpSession;

@RequestMapping("/cms")
@Controller
public class CmsCommuController {static final Logger logger = LoggerFactory.getLogger(CmsCommuController.class);

	@Autowired
	ICmsCommuService cmsCommuService;
	
	@Autowired
	ICommuCommentService commuCommentService;
	@Autowired
	ICommuService commuService;
	
	@Autowired
	private ICommonCodeService commonCodeService;
	
	@GetMapping("/commu/{page}") //관리자 커뮤니티 메인
	public String selectAllUserPost(@RequestParam String commonCodeVal, @PathVariable int page, @ModelAttribute("cmsCommu") CmsCommu cmscommu, Model model,
			HttpSession session) {
	session.setAttribute("page", page);	
	
	List<CmsCommu> cmscommulist = cmsCommuService.selectAllUserPost(commonCodeVal, page);
	for (CmsCommu cm : cmscommulist) {
		System.out.println(cm.getCommonCodeVal());
	}
	
	model.addAttribute("cmscommulist", cmscommulist);
	List<String> cmscatelist = commonCodeService.cateList("C03");
	
	model.addAttribute("cmscatelist", cmscatelist);
	
	int cmscommuCount = commuService.totalCommu();
	int totalPage = 0;

	if (cmscommuCount > 0) {
		totalPage = (int) Math.ceil(cmscommuCount / 10.0);
	}
	int totalPageBlock = (int) (Math.ceil(totalPage / 10.0));
	int nowPageBlock = (int) Math.ceil(page / 10.0);
	int startPage = (nowPageBlock - 1) * 10 + 1;
	int endPage = 0;
	if (totalPage > nowPageBlock * 10) {
		endPage = nowPageBlock * 10;
	} else {
		endPage = totalPage;
	}
	model.addAttribute("totalPageCount", totalPage);
	model.addAttribute("nowPage", page);
	model.addAttribute("totalPageBlock", totalPageBlock);
	model.addAttribute("nowPageBlock", nowPageBlock);
	model.addAttribute("startPage", startPage);
	model.addAttribute("endPage", endPage);

	session.setAttribute("nowPage", page);

	return "cms/commu/list";
}
	
	// 커뮤니티 게시글 제목 누르면 상세보기
	@GetMapping("/commu/detail/{commuId}")
	public String getCommuDetails(@PathVariable int commuId, Model model) {
		List<CommonCode> commuCateCodeList = commonCodeService.findCommonCateCodeByUpperCommonCode("C03");
		model.addAttribute("commuCateCodeList", commuCateCodeList);
		Commu commu = commuService.selectPost(commuId);
		List<CommuFile> commuFiles = commuService.selectFilesByPostId(commuId);
		model.addAttribute("commu", commu);
		model.addAttribute("commuFiles", commuFiles);
		logger.info("getCommuDetails" + commu.toString());
		// 게시글에 연결된 댓글 정보 조회
		List<CommuComment> commentsWithReplies = new ArrayList<>();

	    List<CommuComment> comments = commuCommentService.selectCommuCommentsByCommuCommentCommuId(commuId);
	    for (CommuComment comment : comments) {
	        CommuComment fullComment = commuCommentService.getCommuCommentWithReplies(comment.getCommuCommentId());
	        commentsWithReplies.add(fullComment);
	    }
	    model.addAttribute("comments", commentsWithReplies);

	    return "cms/commu/view";
	}
	
}
