package com.example.myapp.inqry.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.myapp.inqry.dao.IInqryRepository;
import com.example.myapp.inqry.model.Inqry;

@Service
public class InqryService implements IInqryService{

	@Autowired
	IInqryRepository inqryRepository;
	
	@Override
	public List<Inqry> selectInqryList(int page) {
		int start = (page-1)*10 + 1;
		return inqryRepository.selectInqryList(start, start+9);
	}

	@Override
	public Inqry selectInqry(int inqryId) {
		// TODO Auto-generated method stub
		return inqryRepository.selectInqry(inqryId);
	}

	@Override
	public int totalInqry() {
		// TODO Auto-generated method stub
		return inqryRepository.totalInqry();
	}

	@Override
	public void insertInqry(Inqry inqry) {
		inqryRepository.insertInqry(inqry);
	}
	
}
