
package com.example.myapp.user.commu.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.example.myapp.user.commu.service.ICommuService;
import com.example.myapp.user.commucomment.model.CommuComment;
import com.example.myapp.user.commucomment.service.CommuCommentService;
import com.example.myapp.user.commucomment.service.ICommuCommentService;

import org.apache.commons.io.FilenameUtils;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.myapp.commoncode.model.CommonCode;
import com.example.myapp.commoncode.service.ICommonCodeService;
import com.example.myapp.user.commu.model.Commu;
import com.example.myapp.user.commu.model.CommuFile;
import com.google.common.collect.Lists;

import jakarta.servlet.http.HttpSession;

@Controller
public class CommuController {
	static final Logger logger = LoggerFactory.getLogger(CommuController.class);

	@Value("${part4.upload.path}")
	private String uploadPath;
	@Value("${img}")
	private String url;

	@Autowired
	ICommuService commuService;
	
	@Autowired
	private ICommuCommentService commuCommentService;

	@Autowired
	private ICommonCodeService commonCodeService;

	@GetMapping("/commu") // 커뮤니티 메인
	public String main(@RequestParam(defaultValue = "1") int currentPage, @ModelAttribute("commu") Commu commu,
			Model model, HttpSession session) {
		List<Commu> commulist = commuService.selectAllPost();
		List<String> cateList = commonCodeService.cateList("C03");
		List<CommonCode> commuCateCodeList = commonCodeService.findCommonCateCodeByUpperCommonCode("C03");
		model.addAttribute("commuCateCodeList", commuCateCodeList);
		model.addAttribute("cateList", cateList);

		int totalPage = 0;
		int totalCommu = 0;

		// 검색결과가 있는 경우 paging처리
		if (commulist != null && !commulist.isEmpty()) {

			totalCommu = commulist.size();
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
		List<CommonCode> commuCateCodeList = commonCodeService.findCommonCateCodeByUpperCommonCode("C03");
		model.addAttribute("commuCateCodeList", commuCateCodeList);
		Commu commu = commuService.selectPost(commuId);
		List<CommuFile> commuFiles = commuService.selectFilesByPostId(commuId);
		model.addAttribute("commu", commu);
		model.addAttribute("commuFiles", commuFiles);
		logger.info("getCommuDetails" + commu.toString());
		return "user/commu/view";
	}

	// 커뮤니티 게시글 글번호,카테고리에 따른 게시글 상세보기
		@GetMapping("/commu/{commuCateCode}/{commuId}")
		public String getCommuDetails(@PathVariable String commuCateCode, @PathVariable int commuId, Model model) {
			List<CommonCode> commuCateCodeList = commonCodeService.findCommonCateCodeByUpperCommonCode("C03");
			model.addAttribute("commuCateCodeList", commuCateCodeList);
			Commu commu = commuService.selectPost(commuId);
			List<CommuFile> commuFiles = commuService.selectFilesByPostId(commuId);
			model.addAttribute("commu", commu);
			model.addAttribute("commuFiles", commuFiles);
			model.addAttribute("commuCateCode", commu.getCommuCateCode());
			model.addAttribute("commuId", commu.getCommuId());
			logger.info("getCommuDetails" + commu.toString());
			
			 // 게시글에 연결된 댓글 정보 조회
			List<CommuComment> comments = commuCommentService.selectCommuCommentsByCommuCommentId(commuId);
			   model.addAttribute("comments", comments);
			return "user/commu/view";
		}
		
	


	// 카테고리별 커뮤니티 글쓰기
	@GetMapping("/commu/write")
	public String writePost(Model model) {
		List<CommonCode> commuCateCodeList = commonCodeService.findCommonCateCodeByUpperCommonCode("C03");
		model.addAttribute("commuCateCodeList", commuCateCodeList);
		logger.info("Fetched commuCateCodeList: " + commuCateCodeList);// 실제 데이터 확인

		return "user/commu/write";
	}

	// 커뮤니티 글쓰기 및 파일 업로드를 처리하는 메서드
	@PostMapping("/commu/write/{commuCateCode}")
	public String writePost(Commu commu, @PathVariable String commuCateCode,
			@RequestParam("commuUploadFiles") MultipartFile[] commuUploadFiles, BindingResult results,
			RedirectAttributes redirectAttrs, HttpSession session) {

		logger.info("writePost method started.");
		logger.info("Commu object: " + commu.toString());

		// 게시물 정보 로깅
		logger.info("/user/commu/write : " + commu.toString());

		try {

			// 게시물 내용에서 줄 바꿈을 HTML 태그로 변경
			commu.setCommuCntnt(commu.getCommuCntnt().replace("\r\n", "<br>"));
			// 게시물 제목과 내용에 대해 HTML 태그를 제거 (XSS 방지)
			commu.setCommuTitle(Jsoup.clean(commu.getCommuTitle(), Safelist.basic()));
			commu.setCommuCntnt(Jsoup.clean(commu.getCommuCntnt(), Safelist.basic()));

			// 첨부파일 리스트 초기화
			List<CommuFile> commuFiles = new ArrayList<>();

			if (commuUploadFiles != null && commuUploadFiles.length > 0) {
				logger.info("Processing uploaded files.");

				for (MultipartFile uploadFile : commuUploadFiles) {
					if (!uploadFile.isEmpty()) {
						String originalName = uploadFile.getOriginalFilename();
						String fileName = originalName.substring(originalName.lastIndexOf("\\") + 1);
						String uuid = UUID.randomUUID().toString();
						String saveDirectory = uploadPath + "img/photo"; // uploadPath 변수 사용
						File dir = new File(saveDirectory);
						if (!dir.exists()) {
							dir.mkdirs();
						}
						String savefileName = saveDirectory + File.separator + uuid + "_" + fileName;
						Path savePath = Paths.get(savefileName);

						try {
							uploadFile.transferTo(savePath);
							logger.info("File saved successfully at: " + savePath);

							// 웹 접근 경로 설정 (url 변수 활용)
							String webAccessPath = url + uuid + "_" + fileName; // url 변수 사용

							CommuFile file = new CommuFile();
							file.setCommuFileId(UUID.randomUUID().toString());
							file.setCommuFileName(fileName);
							file.setCommuFileSize(uploadFile.getSize());
							file.setCommuFileExt(FilenameUtils.getExtension(originalName));
							file.setCommuFilePath(webAccessPath); // 웹 접근 경로를 DB에 저장
							file.setCommuFileCommuId(commu.getCommuId());
							commuFiles.add(file);
							logger.info("Created CommuFile metadata: " + file.toString());

						} catch (IOException e) {
							logger.error("File saving failed: ", e);
						}
					}
				}

				if (!commuFiles.isEmpty()) {
					logger.info("Attempting to save the following CommuFiles to the DB: " + commuFiles.toString());
					commuService.insertPostwithFiles(commu, commuFiles);
					logger.info("Successfully saved CommuFiles to the DB.");
				} else {
					commuService.insertPost(commu);
				}

			}

		} catch (Exception e) {
			logger.error("Error message", e);
			redirectAttrs.addFlashAttribute("message", e.getMessage());
		}

		return "redirect:/commu/" + commu.getCommuCateCode() + "/" + commu.getCommuId();

	}

	// 게시글 수정하기(기존 게시글 정보 가져오기)
	@GetMapping("/commu/update/{commuCateCode}/{commuId}")
	public String updatePost(@PathVariable int commuId, @PathVariable String commuCateCode, Model model) {
		Commu commu = commuService.selectPost(commuId);
		List<CommuFile> commuFiles = commuService.selectFilesByPostId(commuId);
		List<CommonCode> commuCateCodeList = commonCodeService.findCommonCateCodeByUpperCommonCode("C03");

		model.addAttribute("commu", commu);
		model.addAttribute("commuFiles", commuFiles);
		model.addAttribute("commuCateCodeList", commuCateCodeList);
		logger.info("Editing Commu: " + commu.toString());

		return "user/commu/update";
	}

	// 게시글 수정 처리
	@PostMapping("/commu/update/{commuCateCode}/{commuId}")
	public String updatePostAndFiles(Commu commu, @PathVariable int commuId, @PathVariable String commuCateCode,
			@RequestParam("commuUploadFiles") MultipartFile[] commuUploadFiles, BindingResult results,
			RedirectAttributes redirectAttrs) {

		// 게시물 정보 로깅
		logger.info("/commu/update : " + commu.toString());

		try {
			// 게시물 내용에서 줄 바꿈을 HTML 태그로 변경
			commu.setCommuCntnt(commu.getCommuCntnt().replace("\r\n", "<br>"));
			// 게시물 제목과 내용에 대해 HTML 태그를 제거 (XSS 방지)
			commu.setCommuTitle(Jsoup.clean(commu.getCommuTitle(), Safelist.basic()));
			commu.setCommuCntnt(Jsoup.clean(commu.getCommuCntnt(), Safelist.basic()));

			// 첨부파일 리스트 초기화
			List<CommuFile> commuFiles = new ArrayList<>();

			if (commuUploadFiles != null && commuUploadFiles.length > 0) {
				logger.info("Processing uploaded files for updating.");

				for (MultipartFile uploadFile : commuUploadFiles) {
					if (!uploadFile.isEmpty()) {
						String originalName = uploadFile.getOriginalFilename();
						String fileName = originalName.substring(originalName.lastIndexOf("\\") + 1);
						String uuid = UUID.randomUUID().toString();
						String saveDirectory = uploadPath + "img/photo"; // uploadPath 변수 사용
						File dir = new File(saveDirectory);
						if (!dir.exists()) {
							dir.mkdirs();
						}
						String savefileName = saveDirectory + File.separator + uuid + "_" + fileName;
						Path savePath = Paths.get(savefileName);

						try {
							uploadFile.transferTo(savePath);
							logger.info("File saved successfully at: " + savePath);

							// 웹 접근 경로 설정 (url 변수 활용)
							String webAccessPath = url + uuid + "_" + fileName; // url 변수 사용

							CommuFile file = new CommuFile();
							file.setCommuFileName(fileName);
							file.setCommuFileSize(uploadFile.getSize());
							file.setCommuFileExt(FilenameUtils.getExtension(originalName));
							file.setCommuFilePath(webAccessPath); // 웹 접근 경로를 DB에 저장
							file.setCommuFileCommuId(commu.getCommuId());
							commuFiles.add(file);
							logger.info("Created CommuFile metadata: " + file.toString());

						} catch (IOException e) {
							logger.error("File saving failed: ", e);
						}
					}
				}

				if (!commuFiles.isEmpty()) {
					commuService.updatePostAndFiles(commu, commuFiles);
					logger.info("Successfully updated Commu and/or CommuFiles in the DB.");
				} else {
					commuService.updatePost(commu);
					logger.info("Successfully updated Commu in the DB.");
				}
			}

		} catch (Exception e) {
			logger.error("Error during update:", e);
			redirectAttrs.addFlashAttribute("message", e.getMessage());
			return "redirect:/commu/update/" + commu.getCommuCateCode() + "/" + commu.getCommuId(); // 실패시 다시 수정 페이지로
																									// 리다이렉트
		}
		redirectAttrs.addFlashAttribute("message", "게시물이 성공적으로 업데이트되었습니다.");
		return "redirect:/commu/" + commu.getCommuCateCode() + "/" + commu.getCommuId();

	}

	// 게시글 삭제(삭제상태 변경)
	@PostMapping("/commu/deletepost/{commuId}")
	public String deletePost(@PathVariable int commuId, RedirectAttributes redirectAttrs) {
		try {

			// 게시글과 연결된 첨부파일 목록을 가져옵니다.
			List<CommuFile> attachedFiles = commuService.selectFilesByPostId(commuId);

			// 각 첨부파일을 삭제합니다.
			for (CommuFile file : attachedFiles) {
				commuService.deleteFileById(file.getCommuFileId());
			}

			// 게시글 상태를 "삭제상태"로 변경합니다.
			commuService.deletePost(commuId);

			redirectAttrs.addFlashAttribute("message", "게시물 및 관련 파일이 성공적으로 삭제되었습니다.");
			return "redirect:/commu"; // 게시글 목록 페이지로 리다이렉트

		} catch (Exception e) {
			logger.error("Error during deletion:", e);
			redirectAttrs.addFlashAttribute("message", "게시물 및 파일 삭제 중 오류가 발생하였습니다.");
			return "redirect:/commu/" + commuId; // 게시글 상세 페이지로 리다이렉트
		}
	}

	// 게시글에 첨부파일 삭제
	@PostMapping("/commu/delete-file")
	@Transactional
	public ResponseEntity<Map<String, Object>> deleteFile(@RequestBody CommuFile request) {
		String commuFileId = request.getCommuFileId();
		Map<String, Object> response = new HashMap<>();
		logger.info("Received request to delete file with commuFileId: " + commuFileId);

		try {
			CommuFile commuFile = commuService.getFile(commuFileId);

			// 파일이 DB에 존재하지 않는 경우
			if (commuFile == null) {
				logger.warn("No file found in the database for commuFileId: " + commuFileId);
				response.put("success", false);
				response.put("message", "지정된 파일이 데이터베이스에 존재하지 않습니다.");
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
			}

			Path filePath = Paths.get(uploadPath + commuFile.getCommuFilePath());

			if (Files.deleteIfExists(filePath)) {
				commuService.deleteFileById(commuFileId);
				response.put("success", true);
				response.put("message", "파일이 성공적으로 삭제되었습니다.");
			} else {
				response.put("success", false);
				response.put("message", "파일 삭제에 실패했습니다.");
			}

			return ResponseEntity.ok(response);

		} catch (IOException e) {
			logger.error("Error while deleting file", e);
			response.put("success", false);
			response.put("message", "파일을 삭제하는 동안 오류가 발생했습니다.");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}
}