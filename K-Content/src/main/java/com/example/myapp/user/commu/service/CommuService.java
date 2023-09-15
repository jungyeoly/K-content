package com.example.myapp.user.commu.service;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
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
		String currentTimestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		if (commu.getCommuRegistDate() == null) {
			commu.setCommuRegistDate(currentTimestamp);
		}

		
		commuRepository.insertPost(commu);
	}

	// @Transactional
	public void insertPost(Commu commu, List<CommuFile> files) {
		String currentTimestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));


		// Commu 객체에 대한 등록 날짜 설정
		if (commu.getCommuRegistDate() == null) {
			commu.setCommuRegistDate(currentTimestamp);
		}

		
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

	@Transactional
	public void updatePost(Commu commu) {
		String currentTimestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));


		if (commu.getCommuUpdateDate() == null) {
			commu.setCommuUpdateDate(currentTimestamp);
		}
		commuRepository.updatePost(commu);
	}

	@Transactional
	public void updatePostAndFiles(Commu commu, List<CommuFile> files) {
		String currentTimestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));


		  if (commu.getCommuUpdateDate() == null) {
		        commu.setCommuUpdateDate(currentTimestamp);
		    }

		    commuRepository.updatePost(commu);

		    if (files != null && !files.isEmpty()) {
		        for (CommuFile file : files) {
		            // 새로운 파일 추가
		            if (file.getCommuFileName() != null && !file.getCommuFileName().equals("") && 
		                (file.getCommuFileId() == null || file.getCommuFileId().equals(""))) {
		                
		                file.setCommuFileCommuId(commu.getCommuId());
		                commuRepository.insertFileData(file);
		                
		            } else if (file.getCommuFileId() != null && !file.getCommuFileId().equals("")) {
		                // 기존 파일 업데이트 혹은 삭제
		                if (file.getCommuFileName() != null && !file.getCommuFileName().equals("")) {
		                    commuRepository.updateFiledata(file);
		                } else {
		                    commuRepository.deleteFileById(file.getCommuFileId());
		                }
		            }
		        }
		    }
		}
	
	
	
	  @Override 
	  public List<Commu> selectPostListByCategory(String commuCateCode, int page) {
		  int start = (page-1)*10 +1;
		  return commuRepository.selectPostListByCategory(commuCateCode, start, start+9); 
	  }

	@Override
	public List<CommuFile> selectFilesByPostId(int commuId) {
		return commuRepository.selectFilesByPostId(commuId);
	}

	@Override
	public void deletePost(int commuId) {
		commuRepository.deletePostStatus(commuId);
		
	}

	@Override
	public void deleteFileById(String commuFileId) {
		commuRepository.deleteFileById(commuFileId);
		
	}

}
