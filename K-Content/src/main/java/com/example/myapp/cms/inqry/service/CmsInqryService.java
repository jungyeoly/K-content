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

	@Override
	public void writeCmsInqry(CmsInqry cmsInqry) {
		cmsInqryRepository.writeCmsInqry(cmsInqry);		
	}

	@Override
	public int countInqry(int inqryRefId) {
		return cmsInqryRepository.countInqry(inqryRefId);
	}

	@Override
	public void updateCmsInqry(CmsInqry cmsInqry) {
		cmsInqryRepository.updateCmsInqry(cmsInqry);
		
	}

	@Override
	public void deleteCmsInqry(int inqryId) {
		cmsInqryRepository.deleteCmsInqry(inqryId);
	}

	@Override
	public List<CmsInqry> selectRecentInqry() {
		return cmsInqryRepository.selectRecentInqry();
	}

	@Override
	public List<CmsInqry> selectUnansInqryList(int page) {
		int start = (page-1)*10 + 1;
		return cmsInqryRepository.selectUnansInqryList(start, start+9);
	}

	@Override
	public int countAns() {
		return cmsInqryRepository.countAns();
	}

	@Override
	public String getCmsInqry(int inqryRefId) {
		return cmsInqryRepository.getCmsInqry(inqryRefId);
	}

	@Override
	public CmsInqry selectCmsReply(int inqryId) {
		return cmsInqryRepository.selectCmsReply(inqryId);
	}

}