package com.example.myapp.cms.inqry.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.myapp.cms.inqry.model.CmsInqry;
import com.example.myapp.cms.inqry.service.ICmsInqryService;

@Controller
@RequestMapping("/cs")
public class CmsInqryController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	ICmsInqryService cmsInqrySerivce;
	
	@GetMapping("/inqry")
	public String selectInqryList(Model model) {
		List<CmsInqry> inqryList = cmsInqrySerivce.selectCmsInqryList();
		
		model.addAttribute("iqnryList", inqryList);
		
		return "cms/inqry/list";
		
	}
}
