package com.example.myapp.inqry.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.myapp.inqry.dao.IInqryRepository;
import com.example.myapp.inqry.model.Inqry;
import com.example.myapp.inqry.model.InqryFile;

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
		return inqryRepository.selectInqry(inqryId);
	}

	@Override
	public int totalInqry() {
		return inqryRepository.totalInqry();
	}

	@Transactional
	public void insertInqry(Inqry inqry) {
		inqryRepository.insertInqry(inqry);
	}

	@Transactional
	public void insertInqry(Inqry inqry, InqryFile file) {
		inqryRepository.insertInqry(inqry);
		if(file != null && file.getInqryFileId() != null && !file.getInqryFileId().equals("")) {
			inqryRepository.insertFile(file);
		}
	}

	@Override
	public int selectinqryFileId() {
		return inqryRepository.selectinqryFileId();
	}	
}
