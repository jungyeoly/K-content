package com.example.myapp.user.mber.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.myapp.user.mber.dao.IMberRepository;
import com.example.myapp.user.mber.model.Mber;

@Service
public class MberService implements IMberService {

	@Autowired
	IMberRepository mberRepository;
	
	@Override
	public Mber selectMberbyId(String mberId) {
		return mberRepository.selectMberbyId(mberId);
	}

	@Override
	public List<Mber> selectMberAllList() {
		return mberRepository.selectMberAllList();
	}

	@Override
	public void insertMber(Mber mber) {
		mberRepository.insertMber(mber);
	}

	@Override
	public void updateMber(Mber mber) {
		mberRepository.updateMber(mber);
	}

	@Override
	public void deleteMber(String mberId) {
		mberRepository.deleteMber(mberId);
	}

}
