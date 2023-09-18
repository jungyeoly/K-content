package com.example.myapp.cms.content.service;

import com.example.myapp.cms.content.dao.ICntntGoodsMappingRepository;
import com.example.myapp.cms.content.dao.IContentRepository;
import com.example.myapp.cms.content.model.CmsContent;
import com.example.myapp.cms.content.model.CntntInsertForm;
import com.example.myapp.cms.goods.dao.IGoodsRepository;
import com.example.myapp.cms.goods.service.IGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ContentService implements IContentService {

    @Autowired
    IContentRepository contentRepository;
    @Autowired
    ICntntGoodsMappingRepository cntntGoodsMappingRepository;

    @Override
    public List<CmsContent> getAllContent() {
        return contentRepository.getAllContent();
    }

    @Override
    public CmsContent getAContent(int id) {
        return contentRepository.getAContent(id);
    }

    @Override
    // 굿즈 매핑 트랜잭션
    @Transactional
    public boolean insertAContent(CmsContent contentForm, List<Integer> goodsList) {
        int contentId = 0;
        int rowsAffected = 0;
        List<Boolean> resultList = new ArrayList<>();
        try {
            contentRepository.insertAContent(contentForm);
            contentId = contentForm.getCntntId();

            for (int i = 0; i < goodsList.size(); i++) {
                int goodsId = goodsList.get(i);
                rowsAffected = cntntGoodsMappingRepository.insertMappingDate(contentId, goodsId);
            }
            // INSERT 작업의 성공 여부 확인
            if (contentId > 0 && rowsAffected > 0) {
                return true;
            } else {
                return false; // 실패
            }
        } catch (Exception e) {
            // 예외 처리
            e.printStackTrace();
            return false; // 실패
        }

        //cntnt Goods mapping insert


    }

    @Override
    @Transactional
    public boolean updateAContent(CmsContent contentForm, List<Integer> goodsList) {
        int rowsAffected = 0;
        List<Boolean> resultList = new ArrayList<>();
        try {
            contentRepository.updateAContent(contentForm);
            int contentId = contentForm.getCntntId();
            System.out.println("contentId: "+contentId);
            cntntGoodsMappingRepository.delMappingDate(contentId);
            for (int i = 0; i < goodsList.size(); i++) {
                int goodsId = goodsList.get(i);
                rowsAffected = cntntGoodsMappingRepository.insertMappingDate(contentId, goodsId);
            }
            // INSERT 작업의 성공 여부 확인
            if (contentId > 0 && rowsAffected > 0) {
                return true;
            } else {
                return false; // 실패
            }
        } catch (Exception e) {
            // 예외 처리
            e.printStackTrace();
            return false; // 실패
        }

        //cntnt Goods mapping insert
    }

    @Override
    public List<CmsContent> getContentByKeyword(List<String> keywordList) {
        return  contentRepository.getContentByKeyword(keywordList);
    }



}
//         contentRepository.insertAContent(contentForm);
//        int cntntId =content.getCntntId();
//
//        for(int i=0;)
//
//        int goodsId =content.get굿즈 아이디 ();
//        contentRepository.insertACntntGoodsMapping(cntntId, goodsId);


//    @Scheduled(cron="0/20 * * * * ?")
//    public void crol(){

//    }




