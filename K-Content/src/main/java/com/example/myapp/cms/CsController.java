
package com.example.myapp.cms;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.myapp.user.commu.model.Commu;
import com.example.myapp.user.commu.service.ICommuService;
import com.example.myapp.user.mber.model.Mber;

@Controller
public class CsController {

	@Autowired
	ICommuService commuService;
	
	@GetMapping("/cs/test/")
	public String csMain(Model model) {
		
		return "cms/index";
	}
	
	@GetMapping("/cs/recent-notice")
	@ResponseBody
	public List<Commu> commuList(Model model) {
		List<Commu> commu = commuService.selectRecentNotice();
		
		return commu;
	}
}
