package com.example.myapp.user.mber.service;

import java.util.List;

import com.example.myapp.user.mber.model.Mber;

public interface IMberService {
	Mber selectMberbyId(String mberId);

	Mber selectMberbyEmail(String mberId);

	Mber selectMberbyIdEmail(String mberId, String mberEmail);

	List<Mber> selectMberAllList();

	void insertMber(Mber mber);

	void updateMber(Mber mber);

	void deleteMber(String mberId);

}
