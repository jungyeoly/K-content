package com.example.myapp.cms.commu.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.myapp.cms.commu.dao.ICmsCommuRepository;
import com.example.myapp.cms.commu.model.CmsCommu;

@Service
public class CmsCommuService implements ICmsCommuService {

	@Autowired
	ICmsCommuRepository cmsCommuRepository;
	
	@Override
	public List<CmsCommu> selectAllUserPost(String commonCodeVal, int page) {
		int start = (page - 1) * 10 + 1;
		return cmsCommuRepository.selectAllUserPost(commonCodeVal, start, start + 9);
	}

	@Override
	public CmsCommu selectUserPost(int commuId) {
		return cmsCommuRepository.selectUserPost(commuId);
		
	}
	
	

}
