package com.example.myapp.cms.inqry.controller;

import java.security.Principal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
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

	@Autowired
	ICmsInqryService cmsInqryService;

	@GetMapping("/inqry/{page}")
	public String selectCmsInqryList(@PathVariable int page, HttpSession session, Model model) {
		session.setAttribute("page", page);
		
		List<Inqry> inqryList = inqryService.selectInqryList(page);
		model.addAttribute("inqryList", inqryList);
		
		List<CmsInqry> unansList = cmsInqryService.selectUnansInqryList(page);
		model.addAttribute("unansList", unansList);
		
		return "cms/inqry/list";
	}
	
	@GetMapping("/inqry/paging")
	public String inqryPaging(@RequestParam(defaultValue = "1") int page, @RequestParam String activeTab, Model model) {
		if (activeTab.equals("menu1")) {
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
			return "cms/inqry/paging";

		} else {
			int bbsCount = cmsInqryService.countAns();
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
			return "cms/inqry/paging";
		}
	}

	@GetMapping("/inqry")
	public String selectInqryList(HttpSession session, Model model,  @RequestParam(required = false) Integer inqryId) {
		
		if (inqryId != null) {
			model.addAttribute("unInqryId", inqryId);			
		}
		
		return "cms/inqry/main";
	}

	@RequestMapping("/inqry/detail/{inqryId}")
	public String selectCmsInqry(@PathVariable int inqryId, Model model, HttpSession session) {
		if (inqryService.selectInqry(inqryId).getInqryGroupOrd() == 1) {
			Inqry reply = inqryService.selectInqry(inqryId);
			Inqry inqry = inqryService.selectInqry(reply.getInqryRefId());
			model.addAttribute("reply", reply);
			model.addAttribute("inqry", inqry);
		} else if (inqryService.selectInqry(inqryId).getInqryGroupOrd() == 0) {
			Inqry inqry = inqryService.selectInqry(inqryId);
			model.addAttribute("inqry", inqry);
		}
		int cnt = cmsInqryService.countInqry(inqryId);
		model.addAttribute("cnt", cnt);
		session.setAttribute("inqryId", inqryId);
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
		cmsInqry.setInqryTitle(inqry.getInqryTitle());
		cmsInqry.setInqryRefId(inqryId);
		cmsInqry.setInqryGroupOrd(1);
		
		String pwd = inqry.getInqryPwd();
		if(pwd == null || pwd == "") {
			cmsInqry.setInqryPwd("");
		} else {
			cmsInqry.setInqryPwd(pwd);			
		}
		cmsInqry.setInqryMberId(principal.getName());
		cmsInqryService.writeCmsInqry(cmsInqry);

		return "redirect:/cs/inqry";
	}

	@GetMapping("/inqry/update/{inqryId}")
	public String updateInqry(@PathVariable int inqryId, Model model, Principal principal, Authentication authentication) {
		CmsInqry cmsInqry = cmsInqryService.selectCmsInqry(inqryId);
		String re = cmsInqryService.getCmsInqry(cmsInqry.getInqryRefId());
		if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
			model.addAttribute("inqry", cmsInqry);
			model.addAttribute("re", re);
			return "cms/inqry/update";
		} else {
			model.addAttribute("message", "잘못된 접근입니다."); 
			return "redirect:/cs/inqry";
		}
	}
	
	@PostMapping("/inqry/update/{inqryId}")
	public String updateInqry(@PathVariable int inqryId, CmsInqry cmsInqry) {
		cmsInqryService.updateCmsInqry(cmsInqry);
		
		return "redirect:/cs/inqry";
	}
	
	@PostMapping("/inqry/delete/{inqryId}")
	public String deleteInqry(@PathVariable int inqryId, Authentication authentication, Principal principal, RedirectAttributes redirectAttrs) {
		if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
			cmsInqryService.deleteCmsInqry(inqryId);
		}
		return "redirect:/cs/inqry";
	}

}