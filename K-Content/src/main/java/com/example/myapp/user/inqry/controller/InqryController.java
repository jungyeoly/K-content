package com.example.myapp.user.inqry.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.myapp.user.inqry.model.Inqry;
import com.example.myapp.user.inqry.service.IInqryService;

import jakarta.servlet.http.HttpSession;

@Controller
public class InqryController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	IInqryService inqryService;


	@GetMapping("/inqury/{page}")
	public String selectInqryList(@PathVariable int page, HttpSession session,Model model) {
		session.setAttribute("page", page);

		List<Inqry> inqryList = inqryService.selectInqryList(page);
		logger.info(inqryList.toString());
		model.addAttribute("inqryList", inqryList);

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

		return "inqury/list";
	}

	@GetMapping("/inqury")
	public String selectInqryList(HttpSession session, Model model) {
		return selectInqryList(1, session, model);

	}

	@PostMapping("/inqury/check-password")
	@ResponseBody
	public ResponseEntity<String> checkPasswordAndSelectInqry(@RequestParam int inqryId, @RequestParam int enteredPwd, HttpSession session, Model model) {
	Inqry inqry = inqryService.selectInqry(inqryId);


		if(inqry.getInqryPwd() == enteredPwd) {
	    	return new ResponseEntity<>("success", HttpStatus.OK);
	    } else {
	    	return new ResponseEntity<>("fail", HttpStatus.OK);
	    }


	}

	@RequestMapping("/inqury/detail/{inqryId}")
	public String selectInqry(@PathVariable int inqryId, Model model) {
		Inqry inqry = inqryService.selectInqry(inqryId);
		model.addAttribute("inqry", inqry);
		return "inqury/detail";
	}

	@GetMapping("/inqury/insert")
	public String insertInqry() {
		return "inqury/write";
	}
}
