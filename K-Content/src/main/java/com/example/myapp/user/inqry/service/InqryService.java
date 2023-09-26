package com.example.myapp.user.inqry.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.myapp.user.inqry.dao.IInqryRepository;
import com.example.myapp.user.inqry.model.Inqry;
import com.example.myapp.user.inqry.model.InqryFile;

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

	@Override
	public void updateInqry(Inqry inqry) {
		inqryRepository.updateInqry(inqry);
	}

	@Transactional
	public void updateInqry(Inqry inqry, InqryFile file) {
		inqryRepository.updateInqry(inqry);

		if(file != null && file.getInqryFileId() != null && !file.getInqryFileId().equals("")) {
			if(file.getInqryFileInqryId()>0) {
				inqryRepository.updateInqryFile(file);
			} else {
				file.setInqryFileInqryId(inqry.getInqryId());
				inqryRepository.insertFile(file);
			}
		}
	}

	@Override
	public String getInqryFileId(int inqryId) {
		return inqryRepository.getInqryFileId(inqryId);
	}

	@Transactional
	public void deleteInqry(int inqryId) {
		inqryRepository.deleteInqry(inqryId);		
	}
	
	@Transactional
	public void deleteInqry(int inqryId, String inqryFileId) {
		if(inqryFileId != null && !inqryFileId.equals("")) {
			inqryRepository.deleteInqryFile(inqryFileId);
			inqryRepository.deleteInqry(inqryId);
		}
	}
}
