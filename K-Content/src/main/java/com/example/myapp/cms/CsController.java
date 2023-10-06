package com.example.myapp.cms;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.myapp.cms.commu.model.CmsCommu;
import com.example.myapp.cms.commu.service.ICmsCommuService;
import com.example.myapp.cms.inqry.model.CmsInqry;
import com.example.myapp.cms.inqry.service.ICmsInqryService;

@Controller
@RequestMapping("/cs")
public class CsController {

	@Autowired
	ICmsCommuService cmsCommuService;
	
	@Autowired
	ICmsInqryService cmsInqryService;
	
	@GetMapping("/")
	public String csMain(Model model) {
		
		return "cms/index";
	}
	
	@GetMapping("/recent-notice")
	@ResponseBody

	public List<CmsCommu> commuList(Model model) {
		List<CmsCommu> cmsCommu = cmsCommuService.selectRecentNotice();
		
		return cmsCommu;
	}
	
	@GetMapping("/recent-inqry")
	@ResponseBody
	public List<CmsInqry> inqryList() {
		List<CmsInqry> inqry = cmsInqryService.selectRecentInqry();

		return inqry;
	}
}
