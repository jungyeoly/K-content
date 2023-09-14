package com.example.myapp.user.mber.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.support.SessionStatus;

import com.example.myapp.commoncode.service.ICommonCodeService;
import com.example.myapp.user.mber.model.Mber;
import com.example.myapp.user.mber.service.IEmailService;
import com.example.myapp.user.mber.service.IMberService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
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

	@GetMapping("/modal")
	public String modal() {
		return "include/modal";
	}

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

		// 이메일 중복 체크
		Mber existingMber = mberService.selectMberbyEmail(mber.getMberEmail());
		if (existingMber != null) {
			model.addAttribute("mber", mber);
			model.addAttribute("existEmailMessage", "이미 존재하는 이메일입니다.");
			return "user/mber/signup";
		}
		mber.setMberStatCode("C0202");

		try {
			PasswordEncoder pwdEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
			String encodedPwd = pwdEncoder.encode(mber.getMberPwd());
			mber.setMberPwd(encodedPwd);
			mberService.insertMber(mber);
			logger.info("Saved: " + mber.getMberId());
		} catch (DuplicateKeyException e) {
			mber.setMberId(null);
			model.addAttribute("mber", mber);
			model.addAttribute("existIdMessage", "이미 존재하는 아이디입니다.");
			return "user/mber/signup";
		}
		session.invalidate();
		return "user/mber/signin";
	}

	@RequestMapping(value = "/mber/signin", method = RequestMethod.GET)
	public String signin(HttpServletRequest request, Model model) {

		return "user/mber/signin";
	}

	@GetMapping(value = "/mber/mypage")
	public String myPage() {
		return "user/mber/mypage";
	}

	@RequestMapping(value = "/mber/signout", method = RequestMethod.GET)
	public String signout(HttpServletRequest request, HttpServletResponse response, SessionStatus sessionStatus) {
		// Spring Security가 로그아웃 처리를 하므로 여기에서는 세션만 초기화하고 리다이렉트
		sessionStatus.setComplete();

//		// 쿠키 삭제
//		Cookie[] cookies = request.getCookies();
//		if (cookies != null) {
//			for (Cookie cookie : cookies) {
//				if (cookie.getName().equals("savedMberId")) {
//					cookie.setValue("");
//					cookie.setMaxAge(0);
//					cookie.setPath("/");
//					response.addCookie(cookie);
//				}
//			}
//		}

		return "redirect:/";
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

	@RequestMapping(value = "/mber/mailauth", method = RequestMethod.POST)
	@ResponseBody
	public String sendMailAuth(@RequestParam String mberEmail) throws Exception {
		String authNum = emailService.sendAuthNum(mberEmail);
		logger.info("인증코드 : " + authNum);
		return authNum;
	}

	@RequestMapping(value = "/mber/temppwd", method = RequestMethod.POST)
	@ResponseBody
	public String sendTempPwd(@RequestParam String mberId, @RequestParam String mberEmail, HttpServletResponse response)
			throws Exception {
		String tempPwd = "";
		Mber mber = mberService.selectMberbyIdEmail(mberId, mberEmail);

		// 회원 정보 업데이트
		if (mber != null) {
			tempPwd = emailService.sendTempPwd(mberEmail);
			PasswordEncoder pwdEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
			String encodedPwd = pwdEncoder.encode(tempPwd);
			mber.setMberPwd(encodedPwd);
			logger.info(encodedPwd);
			logger.info(mber.getMberPwd());
			mberService.updateMber(mber);
			// 임시 비밀번호 여부 생성
			Cookie tempPwdCookie = new Cookie("tempPwd", "Y"); // 재설정 페이지로 이동시켜주는 쿠키 "Y"로 설정
			tempPwdCookie.setMaxAge(86400); // 쿠키 유효 기간 설정
			tempPwdCookie.setPath("/"); // 쿠키 경로 설정
			response.addCookie(tempPwdCookie);

		}

		return tempPwd;
	}

	@GetMapping(value = "/mber/resetpwd")
	public String resetPwd() {
		return "user/mber/resetpwd";
	}

	@RequestMapping(value = "/mber/resetpwd", method = RequestMethod.POST)
	public String resetPwd(Model model, @RequestParam String mberPwd) {
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

	    Mber mber = mberService.selectMberbyId(authentication.getName());

		
		  if (mber != null) {
		        PasswordEncoder pwdEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
		        String encodedPwd = pwdEncoder.encode(mberPwd);
		        mber.setMberPwd(encodedPwd);
		        mberService.updateMber(mber);
		    }
		  
		return "redirect:/";
	}

}
