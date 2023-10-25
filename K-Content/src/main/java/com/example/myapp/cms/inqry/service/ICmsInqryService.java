package com.example.myapp.cms.inqry.service;

import java.util.List;

import com.example.myapp.cms.inqry.model.CmsInqry;

public interface ICmsInqryService {
	List<CmsInqry> selectCmsInqryList(int page);
	List<CmsInqry> selectUnansInqryList(int page);
	CmsInqry selectCmsInqry(int inqryId);
	void writeCmsInqry(CmsInqry cmsInqry);
	int countInqry(int inqryRefId);
	int countAns();
	void updateCmsInqry(CmsInqry cmsInqry);
	void deleteCmsInqry(int inqryId);
	List<CmsInqry> selectRecentInqry();
	String getCmsInqry(int inqryRefId);
	CmsInqry selectCmsReply(int inqryId);
}
