package com.example.myapp.user.bkmk.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.myapp.user.bkmk.dao.IBkmkRepository;
import com.example.myapp.user.bkmk.model.CntntBkmk;

@Service
public class BkmkService implements IBkmkService {

	@Autowired
	IBkmkRepository bkmkRepository;

	@Override
	public List<CntntBkmk> selectCntntBkmkList(String mberId) {
		return bkmkRepository.selectCntntBkmkList(mberId);
	}

	@Override
	public void insertCntntBkmkList(String mberId, int cntntId) {
		 bkmkRepository.insertCntntBkmkList(mberId, cntntId);
	}

	@Override
	public void deleteCntntBkmkList(String mberId, int cntntId) {
		bkmkRepository.deleteCntntBkmkList(mberId, cntntId);
	}

	@Override
	public int selectCntntBkmk(String mberId, int cntntId) {
		return bkmkRepository.selectCntntBkmk(mberId, cntntId);
	}

}
