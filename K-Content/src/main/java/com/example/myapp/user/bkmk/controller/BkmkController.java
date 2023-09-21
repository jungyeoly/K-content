package com.example.myapp.user.bkmk.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.myapp.user.bkmk.model.CntntBkmk;
import com.example.myapp.user.bkmk.service.IBkmkService;


@Controller
public class BkmkController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	IBkmkService bkmkService;
	
	@GetMapping("/bkmk")
	public String selectBkmkList(Authentication authentication, Model model) {
		UserDetails userDetails = (UserDetails)authentication.getPrincipal();
		 String userId = userDetails.getUsername();
		 
		 List<CntntBkmk> selectCntntBkmkList = bkmkService.selectCntntBkmkList(userId);
		 model.addAttribute("cntntList", selectCntntBkmkList);
		 return "user/bkmk/main";
	}
}
