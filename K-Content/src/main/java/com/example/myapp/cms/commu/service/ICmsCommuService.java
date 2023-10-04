package com.example.myapp.cms.commu.service;

import java.util.List;

import com.example.myapp.cms.commu.model.CmsCommu;
import com.example.myapp.user.commu.model.Commu;

public interface ICmsCommuService {
	
	List<CmsCommu> selectAllUserPost(String commonCodeVal, int page); //커뮤니티 전체글 다 보기

	CmsCommu selectUserPost(int commuId); // 커뮤니티 게시글 상세조회
}
