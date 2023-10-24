//package com.example.myapp.cms.content.controller;
//
//import com.example.myapp.cms.content.model.CntntGoodsMapping;
//import com.example.myapp.cms.content.model.CmsContent;
//import com.example.myapp.cms.content.model.CntntInsertForm;
//import com.example.myapp.cms.content.model.YouTubeItem;
//import com.example.myapp.cms.content.service.*;
//import com.example.myapp.cms.goods.model.Goods;
//import com.example.myapp.cms.goods.service.IGoodsService;
//import com.example.myapp.commoncode.model.CommonCode;
//import com.example.myapp.commoncode.service.ICommonCodeService;
//import jakarta.servlet.http.HttpSession;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.security.core.Authentication;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//
//import javax.imageio.ImageIO;
//import java.awt.image.BufferedImage;
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.net.URL;
//import java.util.*;
//
//@Controller
//@PreAuthorize("hasRole('ADMIN')")
//@RequestMapping("/cs/test")
//public class CSController {
//    @Autowired
//    YouTubeApiService youTubeApiService;
//    @Autowired
//    IContentService contentService;
//    @Autowired
//    ICSService csService;
//    @Autowired
//    ICntntGoodsMappingService cntntGoodsMappingService;
//    @Autowired
//    IGoodsService goodsService;
//    @Autowired
//    Instagram_Selenium instagram_Selenium;
//    @Autowired
//    ICommonCodeService commonCodeService;
//
//    // 콘텐츠 추천 페이지
//    @GetMapping("/recomm")
//    public String showYouTube() {
//        return "cms/cntnt/new-cntnt-recom";
//    }
//
//    @GetMapping("/youtube/keyword")
//    @ResponseBody
//    public List<YouTubeItem> searchYouTube(@RequestParam(value = "searchKeyword", required = false) String searchKeyword, @RequestParam(value = "items", required = false, defaultValue = "20") String items) {
//        int max = Integer.parseInt(items);
//        List<YouTubeItem> result = youTubeApiService.youTubeSearch(searchKeyword, max);
//
//        return result;
//    }
//
//    //관리자 컨텐츠 메인화면
//    @GetMapping("")
//    public String test(@RequestParam(required = false, defaultValue = "All") String cate, Model model, HttpSession session) {
//        List<String> cateList = commonCodeService.cateList("C03");
//        model.addAttribute("cateList", cateList);
//        model.addAttribute("cate", cate);
//        return "cms/cntnt/new-admin-main-content";
//    }
//
//
//    @GetMapping("/contents/{page}")
//    @ResponseBody
//    public List<CmsContent> getallcntnt(
//            @RequestParam(required = false, defaultValue = "All") String cate,
//            @RequestParam(defaultValue = "1") int page, Model model, HttpSession session) {
//
//        List<CmsContent> result = contentService.getAllContent(cate, page);
//        model.addAttribute("cate", cate);
//
//        for (int i = 0; i < result.size(); i++) {
//            List<String> contentUrlSplit = List.of(result.get(i).getCntntUrl().split("/"));
//            String partOfUrl = contentUrlSplit.get(3);
//            List<String> partOfUrl2 = List.of(partOfUrl.split("="));
//            String restultCode = partOfUrl2.get(1);
//            result.get(i).setCntntThumnail("https://i.ytimg.com/vi/" + restultCode + "/hqdefault.jpg");
//        }
//
//        int bbsCount = contentService.totalCntnt(cate);
//        int totalPage = 0;
//
//        if (bbsCount > 0) {
//            totalPage = (int) Math.ceil(bbsCount / 10.0);
//        }
//        int totalPageBlock = (int) (Math.ceil(totalPage / 10.0));
//        int nowPageBlock = (int) Math.ceil(page / 10.0);
//        int startPage = (nowPageBlock - 1) * 10 + 1;
//        int endPage = 0;
//        if (totalPage > nowPageBlock * 10) {
//            endPage = nowPageBlock * 10;
//        } else {
//            endPage = totalPage;
//        }
//        model.addAttribute("totalPageCount", totalPage);
//        model.addAttribute("nowPage", page);
//        model.addAttribute("totalPageBlock", totalPageBlock);
//        model.addAttribute("nowPageBlock", nowPageBlock);
//        model.addAttribute("startPage", startPage);
//        model.addAttribute("endPage", endPage);
//
//        return result;
//
//    }
//
//
//    //콘텐츠 상세페이지의 유튜브 영상 호출
//    @GetMapping("/youtube/iframe")
//    @ResponseBody
//    public String getAIframe(@RequestParam(value = "targetContentIdF") String targetContentIdF) {
//        List<String> contentUrlSplit = List.of(targetContentIdF.split("/"));
//        String partOfUrl = contentUrlSplit.get(3);
//        List<String> partOfUrl2 = List.of(partOfUrl.split("="));
//        String restultCode = partOfUrl2.get(1);
//        return restultCode;
//    }
//
//    //콘텐츠 상세 페이지인스타 크롤링
//    @GetMapping("/insta-img")
//    @ResponseBody
//    public List<String> getInstaImg(@RequestParam(value = "trendQueryList") List<String> trendQueryList, Authentication authentication) throws IOException {
//
//        List<String> realImg = new ArrayList<>();
//        String role = authentication.getAuthorities().toString();
//        if (role.equals("[ROLE_ADMIN]")) {
//            return realImg;
//        } else {
//            instagram_Selenium.isQuit();
////            instagram_Selenium = new Instagram_Selenium();
//            instagram_Selenium.instagram_Selenium();
//            for (int i = 0; i < trendQueryList.size(); i++) {
//                try {
//                    String oneUrl = instagram_Selenium.crawl(trendQueryList.get(i));
//                    //TODO 예외처리
//                    URL urlInput = new URL(oneUrl);
//                    BufferedImage urlImg = ImageIO.read(urlInput);
//                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
//                    ImageIO.write(urlImg, "jpg", bos);
//                    Base64.Encoder encoder = Base64.getEncoder();
//                    String encodedString = encoder.encodeToString(bos.toByteArray());
//                    //TODO encodedString만 보내고 태그는 자바사크립트에서 적기 @!!!!
//                    realImg.add("<img src=data:image/jpg;base64," + encodedString + " style=\"width: 200px; height: auto;\" >");
//                } catch (Exception e) {
//                    instagram_Selenium.chromeExit();
//                }
//            }
//            instagram_Selenium.chromeExit();
//            return realImg;
//        }
//
//    }
//
//    //콘텐츠 생성 페이지
//    @GetMapping("/content/new")
//    public String getMakeContentFormNew(Model model) {
//        List<CommonCode> commonCodes = commonCodeService.findCommonCateCodeByUpperCommonCode("C03");
//        model.addAttribute("category", commonCodes);
//        return "cms/cntnt/new-make-cntnt";
//    }
//
//    //콘텐츠 생성 페이지 form 유튜브
//    @GetMapping("/content-form")
//    public String getMakeContentForm(String cntntURL, String cntntTitle, Model model) {
//        CmsContent cntnt = new CmsContent();
//        cntnt.setCntntTitle(cntntTitle);
//        cntnt.setCntntUrl(cntntURL);
//        //이거 다른이름으로 보내야됨
//        model.addAttribute("yContent", cntnt);
//        List<CommonCode> commonCodes = commonCodeService.findCommonCateCodeByUpperCommonCode("C03");
//        model.addAttribute("category", commonCodes);
//        return "cms/cntnt/new-make-cntnt";
//    }
//
//    // 기존 콘텐츠 수정 form
//    @GetMapping("/content/modify-form")
//    public String getUpdateContentForm(int targetContentIdF, Model model) {
//        //기존 콘텐츠 데이터 뽑기
//        CmsContent content = contentService.getAContent(targetContentIdF);
//        model.addAttribute("content", content);
//
//        //키워드
//        List<String> keywordList = Arrays.stream(content.getCntntKwrd().split(",")).toList();
//        model.addAttribute("keywordList", keywordList);
//
//        //굿즈 & 파일
//        List<CntntGoodsMapping> goodsIdByCntnt = cntntGoodsMappingService.getAllGoodsByContent(targetContentIdF);
//        List<Goods> goodsJFileList = new ArrayList<Goods>();
//        for (int i = 0; i < goodsIdByCntnt.size(); i++) {
//            //일단 파일이 하나라고 가정....
//            goodsJFileList.add(goodsService.getGoodsJFileByGoodsId(goodsIdByCntnt.get(i).getGoodsId()));
//        }
//
//        // 삭제 된 굿즈는 아예 안뽑는건지? 알아봐야함
//        model.addAttribute("goodsJFileList", goodsJFileList);
//
//        //카테고리
//        List<CommonCode> commonCodes = commonCodeService.findCommonCateCodeByUpperCommonCode("C03");
//        model.addAttribute("category", commonCodes);
//
//        // 쿼리 앞에 키워드 가져와서 뽑기
//        List<String> trendQueryList = new ArrayList<>();
//        for (int i = 0; i < keywordList.size(); i++) {
//            trendQueryList.add(keywordList.get(i));
//        }
//        model.addAttribute("trendQueryList", trendQueryList);
////        return "cms/cntnt/newcontentMakeForm";
//        return "cms/cntnt/new-make-cntnt";
//    }
//
//    //콘텐츠 생성/수정
//    @PostMapping("/content")
//    @ResponseBody
//    public ResponseEntity<String> postCntntForm(@RequestBody CntntInsertForm receivedData) {
//        CmsContent content = new CmsContent();
//
//        content.setCntntUrl(receivedData.getCntntUrl());
//        content.setCntntTitle(receivedData.getCntntTitle());
//        content.setCntntCateCode(receivedData.getCntntCateCode());
//        // 키워드 리스트를 하나의 문자열로
//
//        String keywordlist = receivedData.getKeywordList().toString();
//
//        String keyword = receivedData.getKeywordList().toString().substring(1, keywordlist.length() - 1);
//        content.setCntntKwrd(keyword);
//
//        List<Integer> goodsList = receivedData.getGoodsList();
//        int rowsAffected = 0;
//        if (receivedData.getIs().equals("수정")) {
//            content.setCntntId(receivedData.getCntntId());
//            rowsAffected = contentService.updateAContent(content, goodsList);
//        } else if (receivedData.getIs().equals("생성")) {
//            rowsAffected = contentService.insertAContent(content, goodsList);
//        }
//
//        if (rowsAffected <= 0) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("cntnt 업로드 실패");
//        } else {
//            if (receivedData.getIs().equals("수정")) {
//                return ResponseEntity.ok("수정완료");
//            } else if (receivedData.getIs().equals("생성")) {
//                return ResponseEntity.ok("생성완료");
//            }
//        }
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("cntnt 업로드 실패");
//    }
//
//
//    @GetMapping("paging")
//    public String paging(
//            @RequestParam(required = false, defaultValue = "All") String cate,
//            @RequestParam("page") int page, Model model, HttpSession session) {
//
//        int bbsCount = contentService.totalCntnt(cate);
//        int totalPage = 0;
//
//        if (bbsCount > 0) {
//            totalPage = (int) Math.ceil(bbsCount / 10.0);
//        }
//        int totalPageBlock = (int) (Math.ceil(totalPage / 10.0));
//        int nowPageBlock = (int) Math.ceil(page / 10.0);
//        int startPage = (nowPageBlock - 1) * 10 + 1;
//        int endPage = 0;
//        if (totalPage > nowPageBlock * 10) {
//            endPage = nowPageBlock * 10;
//        } else {
//            endPage = totalPage;
//            if (endPage == 0) {
//                endPage = 1;
//            }
//        }
//        model.addAttribute("totalPageCount", totalPage);
//        model.addAttribute("nowPage", page);
//        model.addAttribute("totalPageBlock", totalPageBlock);
//        model.addAttribute("nowPageBlock", nowPageBlock);
//        model.addAttribute("startPage", startPage);
//        model.addAttribute("endPage", endPage);
//        session.setAttribute("nowPage", page);
//
//        return "cms/cntnt/paging";
//    }
//
//    //콘텐츠 상세 콘텐츠 추천
//    @GetMapping("/content/keyword")
//    @ResponseBody
//    public List<CmsContent> getContentBykeyword(@RequestParam(value = "trendQueryList") List<String> keywordList,
//                                                @RequestParam(value = "cntntId") int cntntId) {
//        List<CmsContent> result = contentService.getContentByKeyword(keywordList);
//        for (int i = 0; i < result.size(); i++) {
//            if (result.get(i).getCntntId() == cntntId) {
//                result.remove(i);
//            } else {
//                continue;
//            }
//        }
//
//        for (int i = 0; i < result.size(); i++) {
//            List<String> contentUrlSplit = List.of(result.get(i).getCntntUrl().split("/"));
//            String partOfUrl = contentUrlSplit.get(3);
//            List<String> partOfUrl2 = List.of(partOfUrl.split("="));
//            String restultCode = partOfUrl2.get(1);
//            result.get(i).setCntntThumnail("https://i.ytimg.com/vi/" + restultCode + "/hqdefault.jpg");
//        }
//        return result;
//    }
//
//    //콘텐츠 검색
//    @GetMapping("/contents/search")
//    @ResponseBody
//    public List<CmsContent> getContentBySearchKeyword(
//            @RequestParam(value = "searchKeyword") String searchKeyword,
//            @RequestParam(value = "page", defaultValue = "1") int page, Model model) {
//
//
//        List<CmsContent> result = contentService.getPagingContentBySearch(Collections.singletonList(searchKeyword), page);
//        for (int i = 0; i < result.size(); i++) {
//            List<String> contentUrlSplit = List.of(result.get(i).getCntntUrl().split("/"));
//            String partOfUrl = contentUrlSplit.get(3);
//            List<String> partOfUrl2 = List.of(partOfUrl.split("="));
//            String restultCode = partOfUrl2.get(1);
//            result.get(i).setCntntThumnail("https://i.ytimg.com/vi/" + restultCode + "/hqdefault.jpg");
//        }
//
//        return result;
//    }
//
//    @GetMapping("search/paging")
//    public String searchPaging(
//            @RequestParam(value = "searchKeyword") String searchKeyword,
//            @RequestParam(value = "page", defaultValue = "1") int page, Model model, HttpSession session) {
//        int bbsCount = contentService.totalSearch(Collections.singletonList(searchKeyword));
//        int totalPage = 0;
//
//        if (bbsCount > 0) {
//            totalPage = (int) Math.ceil(bbsCount / 10.0);
//        }
//        int totalPageBlock = (int) (Math.ceil(totalPage / 10.0));
//        int nowPageBlock = (int) Math.ceil(page / 10.0);
//        int startPage = (nowPageBlock - 1) * 10 + 1;
//        int endPage = 0;
//        if (totalPage > nowPageBlock * 10) {
//            endPage = nowPageBlock * 10;
//        } else {
//            endPage = totalPage;
//            if (endPage == 0) {
//                endPage = 1;
//            }
//        }
//        model.addAttribute("totalPageCount", totalPage);
//        model.addAttribute("nowPage", page);
//        model.addAttribute("totalPageBlock", totalPageBlock);
//        model.addAttribute("nowPageBlock", nowPageBlock);
//        model.addAttribute("startPage", startPage);
//        model.addAttribute("endPage", endPage);
//
//
//        return "cms/cntnt/paging";
//    }
//
//    //콘텐츠 삭제 처리
//    @PatchMapping("/content")
//    public String deleteContentForm(@RequestParam(value = "cntntId") int cntntId, Model model, HttpSession session) {
//        //update content
//
//        contentService.updateDelStat(cntntId);
//
//        List<String> cateList = commonCodeService.cateList("C03");
//        model.addAttribute("cateList", cateList);
//
//        int page = 1;
//        int bbsCount = contentService.totalCntnt("ALL");
//
//        int totalPage = 0;
//
//        if (bbsCount > 0) {
//            totalPage = (int) Math.ceil(bbsCount / 10.0);
//        }
//        int totalPageBlock = (int) (Math.ceil(totalPage / 10.0));
//        int nowPageBlock = (int) Math.ceil(page / 10.0);
//        int startPage = (nowPageBlock - 1) * 10 + 1;
//        int endPage = 0;
//        if (totalPage > nowPageBlock * 10) {
//            endPage = nowPageBlock * 10;
//        } else {
//            endPage = totalPage;
//        }
//        model.addAttribute("totalPageCount", totalPage);
//        model.addAttribute("nowPage", page);
//        model.addAttribute("totalPageBlock", totalPageBlock);
//        model.addAttribute("nowPageBlock", nowPageBlock);
//        model.addAttribute("startPage", startPage);
//        model.addAttribute("endPage", endPage);
//
//        session.setAttribute("nowPage", page);
//        return "cms/cntnt/new-admin-main-content";
//
//    }
//
//}
