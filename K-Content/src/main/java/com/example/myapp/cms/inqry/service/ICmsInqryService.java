package com.example.myapp.cms.inqry.service;

import java.util.List;

import com.example.myapp.cms.inqry.model.CmsInqry;

public interface ICmsInqryService {
	List<CmsInqry> selectCmsInqryList(int page);
	CmsInqry selectCmsInqry(int inqryId);
}
