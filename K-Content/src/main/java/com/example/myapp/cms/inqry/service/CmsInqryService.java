package com.example.myapp.cms.inqry.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.myapp.cms.inqry.dao.ICmsInqryRepository;
import com.example.myapp.cms.inqry.model.CmsInqry;

@Service
public class CmsInqryService implements ICmsInqryService, ICmsInqryRepository {

	@Autowired
	ICmsInqryRepository cmsInqryRepository;
	
	@Override
	public List<CmsInqry> selectCmsInqryList() {
		return cmsInqryRepository.selectCmsInqryList();
	}

}
