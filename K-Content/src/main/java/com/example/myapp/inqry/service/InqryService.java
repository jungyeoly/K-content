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
	public List<Inqry> selectInqryList() {
		return inqryRepository.selectInqryList();
	}
	
}
