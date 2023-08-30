package com.example.myapp.commu.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.myapp.commu.model.Commu;
import com.example.myapp.commu.service.ICommuService;
import com.google.common.collect.Lists;

import jakarta.servlet.http.HttpSession;

@Controller
public class CommuController {
	static final Logger logger = LoggerFactory.getLogger(CommuController.class);

	@Autowired
	ICommuService commuService;

	@GetMapping("/commulist") // 커뮤니티 메인
	public String main(@RequestParam(defaultValue = "1") int currentPage, @ModelAttribute("commu") Commu commu,
			Model model, HttpSession session) {
		System.out.println(currentPage);

		List<Commu> commulist = commuService.selectAllPost();

		int totalPage = 0;
		int totalCommu = 0;

		// 검색결과가 있는 경우 paging처리
		if (commulist != null && !commulist.isEmpty()) {

			totalCommu = commulist.size();
			System.out.println(totalCommu);
			int partitionSize = 10;
			List<List<Commu>> partitionedList = Lists.partition(commulist, partitionSize);
			totalPage = partitionedList.size();
			commulist = partitionedList.get(currentPage - 1);
		}

		model.addAttribute("totalPage", totalPage);
		model.addAttribute("currentPage", currentPage);

		model.addAttribute("totalCommu", totalCommu);
		model.addAttribute("commulist", commulist);
		return "commu/commulist";
	}
	
	//커뮤니티 게시글 제목 누르면 상세보기
	@GetMapping("/commu/{commuId}")
	public String getCommuDetails(@PathVariable int commuId, Model model) {
	    Commu commu = commuService.selectPost(commuId);
	    model.addAttribute("commuview", commu);
	    logger.info("getCommuDetails" + commu.toString());
	    return "commu/commuview";
	}
	
	//커뮤니티 게시글 상세보기
	@GetMapping("/commu/{commuId}/{page}")
	public String getCommuDetails(@PathVariable int commuId,@PathVariable int page, Model model) {
		Commu commu = commuService.selectPost(commuId);
		model.addAttribute("commuview", commu);
		model.addAttribute("page",page);
		model.addAttribute("commuId", commu.getCommuId());
		logger.info("getCommuDetails" + commu.toString());
		return "commu/commuview";
	}
	

	/*
	 * 페이징연습
	 * 
	 * @GetMapping("/paging") public String paging(
	 * 
	 * @RequestParam(defaultValue = "1") int currentPage,
	 * 
	 * @ModelAttribute("searchContent") LogVO searchContent, Model model,
	 * HttpSession session ) {
	 * 
	 * log.info("currentPage={}, searchContent={}", currentPage, searchContent);
	 * 
	 * // List<LogVO> logs = logService.getAllLogs(); List<LogVO> logs =
	 * logService.getRetrievedLogs(searchContent); int totalPage = 0; int totalLog =
	 * 0;
	 * 
	 * // 검색결과가 있는 경우 paging처리 if (logs != null && !logs.isEmpty()) {
	 * log.info("검색된 첫번째 로그 logs={}", logs.get(0)); totalLog = logs.size(); int
	 * partitionSize = 10; List<List<LogVO>> partitionedList = Lists.partition(logs,
	 * partitionSize); totalPage = partitionedList.size(); logs =
	 * partitionedList.get(currentPage-1); } else { log.info("logs={}", logs); }
	 * 
	 * model.addAttribute("totalPage", totalPage); model.addAttribute("currentPage",
	 * currentPage);
	 * 
	 * model.addAttribute("totalLog", totalLog); model.addAttribute("logs", logs);
	 * 
	 * return "log/loglist2"; }
	 */

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