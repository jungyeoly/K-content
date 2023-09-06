package com.example.myapp.user.commu.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
		String currentTimestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		if (commu.getCommuRegistDate() == null) {
			commu.setCommuRegistDate(currentTimestamp);
		}

		commu.setCommuId(commuRepository.selectMaxPost() + 1);
		commuRepository.insertPost(commu);
	}

	@Transactional
	public void insertPost(Commu commu, List<CommuFile> files) {
		String currentTimestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

		// Commu 객체에 대한 등록 날짜 설정
		if (commu.getCommuRegistDate() == null) {
			commu.setCommuRegistDate(currentTimestamp);
		}

		commu.setCommuId(commuRepository.selectMaxPost() + 1);
		commuRepository.insertPost(commu);

		 if (files != null && !files.isEmpty()) {
		        for (CommuFile commufile : files) {
		            if (commufile.getCommuFileName() != null && !commufile.getCommuFileName().equals("")) {
		                commufile.setCommuFileCommuId(commu.getCommuId());
		                commuRepository.insertFileData(commufile); 
				}
			}
		}
	}

	@Override
	public CommuFile getFile(int commuFileId) {
		return commuRepository.getFile(commuFileId);
	}

	@Transactional
	public Commu selectPost(int commuId) {
		commuRepository.updateReadCnt(commuId);
		return commuRepository.selectPost(commuId);
	}

	@Override
	public void updatePost(Commu commu) {
		commuRepository.updatePost(commu);

	}

	@Override
	public void updatePost(Commu commu, CommuFile file) {
		commuRepository.updatePost(commu);
		if (file != null && file.getCommuFileName() != null && !file.getCommuFileName().equals("")) {
			file.setCommuFileCommuId(commu.getCommuId());

			if (file.getCommuFileId() != null && !file.getCommuFileId().equals("")) {
				// 파일 ID가 존재하면, 해당 파일 업데이트
				commuRepository.updateFiledata(file);
			} else {
				// 아니라면, 새로운 파일 추가
				// (이 부분은 필요하다면 파일 ID 생성 로직 추가)
				commuRepository.updateFiledata(file);
			}
		}

	}

	

}
