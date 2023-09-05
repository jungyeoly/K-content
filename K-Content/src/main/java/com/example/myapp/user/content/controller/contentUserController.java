package com.example.myapp.user.content.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.myapp.user.content.model.Content;
import com.example.myapp.user.content.service.IContentUserService;


@Controller
public class contentUserController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	IContentUserService contentService;
	
	@GetMapping("/user/content")
	public String selectUserContentList(@RequestParam(required = false, defaultValue = "All") String cate, Model model) {
		// 카테고리 별 조회
		List<Content> contentList = contentService.selectUserContent(cate);

		for(int i=0; i<contentList.size(); i++) {
			// 유튜뷰 영상 썸네일 추출
			List<String> contentUrlSplit = List.of(contentList.get(i).getCntntUrl().split("/"));
			String partOfUrl = contentUrlSplit.get(3);
			List<String> partOfUrl2 = List.of(partOfUrl.split("="));
			String resultCode = partOfUrl2.get(1);
			contentList.get(i).setCntntThumnail("https://i.ytimg.com/vi/"+resultCode+"/hqdefault.jpg");
		}
		model.addAttribute("contentList", contentList);
		
		return "user/content/list";
	}
}
