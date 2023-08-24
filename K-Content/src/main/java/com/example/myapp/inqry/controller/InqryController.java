package com.example.myapp.inqry.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.myapp.inqry.model.Inqry;
import com.example.myapp.inqry.service.IInqryService;

@Controller
public class InqryController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	IInqryService inqryService;
	
	@RequestMapping("/inqury")
	public String selectInqryList(Model model) {
		List<Inqry> inqryList = inqryService.selectInqryList();
		logger.info(inqryList.toString());
		model.addAttribute("inqryList", inqryList);
		return "inqury/list";
	}
}
