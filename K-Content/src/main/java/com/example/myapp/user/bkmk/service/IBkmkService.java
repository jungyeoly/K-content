package com.example.myapp.user.bkmk.service;

import java.util.List;

import com.example.myapp.user.bkmk.model.CntntBkmk;

public interface IBkmkService {
	List<CntntBkmk> selectCntntBkmkList(String mberId);
}
