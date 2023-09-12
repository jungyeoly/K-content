package com.example.myapp.cms.inqry.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.myapp.cms.inqry.dao.ICmsInqryRepository;
import com.example.myapp.cms.inqry.model.CmsInqry;

@Service
public class CmsInqryService implements ICmsInqryService{

	@Autowired
	ICmsInqryRepository cmsInqryRepository;
	
	@Override
	public List<CmsInqry> selectCmsInqryList(int page) {
		int start = (page-1)*10 + 1;
		return cmsInqryRepository.selectCmsInqryList(start, start+9);
	}

	@Override
	public CmsInqry selectCmsInqry(int inqryId) {
		return cmsInqryRepository.selectCmsInqry(inqryId);
	}

}
