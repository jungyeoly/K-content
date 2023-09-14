package com.example.myapp.user;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.example.myapp.commoncode.service.ICommonCodeService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class MainCon {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	ICommonCodeService commonCodeService;


    @GetMapping("/")
    public String getCate(HttpServletRequest request, Model model) {
    	// 공통코드를 이용한 content의 카테고리 조회
    	List<String> cateList = commonCodeService.cateList("C03");
    	model.addAttribute("cateList", cateList);
        // 여기서 쿠키를 확인하고, "tempPwd" 쿠키가 있는지 여부를 검사합니다.
	    boolean isTempPwd = false;
	    Cookie[] cookies = request.getCookies();
	    if (cookies != null) {
	        for (Cookie cookie : cookies) {
	            if ("tempPwd".equals(cookie.getName()) && "N".equals(cookie.getValue())) {
	                isTempPwd = true;
	                break;
	            }
	        }
	    }

	    if (isTempPwd) {
	        // 임시 비밀번호로 로그인한 경우
	        model.addAttribute("tempPwdMessage", "임시 비밀번호로 로그인했습니다. 비밀번호를 변경하세요.");
	        return "user/mber/resetpwd";
	    }
      return "user/index";
    }
}
