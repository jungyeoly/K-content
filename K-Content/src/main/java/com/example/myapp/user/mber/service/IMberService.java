package com.example.myapp.user.mber.service;

import java.util.List;

import org.apache.ibatis.javassist.NotFoundException;

import com.example.myapp.user.mber.model.Mber;

public interface IMberService {
	Mber selectMberbyId(String mberId);

	Mber selectMberbyEmail(String mberEmail);

	Mber selectMberbyIdEmail(String mberId, String mberEmail);

	List<Mber> selectMberList(int page);
	
	int getMberTotalCount();

	void insertMber(Mber mber);

	void updateMber(Mber mber);

	void deleteMber(String mberId) throws NotFoundException;

	String mberGenderCodeById(String mberId);

	boolean isMberId(String mberId);

	boolean isMberEmail(String mberEmail);
	
	void changeMberStatus(String mberId, String newStatus);
}
