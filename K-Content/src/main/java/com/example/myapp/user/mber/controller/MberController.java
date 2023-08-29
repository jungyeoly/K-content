package com.example.myapp.user.mber.controller;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.myapp.user.mber.model.Mber;
import com.example.myapp.user.mber.service.IMberService;

import jakarta.servlet.http.HttpSession;

@Controller
public class MberController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	IMberService mberService;

//    private final EmailService emailService;


	@GetMapping("/modal")
	public String modal() {
		return "include/modal";
	}

	@GetMapping("/mber/list")
	public String mberList(Model model) {
		List<Mber> mber = mberService.selectMberAllList();
		model.addAttribute("mber", mber);
		return "mber/list";
	}

	@RequestMapping(value = "/mber/signin", method = RequestMethod.GET)
	public String signin(Model model) {
		return "mber/signin";
	}

	@RequestMapping(value = "/mber/signin", method = RequestMethod.POST)
	public String signin(String mberId, String mberPwd, HttpSession session, Model model) {
		Mber mber = mberService.selectMberbyId(mberId);
		if (mber != null) {
			if (mber.getMberStatCode() == "C0201") {
				// 계정이 비활성화된 경우, 세션을 무효화하고 에러 메시지를 전달하고 뷰를 반환
				session.invalidate();
				model.addAttribute("message", "비활성화된 계정입니다. 관리자에게 문의하세요.");
				return "mber/signin";
			} else {
				logger.info(mber.toString());
				String dbPassword = mber.getMberPwd(); // DB에서 비밀번호 가져오기
				if (dbPassword.equals(mberPwd)) { // 비밀번호 일치
					// 로그인이 성공한 경우, 세션에 사용자 정보를 설정하고 뷰 반환
					session.setMaxInactiveInterval(600); // 10분
					session.setAttribute("userId", mber.getMberId());
					session.setAttribute("userName", mber.getMberName());
					session.setAttribute("userState", mber.getMberStatCode());
					model.addAttribute("mber", mber);
					return "redirect:/mber/list";
				} else { // 비밀번호가 다른 경우
					session.invalidate();
					model.addAttribute("message", "비밀번호가 다릅니다. 다시 확인해주세요.");
					return "user/signin";
				}
			}
		} else { // 아이디가 존재하지 않는 경우
			session.invalidate();
			model.addAttribute("message", "존재하지 않는 아이디입니다. 다시 확인해주세요.");
			return "mber/signin";
		}
	}

	@RequestMapping(value = "/mber/signup", method = RequestMethod.GET)
	public String signup(Model model) {
		return "mber/signup";
	}

	@RequestMapping(value = "/mber/signup", method = RequestMethod.POST)
	public String signup(Mber mber, HttpSession session, Model model) {
		System.out.println(mber.getMberId());
		System.out.println(mber.getMberEmail());
		System.out.println(mber.getMberName());
		System.out.println(mber.getMberBirth());
		System.out.println(mber.getMberPhone());
		System.out.println(mber.getMberRegistDate());
		System.out.println(mber.getMberUpdateDate());
		System.out.println(mber.getMberGenderCode());
		System.out.println(mber.getMberStatCode());
		try {
			mberService.insertMber(mber);
		} catch (DuplicateKeyException e) {
			model.addAttribute("existIdMessage", "이미 존재하는 아이디입니다.");
			return "mber/signup";
		}
		return "mber/signin";
	}


	/*
	 * @RequestMapping(value = "/mber/mailauth", method = RequestMethod.POST)
	 *
	 * @ResponseBody public String mailConfirm(@RequestParam String email) throws
	 * Exception { String code = emailService.sendSimpleMessage(email);
	 * log.info("인증코드 : " + code); return code; }
	 */
}
