
package com.example.myapp.cms;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.myapp.cms.commu.model.CmsCommu;
import com.example.myapp.cms.commu.service.ICmsCommuService;

@Controller
public class CsController {

	@Autowired
	ICmsCommuService cmsCommuService;
	
	@GetMapping("/cs/test/")
	public String csMain(Model model) {
		
		return "cms/index";
	}
	
	@GetMapping("/cs/recent-notice")
	@ResponseBody
	public List<CmsCommu> commuList(Model model) {
		List<CmsCommu> cmsCommu = cmsCommuService.selectRecentNotice();
		
		return cmsCommu;
	}
}
