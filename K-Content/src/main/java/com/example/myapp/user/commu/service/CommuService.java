package com.example.myapp.user.commu.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.myapp.user.commu.dao.ICommuRepository;
import com.example.myapp.user.commu.model.Commu;
import com.example.myapp.user.commu.model.CommuFile;

@Service
public class CommuService implements ICommuService {

	@Autowired
	ICommuRepository commuRepository;

	@Override
	public List<Commu> selectAllPost() {
		List<Commu> commuList = commuRepository.selectAllPost();
		return commuList;
	}

	@Transactional
	public void insertPost(Commu commu) {
		commu.setCommuId(commuRepository.selectMaxPost()+1);
		commuRepository.insertPost(commu);

	}

	@Transactional
	public void insertPost(Commu commu, CommuFile file) {
		commu.setCommuId(commuRepository.selectMaxPost()+1);
		commuRepository.insertPost(commu);
		if(file != null && file.getCommuFileName() != null &&
				! file.getCommuFileName().equals("")) {
			file.setCommuFileCommuId(commu.getCommuId());
			commuRepository.insertFileData(file);
		}
	}

	@Override
	public CommuFile getFile(int commuFileId) {
		return commuRepository.getFile(commuFileId);
	}









}
