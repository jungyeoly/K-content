package com.example.myapp.user.mber.controller;

import org.apache.ibatis.javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.myapp.commoncode.service.ICommonCodeService;
import com.example.myapp.user.mber.model.Mber;
import com.example.myapp.user.mber.service.IEmailService;
import com.example.myapp.user.mber.service.IMberService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class MberController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	IMberService mberService;

	@Autowired
	IEmailService emailService;

	@Autowired
	ICommonCodeService commonCodeService;

	@Autowired
	PasswordEncoder passwordEncoder;

	@RequestMapping(value = "/mber/signup", method = RequestMethod.GET)
	public String signup(Model model) {
		return "user/mber/signup";
	}

	@RequestMapping(value = "/mber/signup", method = RequestMethod.POST)
	public String signup(Mber mber, HttpSession session, Model model) {
//		String sessionToken = (String) session.getAttribute("csrfToken");
//		if(CsrfToken==null || !CsrfToken.equals(sessionToken)) {
//			throw new RuntimeException("CSRF Token Error.");
//		}
		try {
			PasswordEncoder pwdEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
			String encodedPwd = pwdEncoder.encode(mber.getMberPwd());
			mber.setMberPwd(encodedPwd);
			logger.info(mber.toString());
			mberService.insertMber(mber);
			logger.info("회원가입 성공: " + mber.getMberId());
		} catch (DuplicateKeyException e) {
			mber.setMberId(null);
			model.addAttribute("mber", mber);
			return "user/mber/signup";
		}
		session.invalidate();
		return "user/mber/signin";
	}

	@PreAuthorize("isAnonymous()")
	@RequestMapping(value = "/mber/signin", method = RequestMethod.GET)
	public String signin(@RequestParam(value = "error", required = false) String error,
			@RequestParam(value = "exception", required = false) String exception, Model model) {
		  
		model.addAttribute("error", error);
		model.addAttribute("exception", exception);
		return "user/mber/signin";
	}

	@GetMapping("/mber/findmber")
	public String findMber(@RequestParam(name = "findType", required = false, defaultValue = "id") String findType,
			Model model) {
		model.addAttribute("findType", findType);
		return "user/mber/findmber";
	}

	@RequestMapping(value = "/mber/findid", method = RequestMethod.POST)
	@ResponseBody
	public String findId(@RequestParam String mberEmail) throws Exception {
		Mber mber = mberService.selectMberbyEmail(mberEmail);
		String maskId = "";

		if (mber != null) {
			maskId = emailService.sendMaskId(mberEmail);
			logger.info("마스킹된 아이디 이메일 전송 완료");
		}

		return maskId;
	}

	@RequestMapping(value = "/mber/emailauth", method = RequestMethod.POST)
	@ResponseBody
	public String sendEmailAuth(@RequestParam String mberEmail, HttpSession session) throws Exception {
		String authNum = emailService.sendAuthNum(mberEmail);
		session.setAttribute("authNum", authNum);

		return authNum;
	}

	@PostMapping(value = "/mber/checkauthnum")
	@ResponseBody
	public boolean checkAuthNum(@RequestParam String enteredAuthNum, HttpSession session) {
		String savedAuthNum = (String) session.getAttribute("authNum");

		boolean isAuthCodeValid = savedAuthNum != null && savedAuthNum.equals(enteredAuthNum);

		return isAuthCodeValid;
	}

	@RequestMapping(value = "/mber/temppwd", method = RequestMethod.POST)
	@ResponseBody
	public String sendTempPwd(@RequestParam String mberId, @RequestParam String mberEmail, HttpServletResponse response,
			HttpSession session) throws Exception {
		String tempPwd = "";
		Mber mber = mberService.selectMberbyIdEmail(mberId, mberEmail);

		// 회원 정보 업데이트
		if (mber != null) {
			tempPwd = emailService.sendTempPwd(mberEmail);
			PasswordEncoder pwdEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
			String encodedPwd = pwdEncoder.encode(tempPwd);
			mber.setMberPwd(encodedPwd);

			if(mber.getMberBirth()== null) {
				mber.setMberBirth("");				
			}
			if(mber.getMberPhone()== null) {
				mber.setMberPhone("");
			}
			
			mberService.updateMber(mber);

			session.setAttribute("isTempPwd", true);

		}

		return tempPwd;
	}

	@GetMapping(value = "/mber/resetpwd")
	public String resetPwd(HttpSession session) {
		boolean isTempPwd = session.getAttribute("isTempPwd") != null && (boolean) session.getAttribute("isTempPwd");

		if (isTempPwd) {
			return "user/mber/resetpwd";
		} else {
			return "redirect:/";
		}
	}

	@RequestMapping(value = "/mber/resetpwd", method = RequestMethod.POST)
	public String resetPwd(Model model, @RequestParam String mberPwd, @RequestParam String currentMberPwd,
			HttpSession session) {
		boolean isTempPwd = session.getAttribute("isTempPwd") != null && (boolean) session.getAttribute("isTempPwd");
		if (isTempPwd) {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			logger.info(authentication.getName());
			Mber mber = mberService.selectMberbyId(authentication.getName());

			PasswordEncoder pwdEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
			String encodedPwd = pwdEncoder.encode(mberPwd);
			mber.setMberPwd(encodedPwd);

			if(mber.getMberBirth()== null) {
				mber.setMberBirth("");				
			}
			if(mber.getMberPhone()== null) {
				mber.setMberPhone("");
			}
			mberService.updateMber(mber);

			session.removeAttribute("isTempPwd");

			return "redirect:/";
		}
		model.addAttribute("message", "비밀번호가 일치하지 않습니다.");
		return "/mber/resetpwd";
	}

	@PreAuthorize("isAuthenticated()")
	@GetMapping(value = "/mber/mypage")
	public String myPage(Model model, Authentication auth, HttpSession session) {
		String currentMberId = auth.getName();

		Mber mber = mberService.selectMberbyId(currentMberId);

		if (mber == null) {
			return "redirect:/mber/signin";
		}

		// 검증이 실패한 경우 다른 페이지로 리다이렉트
		if (session.getAttribute("pwdVerificationSuccess") != "Y") {
			session.removeAttribute("pwdVerificationSuccess"); // 플래그 제거
			model.addAttribute("message", "비밀번호 확인이 필요합니다.");
			return "redirect:/mber/verifypwd"; // 또는 다른 페이지로 리다이렉트
		}

		model.addAttribute(mber);
		boolean isAdmin = false;
		for (GrantedAuthority authority : auth.getAuthorities()) {
			if (authority.getAuthority().equals("ROLE_ADMIN")) {
				isAdmin = true;
				break;
			}
		}
		model.addAttribute("isAdmin", isAdmin);
		return "user/mber/mypage";
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping(value = "/mber/editprofile")
	public String mberInfo(Model model, Authentication auth) {

		String currentMberId = auth.getName();

		Mber mber = mberService.selectMberbyId(currentMberId);

		if (mber == null) {
			return "redirect:/mber/signin";
		}
		model.addAttribute(mber);

		boolean isAdmin = false;
		for (GrantedAuthority authority : auth.getAuthorities()) {
			if (authority.getAuthority().equals("ROLE_ADMIN")) {
				isAdmin = true;
				break;
			}
		}
		model.addAttribute("isAdmin", isAdmin);
		return "user/mber/editprofile";
	}

	@PostMapping(value = "/mber/update")
	public String updateProfile(Model model, @ModelAttribute("mber") Mber updatedMber, Authentication auth) {
		String currentMberId = auth.getName();

		Mber mber = mberService.selectMberbyId(currentMberId);
		PasswordEncoder pwdEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
		String encodedPwd = pwdEncoder.encode(updatedMber.getMberPwd());
		mber.setMberPwd(encodedPwd);
		mber.setMberName(updatedMber.getMberName());
		mber.setMberGenderCode(updatedMber.getMberGenderCode());
		mber.setMberBirth(updatedMber.getMberBirth());
		mber.setMberPhone(updatedMber.getMberPhone());

		mberService.updateMber(mber);

		return "redirect:/mber/mypage";
	}

	@PreAuthorize("isAuthenticated()")
	@GetMapping(value = "/mber/verifypwd")
	public String verifyPwd(Model model, Authentication auth) {
		String currentMberId = auth.getName();
		Mber mber = mberService.selectMberbyId(currentMberId);

		if (mber == null) {

			return "redirect:/mber/signin";
		}
		return "user/mber/verifypwd";
	}

	@PostMapping(value = "/mber/verifypwd")
	public String verifyPwd(String mberPwd, Model model, Authentication auth, HttpSession session) {
		String currentMberId = auth.getName();
		Mber mber = mberService.selectMberbyId(currentMberId);

		if (mber == null) {
			model.addAttribute("message", "인증 정보가 없습니다. 다시 로그인해주세요.");
			session.setAttribute("pwdVerificationSuccess", "N");
			return "redirect:/mber/signin";
		} else if (passwordEncoder.matches(mberPwd, mber.getMberPwd())) {
			session.setAttribute("pwdVerificationSuccess", "Y");
			return "redirect:/mber/mypage";
		}
		session.setAttribute("pwdVerificationSuccess", "N");
		model.addAttribute("message", "비밀번호가 일치하지 않습니다. ");
		return "user/mber/verifypwd";
	}

	@GetMapping(value = "/mber/deletember")
	public String deleteMber(Model model, HttpSession session, Authentication auth) {
		String currentMberId = auth.getName();
		Mber mber = mberService.selectMberbyId(currentMberId);
		if (mber == null) {
			model.addAttribute("exception", "계정 정보가 없습니다. 로그인 해주세요.");
			return "redirect:/mber/signin";
		} else if (session.getAttribute("pwdVerificationSuccess") != "Y") {
			session.removeAttribute("pwdVerificationSuccess");
			model.addAttribute("message", "비밀번호 확인이 필요합니다.");
			return "user/mber/verifypwd";
		}

		return "user/mber/deletember";
	}

	@PostMapping(value = "/mber/deletember")
	public String deleteMber(String mberPwd, HttpSession session, Model model, Authentication auth)
			throws NotFoundException {
		String currentMberId = auth.getName();
		Mber mber = mberService.selectMberbyId(currentMberId);
		if (mber == null) {
			model.addAttribute("message", "인증 정보가 없습니다. 다시 로그인해주세요.");
			return "redirect:/mber/signin";
		} else if (passwordEncoder.matches(mberPwd, mber.getMberPwd())) {
			mberService.deleteMber(currentMberId);
			SecurityContextHolder.clearContext();
			session.invalidate(); // 세션 무효화
			return "redirect:/mber/signin";
		} else {
			model.addAttribute("message", "비밀번호가 일치하지 않습니다.");
		}

		return "user/mber/deletember";
	}

	// ID 유효성 검증
	@RequestMapping(value = "/mber/checkid", method = RequestMethod.POST)
	@ResponseBody
	public boolean checkId(String mberId) {

		boolean check = mberService.isMberId(mberId);

		return check;
	}

	@RequestMapping(value = "/mber/checkpwd", method = RequestMethod.POST)
	@ResponseBody
	public boolean checkPwd(String mberPwd) {

		String checkPwd = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,16}$";
		return mberPwd.matches(checkPwd);
	}

	@RequestMapping(value = "/mber/checkname", method = RequestMethod.POST)
	@ResponseBody
	public boolean checkName(String mberName) {

		String nameRegExp = "^[a-zA-Z가-힣\\s]+$";
		return mberName.matches(nameRegExp);
	}

	@RequestMapping(value = "/mber/checkemail", method = RequestMethod.POST)
	@ResponseBody
	public boolean checkEmail(String mberEmail) {

		boolean check = mberService.isMberEmail(mberEmail);

		return check;
	}

	@RequestMapping(value = "/mber/checkphone", method = RequestMethod.POST)
	@ResponseBody
	public boolean checkPhone(String mberPhone) {

		String phoneRegExp = "^01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$";
		return mberPhone.matches(phoneRegExp);
	}

	@RequestMapping(value = "/mber/checkdbpwd", method = RequestMethod.POST)
	@ResponseBody
	public boolean checkDBPwd(String mberPwd, Authentication authentication) {

		String currentMberId = authentication.getName();
		Mber mber = mberService.selectMberbyId(currentMberId);

		return passwordEncoder.matches(mberPwd, mber.getMberPwd());

	}

}
