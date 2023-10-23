package com.example.myapp.cms.inqry.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.example.myapp.cms.inqry.model.CmsInqry;
import com.example.myapp.user.inqry.model.Inqry;

@Repository
@Mapper
public interface ICmsInqryRepository {
	List<CmsInqry> selectCmsInqryList(@Param("start") int start, @Param("end") int end);	// 조회
	List<CmsInqry> selectUnansInqryList(@Param("start") int start, @Param("end") int end);
	
	CmsInqry selectCmsInqry(int inqryId);	// 상세조회
	
	void writeCmsInqry(CmsInqry cmsInqry);	// 답글 쓰기 
	
	int countInqry(int inqryRefId);	// 답글 달린 글 여부 확인
	int countAns();
	void updateCmsInqry(CmsInqry inqry);	// 수정

	void deleteCmsInqry(int inqryId);	// 삭제
	
	List<CmsInqry> selectRecentInqry();
	
	String getCmsInqry(int inqryRefId);
}