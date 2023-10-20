package com.example.myapp.cms.content.service;

import com.example.myapp.cms.content.dao.ICntntGoodsMappingRepository;
import com.example.myapp.cms.content.dao.IContentRepository;
import com.example.myapp.cms.content.model.CmsContent;
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
    public List<CmsContent> getAllContent(String commonCodeVal, int page) {
    	int start = (page-1)*9 + 1;
        return contentRepository.getAllContent(commonCodeVal, start, start+8);
    }

    @Override
    public CmsContent getAContent(int id) {
        return contentRepository.getAContent(id);
    }

    @Override
    // 굿즈 매핑 트랜잭션
    @Transactional
    public int insertAContent(CmsContent contentForm, List<Integer> goodsList) {
        int contentId = 0;
        int rowsAffected = 0;
        int rowsAffectedCntnt = 0;
        List<Boolean> resultList = new ArrayList<>();
        try {
            rowsAffectedCntnt =  contentRepository.insertAContent(contentForm);
            contentId = contentForm.getCntntId();

            for (int i = 0; i < goodsList.size(); i++) {
                int goodsId = goodsList.get(i);
                rowsAffected = cntntGoodsMappingRepository.insertMappingDate(contentId, goodsId);
            }
            // INSERT 작업의 성공 여부 확인
            if (rowsAffectedCntnt > 0 && rowsAffected > 0) {
                return rowsAffectedCntnt;
            } else {
                return rowsAffectedCntnt; // 실패
            }
        } catch (Exception e) {
            // 예외 처리
            e.printStackTrace();
            return rowsAffectedCntnt; // 실패
        }

        //cntnt Goods mapping insert


    }

    @Override
    @Transactional
    public int updateAContent(CmsContent contentForm, List<Integer> goodsList) {
        int rowsAffected = 0;
        List<Boolean> resultList = new ArrayList<>();
        try {
            contentRepository.updateAContent(contentForm);
            int contentId = contentForm.getCntntId();

            cntntGoodsMappingRepository.delMappingDate(contentId);
            for (int i = 0; i < goodsList.size(); i++) {
                int goodsId = goodsList.get(i);
                rowsAffected = cntntGoodsMappingRepository.insertMappingDate(contentId, goodsId);
            }
            // INSERT 작업의 성공 여부 확인
            if (contentId > 0 && rowsAffected > 0) {
                return rowsAffected;
            } else {
                return rowsAffected; // 실패
            }
        } catch (Exception e) {
            // 예외 처리
            e.printStackTrace();
            return rowsAffected; // 실패
        }

        //cntnt Goods mapping insert
    }

    @Override
    public List<CmsContent> getContentByKeyword(List<String> keywordList) {
        return  contentRepository.getContentByKeyword(keywordList);
    }

    @Override
    public void updateDelStat(int cntntId) {
         contentRepository.updateDelStat(cntntId);
    }

	@Override
	public int totalCntnt(String commonCodeVal) {
		return contentRepository.totalContent(commonCodeVal);
	}

	@Override
	public int totalSearch(List<String> keywordList) {
		return contentRepository.totalSearch(keywordList);
	}

	@Override
	public List<CmsContent> getPagingContentBySearch(List<String> keywordList, int page) {
		int start = (page-1)*9 + 1;
		return contentRepository.getPagingContentBySearch(keywordList, start, start+8);
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




