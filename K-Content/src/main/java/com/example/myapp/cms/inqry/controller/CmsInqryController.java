package com.example.myapp.cms.inqry.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.beust.jcommander.internal.Console;
import com.example.myapp.cms.inqry.model.CmsInqry;
import com.example.myapp.cms.inqry.service.CmsInqryService;
import com.example.myapp.cms.inqry.service.ICmsInqryService;
import com.example.myapp.commoncode.service.ICommonCodeService;
import com.example.myapp.user.inqry.model.Inqry;
import com.example.myapp.user.inqry.service.IInqryService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/cs")
public class CmsInqryController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	ICommonCodeService commonCodeSerivce;
	
	@Autowired
	IInqryService inqryService;
	
	@GetMapping("/inqry/{page}")
	public String selectCmsInqryList(@PathVariable int page, HttpSession session, Model model) {
		int inqryPwdId = 0;
		session.setAttribute("inqryPwdId", inqryPwdId);
		session.setAttribute("page", page);
		
		List<Inqry> inqryList = inqryService.selectInqryList(page);
		model.addAttribute("inqryList", inqryList);
		
		logger.info(inqryList.toString());
		
		
		int bbsCount = inqryService.totalInqry();
		int totalPage = 0;
		
		if(bbsCount > 0) {
			totalPage= (int)Math.ceil(bbsCount/10.0);
		}
		int totalPageBlock = (int)(Math.ceil(totalPage/10.0));
		int nowPageBlock = (int) Math.ceil(page/10.0);
		int startPage = (nowPageBlock-1)*10 + 1;
		int endPage = 0;
		if(totalPage > nowPageBlock*10) {
			endPage = nowPageBlock*10;
		}else {
			endPage = totalPage;
		}
		model.addAttribute("totalPageCount", totalPage);
		model.addAttribute("nowPage", page);
		model.addAttribute("totalPageBlock", totalPageBlock);
		model.addAttribute("nowPageBlock", nowPageBlock);
		model.addAttribute("startPage", startPage);
		model.addAttribute("endPage", endPage);

		session.setAttribute("nowPage", page);
		
		return "cms/inqry/list";
	}
	
	@GetMapping("/inqry")
	public String selectInqryList(HttpSession session, Model model) {
		return "cms/inqry/main";
	}
	
	@PostMapping("/inqry/check-password")
	@ResponseBody
	public String checkPasswordAndSelectInqry(@RequestParam int inqryId, @RequestParam int enteredPwd, HttpSession session, Model model) {
		Inqry inqry = inqryService.selectInqry(inqryId);
		int inqryPwdId = (int) session.getAttribute("inqryPwdId");

		if (inqry.getInqryPwd() == enteredPwd) {
			inqryPwdId = inqryId;
		}

		session.setAttribute("inqryPwdId", inqryPwdId);
		
		if (inqryPwdId > 0) {
			// 세션에 고유 id 담아 확인
			return "cms/inqry/detail";
		} else {
			// 비밀번호 틀리면 다시 리스트로
			return "cms/inqry/list";
		}
	}
	
	@RequestMapping("/inqry/detail/{inqryId}")
	public String selectCmsInqry(@PathVariable int inqryId, Model model, HttpSession session) {
		int inqryPwdId = (int) session.getAttribute("inqryPwdId");
		
		if(inqryPwdId == inqryId) {
			Inqry inqry = inqryService.selectInqry(inqryId);
			model.addAttribute("inqry", inqry);
			return "cs/inqry/detail";
		} else {
			return "redirect: /cs/inqry";
		}
	}
}
