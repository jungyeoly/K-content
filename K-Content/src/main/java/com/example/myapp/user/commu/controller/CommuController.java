
package com.example.myapp.user.commu.controller;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import java.io.File;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.example.myapp.user.commu.service.ICommuService;
import com.example.myapp.user.commucomment.service.ICommuCommentService;

import org.apache.commons.io.FilenameUtils;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
	ICommuCommentService commuCommentService;

	@Autowired
	private ICommonCodeService commonCodeService;

	@GetMapping("/commu/{page}") // 커뮤니티 메인
	public String selectAllPost(@PathVariable int page, @ModelAttribute("commu") Commu commu, Model model,
			HttpSession session) {
		session.setAttribute("page", page);

		List<Commu> commuList = commuService.selectAllPost(page);
		List<String> cateList = commonCodeService.cateList("C03");
		List<String> noticeList = commonCodeService.cateList("C06");

		int commuCount = commuService.totalCommu();
		int totalPage = 0;

		if (commuCount > 0) {
			totalPage = (int) Math.ceil(commuCount / 10.0);
		}
		int totalPageBlock = (int) (Math.ceil(totalPage / 10.0));
		int nowPageBlock = (int) Math.ceil(page / 10.0);
		int startPage = (nowPageBlock - 1) * 10 + 1;
		int endPage = 0;
		if (totalPage > nowPageBlock * 10) {
			endPage = nowPageBlock * 10;
		} else {
			endPage = totalPage;
		}
		model.addAttribute("totalPageCount", totalPage);
		model.addAttribute("nowPage", page);
		model.addAttribute("totalPageBlock", totalPageBlock);
		model.addAttribute("nowPageBlock", nowPageBlock);
		model.addAttribute("startPage", startPage);
		model.addAttribute("endPage", endPage);
		model.addAttribute("noticeList", noticeList);
		model.addAttribute("commuList", commuList);
		model.addAttribute("cateList", cateList);

		session.setAttribute("nowPage", page);

		return "user/commu/list";
	}

	@GetMapping("/commu/ajax/{page}")
	public ResponseEntity<Map<String, Object>> loadMainPosts(@PathVariable int page) {
		List<Commu> commuList = commuService.selectAllPost(page);
		List<String> cateList = commonCodeService.cateList("C03");
		List<String> noticeList = commonCodeService.cateList("C06");
		

		int commuCount = commuService.totalCommu();
		int totalPage = 0;
		if (commuCount > 0) {
			totalPage = (int) Math.ceil(commuCount / 10.0);
		}
		int totalPageBlock = (int) (Math.ceil(totalPage / 10.0));
		int nowPageBlock = (int) Math.ceil(page / 10.0);
		int startPage = (nowPageBlock - 1) * 10 + 1;
		int endPage = 0;
		if (totalPage > nowPageBlock * 10) {
			endPage = nowPageBlock * 10;
		} else {
			endPage = totalPage;
		}

		Map<String, Object> response = new HashMap<>();
		response.put("commuList", commuList);
		response.put("cateList", cateList);
		response.put("noticeList", noticeList);
		response.put("totalPageCount", totalPage);
		response.put("nowPage", page);
		response.put("totalPageBlock", totalPageBlock);
		response.put("nowPageBlock", nowPageBlock);
		response.put("startPage", startPage);
		response.put("endPage", endPage);

		return ResponseEntity.ok(response);
	}

	// 카테고리별 게시글 조회
	@GetMapping("/commu/commucatecode/{commuCateCode}")
	public ResponseEntity<Map<String, Object>> getPostsByCategory(@PathVariable String commuCateCode,
			@RequestParam(required = false) int page, HttpSession session) {
		List<Commu> posts = commuService.selectPostListByCategory(commuCateCode, page);
		int commuCategoryCount = commuService.totalCommuByCategory(commuCateCode);
		int totalPage = 0;
		if (commuCategoryCount > 0) {
			totalPage = (int) Math.ceil(commuCategoryCount / 10.0);
		}
		int totalPageBlock = (int) (Math.ceil(totalPage / 10.0));
		int nowPageBlock = (int) Math.ceil(page / 10.0);
		int startPage = (nowPageBlock - 1) * 10 + 1;
		int endPage = 0;
		if (totalPage > nowPageBlock * 10) {
			endPage = nowPageBlock * 10;
		} else {
			endPage = totalPage;
		}

		Map<String, Object> response = new HashMap<>();
		response.put("posts", posts);
		response.put("totalPageCount", totalPage);
		response.put("nowPage", page);
		response.put("totalPageBlock", totalPageBlock);
		response.put("nowPageBlock", nowPageBlock);
		response.put("startPage", startPage);
		response.put("endPage", endPage);

		return ResponseEntity.ok(response);
	}

	// 커뮤니티 게시글 검색
	@GetMapping("/commu/search/{page}")
	public ResponseEntity<Map<String, Object>> search(@RequestParam(required = false, defaultValue = "") String keyword,
			@PathVariable int page) {
		Map<String, Object> response = new HashMap<>();

		try {
			List<Commu> commuList = commuService.searchListByContentKeyword(keyword, page);
			int postsearchCount = commuService.selectTotalPostCountByKeyWord(keyword);
			int totalPage = 0;
			if (postsearchCount > 0) {
				totalPage = (int) Math.ceil(postsearchCount / 10.0);
			}
			int totalPageBlock = (int) (Math.ceil(totalPage / 10.0));
			int nowPageBlock = (int) Math.ceil(page / 10.0);
			int startPage = (nowPageBlock - 1) * 10 + 1;
			int endPage = 0;
			if (totalPage > nowPageBlock * 10) {
				endPage = nowPageBlock * 10;
			} else {
				endPage = totalPage;
			}

			response.put("commuList", commuList);
			response.put("keyword", keyword);
			response.put("totalPageCount", totalPage);
			response.put("nowPage", page);
			response.put("totalPageBlock", totalPageBlock);
			response.put("nowPageBlock", nowPageBlock);
			response.put("startPage", startPage);
			response.put("endPage", endPage);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok(response);
	}

	// 커뮤니티 게시글  누르면 상세보기
	@GetMapping("/commu/detail/{commuId}")
	public String getCommuDetails(@PathVariable int commuId, Model model) {
		List<CommonCode> commuCateCodeList = commonCodeService.findCommonCateCodeByUpperCommonCode("C03");
		model.addAttribute("commuCateCodeList", commuCateCodeList);
		 commuService.increaseReadCnt(commuId);
		    Commu commu = commuService.selectPostWithoutIncreasingReadCnt(commuId);
		List<CommuFile> commuFiles = commuService.selectFilesByPostId(commuId);
		model.addAttribute("commu", commu);
		model.addAttribute("commuFiles", commuFiles);
		logger.info("getCommuDetails" + commu.toString());
	
		return "user/commu/view";
	}

	
	// 게시글에 있는 첨부파일 단일 다운로드
	@GetMapping("/download/{commuFileId}")
	public ResponseEntity<Resource> downloadFile(@PathVariable String commuFileId) {
		try {
			CommuFile commuFile = commuService.getFile(commuFileId);

			// 파일이 DB에 존재하지 않는 경우
			if (commuFile == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body(new ByteArrayResource("지정된 파일이 데이터베이스에 존재하지 않습니다.".getBytes()));
			}

			// 파일의 저장 경로에서 파일 데이터를 읽습니다.
			Path filePath = Paths.get(uploadPath + commuFile.getCommuFilePath());
			Resource resource = new InputStreamResource(Files.newInputStream(filePath));

			// 파일 이름 인코딩
			String originalName = commuFile.getCommuFileName();
			String encodedFileName = URLEncoder.encode(originalName, StandardCharsets.UTF_8);

			return ResponseEntity.ok().contentType(MediaType.parseMediaType(Files.probeContentType(filePath)))
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + encodedFileName + "\"")
					.body(resource);
		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ByteArrayResource("파일을 읽는 동안 오류가 발생했습니다.".getBytes()));
		}
	}

	// 게시글 첨부파일 zip으로 한번에 다운받기
	@GetMapping("/download-all-images/{commuId}")
	public ResponseEntity<Resource> downloadAllImages(@PathVariable int commuId) {
		try {
			List<CommuFile> commuFiles = commuService.getAllFilesByCommuId(commuId);

			// 파일이 하나도 없다면 적절한 응답을 반환
			if (commuFiles.isEmpty()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body(new ByteArrayResource("첨부된 파일이 없습니다.".getBytes(StandardCharsets.UTF_8)));
			}

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			try (ZipOutputStream zos = new ZipOutputStream(baos, StandardCharsets.UTF_8)) {
				for (CommuFile commuFile : commuFiles) {
					Path filePath = Paths.get(uploadPath + commuFile.getCommuFilePath());
					if (!Files.exists(filePath))
						continue; // 파일이 존재하지 않으면 다음 파일로 넘어갑니다.

					ZipEntry entry = new ZipEntry(commuFile.getCommuFileName());
					zos.putNextEntry(entry);
					byte[] bytes = Files.readAllBytes(filePath);
					zos.write(bytes, 0, bytes.length);
					zos.closeEntry();
				}
			}

			byte[] zipBytes = baos.toByteArray();
			ByteArrayResource resource = new ByteArrayResource(zipBytes);

			String encodedFileName = URLEncoder.encode("all-images.zip", StandardCharsets.UTF_8);

			return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM)
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + encodedFileName + "\"")
					.body(resource);
		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ByteArrayResource("파일을 읽는 동안 오류가 발생했습니다.".getBytes(StandardCharsets.UTF_8)));
		}
	}

	// 카테고리별 커뮤니티 글쓰기
	@GetMapping("/commu/write")
	public String writePost(HttpSession session,Model model) {
		//CSRF 토큰을 생성하여 세션에 저장
		String csrfToken = UUID.randomUUID().toString();
	session.setAttribute("csrfToken", csrfToken);
		List<CommonCode> commuCateCodeList = commonCodeService.findCommonCateCodeByUpperCommonCode("C03");
		
		model.addAttribute("commuCateCodeList", commuCateCodeList);
		model.addAttribute("isCommunWritePage", true);

		return "user/commu/write";
	}

	// 커뮤니티 글쓰기 및 파일 업로드를 처리하는 메서드
	@PostMapping("/commu/write/{commuCateCode}")
	public String writePost(Commu commu, @PathVariable String commuCateCode,
			@RequestParam("commuUploadFiles") MultipartFile[] commuUploadFiles, BindingResult results,
			RedirectAttributes redirectAttrs, HttpSession session,String csrfToken) {
		logger.info("/commu/write/{commuCateCode} : " + commu.toString() + csrfToken);
		logger.info("writePost method started.");
		logger.info("Commu object: " + commu.toString());
		// 게시물 정보 로깅
		logger.info("/user/commu/write : " + commu.toString());
		if(csrfToken==null || "".equals(csrfToken)) {
			throw new RuntimeException("CSRF 토큰이 없습니다.");
		}else if(!csrfToken.equals(session.getAttribute("csrfToken"))) {
			throw new RuntimeException("잘못된 접근이 감지되었습니다.");
		}
		

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

		return "redirect:/commu/detail" +  "/" + commu.getCommuId();

	}
	
	// 게시글 수정하기(기존 게시글 정보 가져오기)
	@GetMapping("/commu/update/{commuCateCode}/{commuId}")
	public String updatePost(@PathVariable int commuId, @PathVariable String commuCateCode, Model model, Principal principal) {
		//권한 검사
		Commu commu = commuService.selectPostWithoutIncreasingReadCnt(commuId);
		if(!commu.getCommuMberId().equals(principal.getName())) {
			    return "/error/403"; // 권한이 없으면 에러페이지로 이동
			}
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
			RedirectAttributes redirectAttrs, Principal principal) {
		
		//권한 검사
		 Commu existingCommu = commuService.selectPostWithoutIncreasingReadCnt(commuId);
		    if (!existingCommu.getCommuMberId().equals(principal.getName())) {
		        // 현재 로그인한 사용자와 게시물 작성자가 다르면 권한이 없음
		        return  "/error/403";
		    }
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
		return "redirect:/commu/detail" + "/" + commu.getCommuId();

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
			return "redirect:/commu/1"; // 게시글 목록 페이지로 리다이렉트

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

	// 게시글 신고
	@PostMapping("/commu/report/{commuId}")
	public String reportCommu(@PathVariable int commuId, RedirectAttributes redirectAttributes) {
		Commu commu = commuService.selectPostWithoutIncreasingReadCnt(commuId);
		commuService.increaseReadCnt(commuId);
		if (commu != null) {
			commuService.reportPost(commuId);
			redirectAttributes.addFlashAttribute("message", "게시글이 신고되었습니다.");
		} else {
			redirectAttributes.addFlashAttribute("error", "해당 게시글을 찾을 수 없습니다.");
		}
		return "redirect:/commu";
	}

}