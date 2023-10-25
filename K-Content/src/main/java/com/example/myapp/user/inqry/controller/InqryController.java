package com.example.myapp.user.inqry.controller;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
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

import com.example.myapp.cms.inqry.service.ICmsInqryService;
import com.example.myapp.commoncode.service.ICommonCodeService;
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

	@Autowired
	ICommonCodeService commonCodeSerivce;

	@Autowired
	ICmsInqryService cmsInqryService;

	@GetMapping("/inqury/{page}")
	public String selectInqryList(@PathVariable int page, HttpSession session, Model model) {
		session.removeAttribute("message");
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
	public String selectInqryList(HttpSession session) {
		return "user/inqury/main";
	}

	@PostMapping("/inqury/check-password")
	@ResponseBody
	public String checkPasswordAndSelectInqry(@RequestParam int inqryId, @RequestParam String enteredPwd, HttpSession session, Model model) {
		Inqry inqry = inqryService.selectInqry(inqryId);
		int inqryPwdId = (int) session.getAttribute("inqryPwdId");

		if (inqry.getInqryPwd().equals(enteredPwd)) {
			inqryPwdId = inqryId;
		} else {
	        session.setAttribute("message", "잘못된 비밀번호 입력");
	    }

		session.setAttribute("inqryPwdId", inqryPwdId);

		if (inqryPwdId > 0) {
			return "user/inqury/detail";
		} else {
			return "user/inqury/list";
		}
	}

	@RequestMapping("/inqury/detail/{inqryId}")
	public String selectInqry(@PathVariable int inqryId, Model model, HttpSession session) {
		int inqryPwdId = (int) session.getAttribute("inqryPwdId");

		if (inqryPwdId == inqryId || inqryService.selectInqry(inqryId).getInqryPwd() == null) {
			if (inqryService.selectInqry(inqryId).getInqryGroupOrd() == 1) {
				Inqry reply = inqryService.selectInqry(inqryId);
				Inqry inqry = inqryService.selectInqry(reply.getInqryRefId());
				model.addAttribute("reply", reply);
				model.addAttribute("inqry", inqry);
			} else if (inqryService.selectInqry(inqryId).getInqryGroupOrd() == 0) {
				Inqry inqry = inqryService.selectInqry(inqryId);
				model.addAttribute("inqry", inqry);
			}
		} else {
			return "redirect:/inqury";
		}
		return "user/inqury/detail";
	}
	
	@PreAuthorize("hasAnyRole('ADMIN', 'MBER')")
	@GetMapping("/inqury/insert")
	public String insertInqry(Model model) {
		Inqry inqry = new Inqry();
	    model.addAttribute("inqry", inqry);
		return "user/inqury/write";
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'MBER')")
	@PostMapping("/inqury/insert")
	public String insertInqry(Inqry inqry, BindingResult results, RedirectAttributes redirectAttrs, HttpSession session, Authentication authentication) {
		 UserDetails userDetails = (UserDetails)authentication.getPrincipal();
		 String userId = userDetails.getUsername();

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
			return "redirect:/inqury/detail/" + inqryId;
		} catch (Exception e) {
			e.printStackTrace();
			redirectAttrs.addFlashAttribute("message", e.getMessage());
			return "user/inqury/write";
		}
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'MBER')")
	@GetMapping(value="/inqury/update/{inqryId}")
	public String updateInqury(@PathVariable int inqryId, Model model, HttpSession session, Authentication authentication) {
		Inqry inqry = inqryService.selectInqry(inqryId);
		UserDetails userDetails = (UserDetails)authentication.getPrincipal();
		String userId = userDetails.getUsername();

		if (inqry.getInqryMberId().equals(userId)) {
			model.addAttribute("inqry", inqry);
			return "user/inqury/update";
		} else {
			return "redirect:/inqury";
		}
	}

	@PreAuthorize("isAuthenticated()")
	@PostMapping(value="/inqury/update/{inqryId}")
	public String updateInqury(@PathVariable int inqryId, Inqry inqry, RedirectAttributes redirectAttrs, Model model, HttpSession session) {

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
				if (cmsInqryService.countInqry(inqryId) > 1) {
					inqryService.updateCmsInqry(inqry);
				}
				mfile.transferTo(realPath);
			} else {
				inqryService.updateInqry(inqry);
				if (cmsInqryService.countInqry(inqryId) > 1) {
					inqryService.updateCmsInqry(inqry);
				}
			}
		} catch(Exception e){
			e.printStackTrace();
			redirectAttrs.addFlashAttribute("message", e.getMessage());
		}
		return "redirect:/inqury/detail/" + inqryId;
	};

	@PreAuthorize("hasAnyRole('ADMIN', 'MBER')")
	@PostMapping(value="inqury/delete/{inqryId}")
	public String deleteInqry(@PathVariable int inqryId, HttpSession session, RedirectAttributes model, Authentication authentication) {
		try {
				Inqry inqry = inqryService.selectInqry(inqryId);
				UserDetails userDetails = (UserDetails)authentication.getPrincipal();
				String loginId = userDetails.getUsername();

				if (loginId.equals(inqry.getInqryMberId())) {
					inqryService.deleteInqry(inqryId);
					return "redirect:/inqury";
				} else {
					model.addFlashAttribute("message", "잘못된 접근입니다.");
					return "redirect:/inqury/detail/" +  inqryId;
			}
		}catch(Exception e){
			model.addAttribute("message", e.getMessage());
			e.printStackTrace();
			return "error/runtime";
		}
	}
}
