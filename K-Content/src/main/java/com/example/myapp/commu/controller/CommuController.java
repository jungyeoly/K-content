package com.example.myapp.commu.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.myapp.commu.model.Commu;
import com.example.myapp.commu.service.ICommuService;


@Controller
public class CommuController {
	static final Logger logger = LoggerFactory.getLogger(CommuController.class);
	
	@Autowired
	ICommuService commuService;
	
	@GetMapping("/commuList")  //커뮤니티 메인
	public String main(Model model) {
		List<Commu> commuList = commuService.selectAllPost();
		model.addAttribute("commuList", commuList);
		return "user/commuList";
	}
	
	/*
	 * @GetMapping("/commuView") public String commuView(Model model, commu id) {
	 * model.addAttribute("commuview",commuService.commuView(id)); return
	 * "/user/commuView";
	 * 
	 * }
	 */
	/*
	 * @GetMapping("/commu/write") public String writePost(Model model, HttpSession
	 * session) { return "user/write"; }
	 */
	
	
		/*
		 * @PostMapping("/commu/write") //커뮤니티 글쓰기 public String writePost(Commu commu,
		 * BindingResult result, RedirectAttributes redirectAttrs, HttpSession session)
		 * { logger.info("/commu/write : " + commu.toString());
		 * commu.setCommuMberId((String) session.getAttribute("mberId")); try {
		 * commu.setCommuCntnt(commu.getCommuCntnt().replace("\r\n", "<br>"));
		 * commu.setCommuTitle(Jsoup.clean(commu.getCommuTitle(), Safelist.basic()));
		 * commu.setCommuCntnt(Jsoup.clean(commu.getCommuCntnt(), Safelist.basic()));
		 * List<CommuImage> fileList = new ArrayList<CommuImage>();
		 * 
		 * for(MultipartFile m : CommuFile.getFile()) { MultipartFile mfile=m; if(mfile
		 * ! = null && !mfile.isEmpty()) { CommuImage file = new CommuImage();
		 * file.setCommu } }
		 */
			}