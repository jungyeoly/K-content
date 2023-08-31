package com.example.myapp.user.inqry.controller;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.myapp.user.inqry.model.Inqry;
import com.example.myapp.user.inqry.model.InqryFile;
import com.example.myapp.user.inqry.service.IInqryService;

import jakarta.servlet.http.HttpSession;

@Controller
public class InqryController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Value("${part4.upload.path}")
	private String uploadPath;
	@Value("${img}")
	private String url;

	@Autowired
	IInqryService inqryService;

	@GetMapping("/inqury/{page}")
	public String selectInqryList(@PathVariable int page, HttpSession session, Model model) {
		int inqryPwdId = 0;
		session.setAttribute("inqryPwdId", inqryPwdId);
		session.setAttribute("page", page);

		List<Inqry> inqryList = inqryService.selectInqryList(page);
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

		session.setAttribute("nowPage", page);

		return "user/inqury/list";
	}

	@GetMapping("/inqury")
	public String selectInqryList(HttpSession session, Model model) {
		return selectInqryList(1, session, model);
	}	

	@PostMapping("/inqury/check-password")
	@ResponseBody
	public ResponseEntity<String> checkPasswordAndSelectInqry(@RequestParam int inqryId, @RequestParam int enteredPwd, HttpSession session, Model model) {
		Inqry inqry = inqryService.selectInqry(inqryId);
		int inqryPwdId = (int) session.getAttribute("inqryPwdId");

		if (inqry.getInqryPwd() == enteredPwd) {
			inqryPwdId = inqryId;
		}

		session.setAttribute("inqryPwdId", inqryPwdId);

		if (inqryPwdId > 0) {
			return new ResponseEntity<>("success", HttpStatus.OK);
		} else {
			return new ResponseEntity<>("fail", HttpStatus.OK);
		}
	}

	@RequestMapping("/inqury/detail/{inqryId}")
	public String selectInqry(@PathVariable int inqryId, Model model, HttpSession session) {
		int inqryPwdId = (int) session.getAttribute("inqryPwdId");

		if (inqryPwdId == inqryId) {
			Inqry inqry = inqryService.selectInqry(inqryId);
			model.addAttribute("inqry", inqry);
			return "user/inqury/detail";
		} else {
			return "redirect:/inqury";
		}
	}

	@GetMapping("/inqury/insert")
	public String insertInqry(Model model) {
		Inqry inqry = new Inqry();
	    model.addAttribute("inqry", inqry);
		return "user/inqury/write";
	}

	@PostMapping("/inqury/insert")
	public String insertInqry(Inqry inqry, BindingResult results, RedirectAttributes redirectAttrs, HttpSession session) {
		String userId = (String) session.getAttribute("userId");
		try {
			int inqryId = inqryService.selectinqryFileId();
			inqry.setInqryRefId(inqryId);
			inqry.setInqryMberId(userId);


			MultipartFile mfile = inqry.getFile();

			if (mfile != null && !mfile.isEmpty()) {
				InqryFile file = new InqryFile();
				String originalName = mfile.getOriginalFilename();
				String fileName = originalName.substring(originalName.lastIndexOf("\\") + 1);
				String fileExt = originalName.substring(originalName.lastIndexOf(".") + 1);
				String uuid = UUID.randomUUID().toString();
				String saveFileName = uuid + "_" + fileName;

				int inqryFileInqryId = inqryId;

				file.setInqryFileId(saveFileName);
				file.setInqryFileName(fileName);
				file.setInqryFileSize(mfile.getSize());
				file.setInqryFileExt(fileExt);
				file.setInqryFileInqryId(inqryFileInqryId);
				file.setInqryFilePath(url);

				Path path = Paths.get(uploadPath + url).toAbsolutePath().normalize();
				Path realPath = path.resolve(file.getInqryFileId()).normalize();


				inqryService.insertInqry(inqry, file);
				mfile.transferTo(realPath);

			} else {
				inqryService.insertInqry(inqry);
			}
			return "redirect:/inqury";
		} catch (Exception e) {
			e.printStackTrace();
			redirectAttrs.addFlashAttribute("message", e.getMessage());
			return "user/inqury/write";
		}
	}
	
	@GetMapping(value="/inqury/update/{inqryId}")
	public String updateInqury(@PathVariable int inqryId, Model model, HttpSession session) {
		Inqry inqry = inqryService.selectInqry(inqryId);

		if (inqry.getInqryMberId().equals((String) session.getAttribute("userId"))) {
			model.addAttribute("inqry", inqry);
			return "user/inqury/update";			
		} else {
			return "redirect:/inqury";
		}
	}
	
	@PostMapping(value="/inqury/update/{inqryId}")
	public String updateInqury(@PathVariable int inqryId, Inqry inqry, RedirectAttributes redirectAttrs, Model model, HttpSession session) {
		
		System.out.println("----------------------------------------------");
		System.out.println(inqry);
		System.out.println("--------------------------------------------------");
		
		try {
			MultipartFile mfile = inqry.getFile();
			
			if(mfile != null && !mfile.isEmpty()) {
		
				InqryFile file = new InqryFile();
				
				String originalName = mfile.getOriginalFilename();
				String fileName = originalName.substring(originalName.lastIndexOf("\\") + 1);
				String fileExt = originalName.substring(originalName.lastIndexOf(".") + 1);
				String uuid = UUID.randomUUID().toString();
				String saveFileName = uuid + "_" + fileName;
				
				int inqryFileInqryId = inqry.getInqryFileInqryId();
				
				file.setInqryFileId(saveFileName);
				file.setInqryFileName(fileName);
				file.setInqryFileName(fileName);
				file.setInqryFileSize(mfile.getSize());
				file.setInqryFileExt(fileExt);
				file.setInqryFileInqryId(inqryFileInqryId);
				file.setInqryFilePath(url);
				
				Path path = Paths.get(uploadPath + url).toAbsolutePath().normalize();
				Path realPath = path.resolve(file.getInqryFileId()).normalize();
				
				inqryService.updateInqry(inqry, file);
				mfile.transferTo(realPath);
			} else {
				inqryService.updateInqry(inqry);
			}
		} catch(Exception e){
			e.printStackTrace();
			redirectAttrs.addFlashAttribute("message", e.getMessage());
		}
		return "redirect:/inqury/detail/" + inqryId;
	}
}