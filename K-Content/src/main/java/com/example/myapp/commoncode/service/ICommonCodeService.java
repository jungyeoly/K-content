package com.example.myapp.commoncode.service;

import java.util.List;

import com.example.myapp.commoncode.model.CommonCode;

public interface ICommonCodeService {

	List<CommonCode> findCommonCateCodeByUpperCommonCode(String upperCommonCode);
	CommonCode findByCommonCode(String commonCode);

	List<String> cateList(String upperCommonCode);

	String mberStatByCode(String mberId);

	String mberRoleByCode(String mberId);

	String mberGenderByCode(String mberId);
	
	List<CommonCode> findByCommonCodeVal(String CommonCodeVal);
}
