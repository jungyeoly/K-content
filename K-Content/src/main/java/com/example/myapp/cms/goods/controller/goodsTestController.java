package com.example.myapp.cms.goods.controller;

import com.example.myapp.cms.goods.model.Goods;
import com.example.myapp.cms.goods.model.GoodsFile;
import com.example.myapp.cms.goods.model.PageBox;
import com.example.myapp.cms.goods.service.IGoodsService;
import com.example.myapp.user.inqry.model.Inqry;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Controller
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/cs/test/goods")
public class goodsTestController {
    @Autowired
    IGoodsService goodsService;
    @Value("${part4.upload.path}")
    private String uploadPath;
    @Value("${goods}")
    private String url;

    // admin side-bar 상품 리스트 접근
    @GetMapping("/{page}")
    public String getGoodsPages(@PathVariable int page, HttpSession session, Model model) {

        session.removeAttribute("message");
        session.setAttribute("page", page);

        List<Goods> goodsList = goodsService.getAllGoodsJFile(page);
        model.addAttribute("goodsList", goodsList);

        int bbsCount = goodsService.totalGoods();
        int totalPage = 0;
        if (bbsCount > 0) {
            totalPage = (int) Math.ceil(bbsCount / 9.0);
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

        session.setAttribute("nowPage", page);


        return "cms/goods/new-goods-list";
    }

    @GetMapping("")
    public String selectGoodsList() {
        return "cms/goods/new-goods-main";
    }

    // 상품 상세 화면 보여주기
    @GetMapping("/detail")
    public String getGoodsDetail(@RequestParam(value = "goodsId") int goodsId, Model model) {

        Goods goods = goodsService.getGoodsJFileByGoodsId(goodsId);

        model.addAttribute("goods", goods);

        return "cms/goods/new-goods-detail";
    }


    // 상품 검색 결과 출력
    @GetMapping("/search")
    @ResponseBody
    public List<Object> getSearchGoods(@RequestParam(value = "search") String search, @RequestParam(value = "page", required = false, defaultValue = "1") int page) {
        PageBox pageBox = new PageBox();
        List<Object> searchResult = new ArrayList<>();
        List<Goods> goodsList = goodsService.getSearchGoodsJFile(search, page);
        searchResult.add(goodsList);
        int bbsCount = goodsService.getSearchGoodsJFileCount(search);
        int totalPage = 0;
        if (bbsCount > 0) {
            totalPage = (int) Math.ceil(bbsCount / 9.0);
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
        pageBox.setTotalPageCount(totalPage);
        pageBox.setNowPage(page);
        pageBox.setTotalPageBlock(totalPageBlock);
        pageBox.setNowPageBlock(nowPageBlock);
        pageBox.setStartPage(startPage);
        pageBox.setEndPage(endPage);
        searchResult.add(pageBox);

        return searchResult;
    }

    @GetMapping("/item")
    @ResponseBody
    public List<Goods> getSearchGoodsResult(@RequestParam(value = "sendData") List<String> receivedData) {
        List<Goods> goodsList = new ArrayList<>();
        for (int i = 0; i < receivedData.size(); i++) {
            goodsList.add(goodsService.getGoodsJFileByGoodsId(Integer.parseInt(receivedData.get(i))));
        }

        return goodsList;
    }

    @GetMapping("/form")
    public String getMakeGoodsForm() {

        return "cms/goods/new-make-goods-in-nav";
    }

    @GetMapping("/cntnt/form")
    public String getMakeGoodsFormInMakeCntnt() {

        return "cms/goods/new-make-goods-in-cntnt-make-form";
    }

    // 상품 생성
    @PostMapping("")
    public ResponseEntity<String> createGoods(@RequestParam("goodsTitle") String goodsTitle,
                                              @RequestParam("goodsBrand") String goodsBrand,
                                              @RequestParam("goodsURL") String goodsURL,
                                              @RequestParam("goodsPrice") String goodsPrice,
                                              @RequestParam("keywordList") List<String> keywordListJson,
                                              @RequestParam("goodsFile") MultipartFile goodsFile) throws IOException {

//        try {

        String keywordlist = keywordListJson.toString();

        String keyword = keywordListJson.toString().substring(1, keywordlist.length() - 1);
        String originalEncodingFilename = Normalizer.normalize(goodsFile.getOriginalFilename(), Normalizer.Form.NFC);
        UUID uuid = UUID.randomUUID();
        String uuidString = uuid.toString();
        String originalFilename = originalEncodingFilename;
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String newFilename = uuidString + "_" + originalFilename;

        GoodsFile newGoodsFile = new GoodsFile();
        Goods newGoods = new Goods();

        newGoodsFile.setGoodsFileID(newFilename);
        newGoodsFile.setGoodsFileName(originalFilename);
        newGoodsFile.setGoodsFileExt(fileExtension);
        newGoodsFile.setGoodsFileSize(goodsFile.getSize());
        newGoodsFile.setGoodsFilePath(url);

        newGoods.setGoodsName(goodsTitle);
        newGoods.setGoodsBrand(goodsBrand);
        newGoods.setGoodsPrice(Integer.parseInt(goodsPrice));
        newGoods.setGoodsPurchsLink(goodsURL);
        newGoods.setGoodsKwrd(keyword);

        Path path = Paths.get(uploadPath + url).toAbsolutePath().normalize();

//        newFilename = new String(newFilename.getBytes(StandardCharsets.ISO_8859_1),"UTF-8");
        Path realPath = path.resolve(newFilename).normalize();

        int rowsAffected = goodsService.insertGoods(newGoods, newGoodsFile);
        goodsFile.transferTo(realPath);

        if (rowsAffected <= 0) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("goods 업로드 실패");
        }
        return ResponseEntity.ok("goods 업로드 및 처리 완료");
//TODO try catch 문 작성

//        }
//        catch (IOException e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("굿즈 업로드 실패");
//        }    // JSON 문자열을 객체로 변환 (keywordListJson)


    }

    //상품 수정 폼
    @GetMapping("modify-form")
    public String updateGoods(@RequestParam(value = "goodsId") int goodsId, Model model) {

        Goods goods = goodsService.getGoodsJFileByGoodsId(goodsId);
        model.addAttribute("goods", goods);
        //키워드
        List<String> keywordList = Arrays.stream(goods.getGoodsKwrd().split(",")).toList();
        model.addAttribute("keywordList", keywordList);

        return "cms/goods/new-make-goods-in-nav";
    }

    //상품삭제
    @PatchMapping("")
    public String createGoods(@RequestParam("goodsId") int goodsId) {
        goodsService.updateDelYnGoods(goodsId);
        return "cms/goods/new-goods-main";
    }

    //상품 수정 파일 있음
    @PatchMapping("/form")
    public ResponseEntity<String> updateGoods(@RequestParam("goodsId") int goodsId,
                                              @RequestParam("goodsTitle") String goodsTitle,
                                              @RequestParam("goodsBrand") String goodsBrand,
                                              @RequestParam("goodsURL") String goodsURL,
                                              @RequestParam("goodsPrice") String goodsPrice,
                                              @RequestParam("keywordList") List<String> keywordListJson,
                                              @RequestParam("goodsFile") MultipartFile goodsFile) throws IOException {

//        try {

        String keywordlist = keywordListJson.toString();

        String keyword = keywordListJson.toString().substring(1, keywordlist.length() - 1);
        String originalEncodingFilename = Normalizer.normalize(goodsFile.getOriginalFilename(), Normalizer.Form.NFC);
        UUID uuid = UUID.randomUUID();
        String uuidString = uuid.toString();
        String originalFilename = originalEncodingFilename;
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String newFilename = uuidString + "_" + originalFilename;
        GoodsFile newGoodsFile = new GoodsFile();
        Goods newGoods = new Goods();

        newGoodsFile.setGoodsFileID(newFilename);
        newGoodsFile.setGoodsFileName(originalFilename);
        newGoodsFile.setGoodsFileExt(fileExtension);
        newGoodsFile.setGoodsFileSize(goodsFile.getSize());
        newGoodsFile.setGoodsFilePath(url);

        newGoods.setGoodsId(goodsId);
        newGoods.setGoodsName(goodsTitle);
        newGoods.setGoodsBrand(goodsBrand);
        newGoods.setGoodsPrice(Integer.parseInt(goodsPrice));
        newGoods.setGoodsPurchsLink(goodsURL);
        newGoods.setGoodsKwrd(keyword);

        Path path = Paths.get(uploadPath + url).toAbsolutePath().normalize();

//        newFilename = new String(newFilename.getBytes(StandardCharsets.ISO_8859_1),"UTF-8");
        Path realPath = path.resolve(newFilename).normalize();

        //TODO 수정으로 바꾸기
        int rowsAffected = goodsService.updateGoods(newGoods, newGoodsFile);
        goodsFile.transferTo(realPath);

        if (rowsAffected <= 0) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("goods 업로드 실패");
        }
        return ResponseEntity.ok("goods 업로드 및 처리 완료");
//TODO try catch 문 작성

//        }
//        catch (IOException e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("굿즈 업로드 실패");
//        }    // JSON 문자열을 객체로 변환 (keywordListJson)


    }


    //상품 수정 파일 없음
    @PatchMapping("/form/nofile")
    public ResponseEntity<String> updateGoodsnoFile(@RequestParam("goodsId") int goodsId,
                                                    @RequestParam("goodsTitle") String goodsTitle,
                                                    @RequestParam("goodsBrand") String goodsBrand,
                                                    @RequestParam("goodsURL") String goodsURL,
                                                    @RequestParam("goodsPrice") String goodsPrice,
                                                    @RequestParam("keywordList") List<String> keywordListJson
    ) {
//        try {

        String keywordlist = keywordListJson.toString();

        String keyword = keywordListJson.toString().substring(1, keywordlist.length() - 1);

        Goods newGoods = new Goods();


        newGoods.setGoodsId(goodsId);
        newGoods.setGoodsName(goodsTitle);
        newGoods.setGoodsBrand(goodsBrand);
        newGoods.setGoodsPrice(Integer.parseInt(goodsPrice));
        newGoods.setGoodsPurchsLink(goodsURL);
        newGoods.setGoodsKwrd(keyword);


        //TODO 수정으로 바꾸기
        goodsService.updateGoods(newGoods);


        return ResponseEntity.ok("goods 업로드 및 처리 완료");
//TODO try catch 문 작성


    }

    // 컨텐츠 생성 폼에서 굿즈 리스트
    @GetMapping("/cntnt")
    public String selectGoodsListInMakeCntnt() {
        return "cms/goods/new-goods-main-in-cntnt-make-form";
    }

    @GetMapping("/cntnt/{page}")
    public String getGoodsPagesInMakeCntnt(@PathVariable int page, HttpSession session, Model model) {

        session.removeAttribute("message");
        session.setAttribute("page", page);

        List<Goods> goodsList = goodsService.getAllGoodsJFile(page);
        model.addAttribute("goodsList", goodsList);

        int bbsCount = goodsService.totalGoods();
        int totalPage = 0;

        if (bbsCount > 0) {
            totalPage = (int) Math.ceil(bbsCount / 10.0);
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

        session.setAttribute("nowPage", page);


        return "cms/goods/new-goods-list-in-cntnt-make-form";
    }




}

