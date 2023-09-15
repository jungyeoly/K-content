package com.example.myapp.user;

import java.security.Principal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.example.myapp.commoncode.service.ICommonCodeService;
import com.example.myapp.user.mber.model.MberUserDetails;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class MainCon {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	ICommonCodeService commonCodeService;


    @GetMapping("/")
    public String getCate(HttpServletRequest request, Model model, Authentication a) {
    	// 공통코드를 이용한 content의 카테고리 조회
    	List<String> cateList = commonCodeService.cateList("C03");
    	model.addAttribute("cateList", cateList);
    	
    	MberUserDetails b = (MberUserDetails) a.getPrincipal();
    	
    	logger.info(b.getMberBirth());
    	logger.info(b.getMberEmail());
    	logger.info(b.getMberGender());
    	logger.info(b.getmberPhone());
    	logger.info(b.getmberRegistDate());

      return "user/index";
    }
}
