package com.example.myapp.user.mber.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
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
			logger.info(encodedPwd);
			logger.info(mber.getMberPwd());
			mberService.updateMber(mber);

			session.setAttribute("isTempPwd", true);

		}

		return tempPwd;
	}

	@GetMapping(value = "/mber/resetpwd")
	public String resetPwd(HttpSession session) {
		boolean isTempPwd = session.getAttribute("isTempPwd") != null && (boolean) session.getAttribute("isTempPwd");

		if (isTempPwd) {
			// 임시 비밀번호가 생성된 경우에만 재설정 페이지로 이동
			return "user/mber/resetpwd";
		} else {
			// 임시 비밀번호가 생성되지 않은 경우 다른 페이지로 리다이렉트 또는 처리
			// 예: 오류 메시지 표시 등
			return "redirect:/";
		}
	}

	@RequestMapping(value = "/mber/resetpwd", method = RequestMethod.POST)
	public String resetPwd(Model model, @RequestParam String mberPwd, @RequestParam String currentMberPwd,
			HttpSession session) {
		boolean isTempPwd = session.getAttribute("isTempPwd") != null && (boolean) session.getAttribute("isTempPwd");
		if (isTempPwd) {
			// 비밀번호 재설정 로직을 수행
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			logger.info(authentication.getName());
			Mber mber = mberService.selectMberbyId(authentication.getName());

			PasswordEncoder pwdEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
			// 현재 비밀번호가 맞는지 확인
			if (pwdEncoder.matches(currentMberPwd, mber.getMberPwd())) {
				String encodedPwd = pwdEncoder.encode(mberPwd);
				mber.setMberPwd(encodedPwd);
				mberService.updateMber(mber);

				// 세션에서 임시 비밀번호 생성 여부 제거
				session.removeAttribute("isTempPwd");

				return "redirect:/";
			} else {
				// 현재 비밀번호가 일치하지 않는 경우 오류 메시지 처리 또는 리다이렉트
				// 예: 오류 메시지 표시 등
				return "redirect:/";
			}
		}
		// 현재 비밀번호가 일치하지 않는 경우 오류 메시지 처리 또는 리다이렉트
		// 예: 오류 메시지 표시 등
		return "redirect:/";
	}

	@GetMapping(value = "/mber/mypage")
	public String myPage(Model model, Authentication auth) {
		String currentMberId = auth.getName();

		Mber mber = mberService.selectMberbyId(currentMberId);
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

	@GetMapping(value = "/mber/editprofile")
	public String mberInfo(Model model, Authentication auth) {

		String currentMberId = auth.getName();

		Mber mber = mberService.selectMberbyId(currentMberId);

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

	// 회원 정보 수정 처리
	@PostMapping(value = "/mber/update")
	public String updateProfile(Model model, @ModelAttribute("mber") Mber updatedMber, Authentication auth) {
		// 현재 로그인한 회원의 아이디를 가져옵니다.
		String currentMberId = auth.getName();

		Mber mber = mberService.selectMberbyId(currentMberId);
		PasswordEncoder pwdEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
		String encodedPwd = pwdEncoder.encode(updatedMber.getMberPwd());
		mber.setMberPwd(encodedPwd);
		mber.setMberName(updatedMber.getMberName());
		mber.setMberEmail(updatedMber.getMberEmail());
		mber.setMberGenderCode(updatedMber.getMberGenderCode());
		mber.setMberBirth(updatedMber.getMberBirth());
		mber.setMberPhone(updatedMber.getMberPhone());

		mberService.updateMber(mber);

		return "redirect:/mber/mypage"; // 수정이 완료되면 마이페이지로 리다이렉트
	}

	@GetMapping(value = "/mber/deletember")
	public String deleteMber(Model model, Authentication auth) {
		return "user/mber/deletember";
	}

}
