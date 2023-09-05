package com.example.myapp.user.content.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.myapp.user.content.model.Content;
import com.example.myapp.user.content.service.IContentUserService;


@Controller
public class contentUserController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	IContentUserService contentService;
	
	@GetMapping("/user/content")
	public String selectUserContentList(Model model) {
		List<Content> contentList = contentService.selectUserContent();
		
		for(int i=0; i<contentList.size(); i++) {
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
