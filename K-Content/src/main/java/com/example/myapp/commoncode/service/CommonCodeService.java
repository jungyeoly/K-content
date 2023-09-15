package com.example.myapp.commoncode.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.myapp.commoncode.dao.ICommonCodeRepository;
import com.example.myapp.commoncode.model.CommonCode;

@Service
public class CommonCodeService implements ICommonCodeService {

	@Autowired
	private ICommonCodeRepository commonCodeRepository;

	@Override
	public List<CommonCode> findCommonCateCodeByUpperCommonCode(String upperCommonCode) {
		return commonCodeRepository.findByUpperCommonCode(upperCommonCode);
	}

	@Override
	public List<String> cateList(String upperCommonCode) {
		return commonCodeRepository.cateList(upperCommonCode);
	}

	@Override
	public String mberStatByCode(String mberId) {
		return commonCodeRepository.mberStatByCode(mberId);
	}
	@Override
	public String mberRoleByCode(String mberId) {
		return commonCodeRepository.mberRoleByCode(mberId);
	}
	@Override
	public String mberGenderByCode(String mberId) {
		return commonCodeRepository.mberGenderByCode(mberId);
	}
}