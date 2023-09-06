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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.myapp.user.mber.model.Mber;
import com.example.myapp.user.mber.service.IEmailService;
import com.example.myapp.user.mber.service.IMberService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class MberController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	IMberService mberService;

	@Autowired
	IEmailService emailService;

	@GetMapping("/modal")
	public String modal() {
		return "include/modal";
	}

	@RequestMapping(value = "/mber/signin", method = RequestMethod.GET)
	public String signin(Model model) {
		return "user/mber/signin";
	}

	@RequestMapping(value = "/mber/signin", method = RequestMethod.POST)
	public String signin(String mberId, String mberPwd, @RequestParam(name = "saveId", required = false) String saveId,
						 HttpSession session, HttpServletResponse response, Model model) {
		Mber mber = mberService.selectMberbyId(mberId);
		if (mber != null) {
			if ("C0201".equals(mber.getMberStatCode())) {
				// 계정이 비활성화된 경우, 세션을 무효화하고 에러 메시지를 전달하고 뷰를 반환
				session.invalidate();
				model.addAttribute("message", "비활성화된 계정입니다. 관리자에게 문의하세요.");
				return "user/mber/signin";
			} else {
				logger.info(mber.toString());
				String dbPassword = mber.getMberPwd(); // DB에서 비밀번호 가져오기
				if (dbPassword.equals(mberPwd)) { // 비밀번호 일치
					// 로그인이 성공한 경우, 세션에 사용자 정보를 설정하고 뷰 반환
					session.setMaxInactiveInterval(600); // 10분
					session.setAttribute("mberId", mber.getMberId());
					session.setAttribute("mberName", mber.getMberName());
					session.setAttribute("mberState", mber.getMberStatCode());

					if (saveId != null && saveId.equals("on")) {
						// 체크박스가 선택된 경우, 아이디를 쿠키에 저장
						Cookie idCookie = new Cookie("savedMberId", mber.getMberId());
						idCookie.setMaxAge(30 * 24 * 60 * 60); // 쿠키 유효기간 설정 (30일)
						idCookie.setPath("/"); // 쿠키 경로 설정
						response.addCookie(idCookie);
					} else {
						// 체크박스가 선택되지 않은 경우, 아이디를 저장하는 쿠키 삭제
						Cookie idCookie = new Cookie("savedMberId", "");
						idCookie.setMaxAge(0); // 쿠키 삭제
						idCookie.setPath("/"); // 쿠키 경로 설정
						response.addCookie(idCookie);
					}
					model.addAttribute("mber", mber);
					return "redirect:/";
				} else { // 비밀번호가 다른 경우
					session.invalidate();
					model.addAttribute("message", "비밀번호가 다릅니다. 다시 확인해주세요.");
					return "user/mber/signin";
				}
			}
		} else { // 아이디가 존재하지 않는 경우
			session.invalidate();
			model.addAttribute("message", "존재하지 않는 아이디입니다. 다시 확인해주세요.");
			return "user/mber/signin";
		}
	}

	@RequestMapping(value = "/mber/signout", method = RequestMethod.GET)
	public String logout(HttpSession session) {
		session.invalidate(); // 로그아웃
		return "redirect:/";
	}

	@RequestMapping(value = "/mber/signup", method = RequestMethod.GET)
	public String signup(Model model) {
		return "user/mber/signup";
	}

	@RequestMapping(value = "/mber/signup", method = RequestMethod.POST)
	public String signup(Mber mber, HttpSession session, Model model) {

		try {
			mberService.insertMber(mber);
		} catch (DuplicateKeyException e) {
			model.addAttribute("existIdMessage", "이미 존재하는 아이디입니다.");
			return "user/mber/signup";
		}
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
		String maskId = emailService.sendMaskId(mberEmail);
		logger.info("마스킹된 아이디 이메일 전송 완료");
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
	public String sendTempPwd(@RequestParam String mberId, @RequestParam String mberEmail) throws Exception {

		Mber mber = mberService.selectMberbyIdEmail(mberId, mberEmail);

		String tempPwd = emailService.sendTempPwd(mberEmail);
		System.out.println(tempPwd);
		// 회원 정보 업데이트
		if (mber != null) {

			mber.setMberPwd(tempPwd);
			mberService.updateMber(mber);

		}

		return tempPwd;
	}
}
