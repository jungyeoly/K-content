package com.example.myapp.user.commu.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.example.myapp.user.commu.service.ICommuService;

import org.apache.commons.io.FilenameUtils;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.myapp.commoncode.model.CommonCode;
import com.example.myapp.commoncode.service.CommonCodeService;
import com.example.myapp.user.commu.model.Commu;
import com.example.myapp.user.commu.model.CommuFile;
import com.google.common.collect.Lists;

import jakarta.servlet.http.HttpSession;

@Controller
public class CommuController {
	static final Logger logger = LoggerFactory.getLogger(CommuController.class);

	@Autowired
	ICommuService commuService;

	@Autowired
	private CommonCodeService commonCodeService;

	@GetMapping("/list") // 커뮤니티 메인
	public String main(@RequestParam(defaultValue = "1") int currentPage, @ModelAttribute("commu") Commu commu,
			Model model, HttpSession session) {

		model.addAttribute("showAll", true); // "BEST" 대신 "ALL" 표시

		System.out.println(currentPage);

		List<Commu> commulist = commuService.selectAllPost();

		int totalPage = 0;
		int totalCommu = 0;

		// 검색결과가 있는 경우 paging처리
		if (commulist != null && !commulist.isEmpty()) {

			totalCommu = commulist.size();
			System.out.println(totalCommu);
			int partitionSize = 10;
			List<List<Commu>> partitionedList = Lists.partition(commulist, partitionSize);
			totalPage = partitionedList.size();
			commulist = partitionedList.get(currentPage - 1);
		}

		model.addAttribute("totalPage", totalPage);
		model.addAttribute("currentPage", currentPage);

		model.addAttribute("totalCommu", totalCommu);
		model.addAttribute("commulist", commulist);
		return "user/commu/list";
	}

	// 커뮤니티 게시글 제목 누르면 상세보기
	@GetMapping("/commu/{commuId}")
	public String getCommuDetails(@PathVariable int commuId, Model model) {
		Commu commu = commuService.selectPost(commuId);
		model.addAttribute("commuview", commu);
		logger.info("getCommuDetails" + commu.toString());
		return "user/commu/view";
	}

	// 커뮤니티 게시글 글번호,카테고리에 따른 게시글 상세보기
	@GetMapping("/commu/{commuId}/{commuCateCode}")
	public String getCommuDetails(@PathVariable int commuId, @PathVariable String commuCateCode, Model model) {
		Commu commu = commuService.selectPost(commuId);
		model.addAttribute("commuview", commu);
		model.addAttribute("commuCateCode", commuCateCode);
		model.addAttribute("commuId", commu.getCommuId());
		logger.info("getCommuDetails" + commu.toString());
		return "user/commu/view";
	}

	// 카테고리별 커뮤니티 글쓰기
	@GetMapping("/commu/commuwrite")
	public String writePost(Model model) {
		model.addAttribute("showAll", true);
		List<CommonCode> commuCateCodeList = commonCodeService.findCommonCateCodeByUpperCommonCode("C03");
		model.addAttribute("commuCateCodeList", commuCateCodeList);
		logger.info("Fetched commuCateCodeList: " + commuCateCodeList);// 실제 데이터 확인

		return "user/commu/write";
	}

	// 커뮤니티 글쓰기 및 파일 업로드를 처리하는 메서드
	@PostMapping("/commu/commuwrite")
	public String writePostAndUploadFiles(Commu commu,
			@RequestParam("commuUploadFiles") MultipartFile[] commuUploadFiles, BindingResult results,
			RedirectAttributes redirectAttrs) {

		// 게시물 정보 로깅
		logger.info("/user/commu/write : " + commu.toString());

		try {
			// 게시물 내용에서 줄 바꿈을 HTML 태그로 변경
			commu.setCommuCntnt(commu.getCommuCntnt().replace("\r\n", "<br>"));
			// 게시물 제목과 내용에 대해 HTML 태그를 제거 (XSS 방지)
			commu.setCommuTitle(Jsoup.clean(commu.getCommuTitle(), Safelist.basic()));
			commu.setCommuCntnt(Jsoup.clean(commu.getCommuCntnt(), Safelist.basic()));

			// 첨부파일 리스트 초기화
			List<CommuFile> commuFiles = commu.getCommuFiles();
			if (commuFiles == null) {
			    commuFiles = new ArrayList<>();
			    commu.setCommuFiles(commuFiles);  
			}


			// 첨부 파일이 있을 경우 처리
			if (commuUploadFiles != null && commuUploadFiles.length > 0) {
				// 첨부파일들을 반복 처리
				for (MultipartFile uploadFile : commuUploadFiles) {
					// 파일이 비어있지 않은 경우
					if (!uploadFile.isEmpty()) {
						String originalName = uploadFile.getOriginalFilename();
						String fileName = originalName.substring(originalName.lastIndexOf("\\") + 1);
						String uuid = UUID.randomUUID().toString(); // 고유 ID 생성
						String url = "C:\\Users\\KOSA\\Downloads";
						File dir = new File(url);
						if (!dir.exists()) {
							dir.mkdirs(); // 폴더가 존재하지 않으면 생성
						}
						String savefileName = url + File.separator + uuid + "_" + fileName; // 저장될 파일명 결정
						Path savePath = Paths.get(savefileName);

						// 실제 파일 저장
						try {
							uploadFile.transferTo(savePath);

							// 첨부 파일 메타데이터 생성
							CommuFile file = new CommuFile();
							file.setCommuFileName(fileName);
							file.setCommuFileSize(uploadFile.getSize());
							file.setCommuFileExt(FilenameUtils.getExtension(originalName));
							System.out.println(originalName);
							file.setCommuFilePath(savefileName); // 파일의 저장 경로


							commuFiles.add(file); // 첨부 파일 리스트에 추가

						} catch (IOException e) { // 파일 저장 실패 시 예외 처리
							logger.error("File saving failed: ", e);
						}
					}
				}
			}
			// 데이터베이스에 게시물 및 첨부파일 정보 삽입
			commuService.insertPost(commu, commuFiles);

		} catch (Exception e) { // 기타 예외 처리
			e.printStackTrace();
			// 리다이렉트 시 메시지 전달
			redirectAttrs.addFlashAttribute("message", e.getMessage());
		}

		// 작성한 게시물을 보여주는 페이지로 리다이렉트
		return "redirect:/user/commu/view" + commu.getCommuId() + "/" + commu.getCommuCateCode();
	}

}
