package com.example.myapp.cms.inqry.controller;

import java.security.Principal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.example.myapp.cms.inqry.model.CmsInqry;
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

	@Autowired
	ICmsInqryService cmsInqryService;

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

	@RequestMapping("/inqry/detail/{inqryId}")
	public String selectCmsInqry(@PathVariable int inqryId, Model model, HttpSession session) {
		Inqry inqry = inqryService.selectInqry(inqryId);
		model.addAttribute("inqry", inqry);
		session.setAttribute("inqryId", inqryId);

		int cnt = cmsInqryService.countInqry(inqryId);
		model.addAttribute("cnt", cnt);

		if (inqry.getInqryGroupOrd() == 1) {
			Inqry originInqry = inqryService.selectInqry(inqry.getInqryRefId());
			model.addAttribute("origin", originInqry);
			model.addAttribute("ref", "ref");
		}

		return "cms/inqry/detail";
	}

	@GetMapping("/inqry/write")
	public String writeInqry(HttpSession session, Model model) {
		int inqryId = (int) session.getAttribute("inqryId");

		Inqry inqry = inqryService.selectInqry(inqryId);
		model.addAttribute("inqry", inqry);

		int cnt = cmsInqryService.countInqry(inqry.getInqryRefId());

		if (cnt > 1) {
			model.addAttribute("message", "이미 답글이 등록된 글입니다.");
			return "cms/inqry/main";
		}
		else {
			return "cms/inqry/write";
		}
	}

	@PostMapping("/inqry/write")
	public String writeInqry(CmsInqry cmsInqry, HttpSession session, Principal principal) {
		int inqryId = (int) session.getAttribute("inqryId");
		Inqry inqry = inqryService.selectInqry(inqryId);
		cmsInqry.setInqryRefId(inqryId);
		cmsInqry.setInqryGroupOrd(1);
		cmsInqry.setInqryPwd(inqry.getInqryPwd());
		cmsInqry.setInqryMberId(principal.getName());

		logger.info(cmsInqry.toString());

		cmsInqryService.writeCmsInqry(cmsInqry);

		return "redirect:/cs/inqry";
	}

	@GetMapping("/inqry/update/{inqryId}")
	public String updateInqry(@PathVariable int inqryId, Model model, Principal principal) {
		CmsInqry cmsInqry = cmsInqryService.selectCmsInqry(inqryId);
		String userName = principal.getName();

		if (cmsInqry.getInqryMberId().equals(userName)) {
			model.addAttribute("cmsInqry", cmsInqry);
			return "cms/inqry/write";
		} else {
			model.addAttribute("message", "잘못된 접근입니다.");
			return "cms/inqry";
		}
	}
}
