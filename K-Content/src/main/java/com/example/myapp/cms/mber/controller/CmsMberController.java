
package com.example.myapp.cms.mber.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.myapp.commoncode.service.ICommonCodeService;
import com.example.myapp.user.mber.model.Mber;
import com.example.myapp.user.mber.service.IMberService;

@Controller
public class CmsMberController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	IMberService mberService;

	@Autowired
	ICommonCodeService commonCodeService;

	@GetMapping("/cs/mber")
	public String mberManage(Model model) {
		int page = 1;
		int mberCount = mberService.getMberTotalCount();
		int totalPage = 0;

		if(mberCount > 0) {
			totalPage= (int)Math.ceil(mberCount/10.0);
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
		model.addAttribute("totalPage", totalPage);
		return "cms/mber/list";
	}

    @GetMapping("/cs/mber/list/{page}")
    @ResponseBody // HTML을 직접 반환
    public List<Mber> getMberList(@PathVariable int page, Model model) {
        List<Mber> mber = mberService.selectMberList(page);
        

		int mberCount = mberService.getMberTotalCount();
	
		int totalPage = 0;

		if(mberCount > 0) {
			totalPage= (int)Math.ceil(mberCount/10.0);
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

        return mber;
    }
    
	@PostMapping("/mber/changestat")
	@ResponseBody
	public Map<String, Object> changeMberStatus(@RequestParam String mberId, @RequestParam String newStatus) {
		Map<String, Object> response = new HashMap<>();
		try {
			
			mberService.changeMberStatus(mberId, newStatus);
			response.put("success", true);
		} catch (Exception e) {
			response.put("success", false);
			response.put("message", "상태 변경에 실패했습니다.");
		}
		return response;
	}

}
