package com.example.myapp.user.commu.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

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
	public List<Commu> selectAllPost(int page) {
		int start = (page - 1) * 10 + 1;
		return commuRepository.selectAllPost(start, start + 9);
	}

	@Transactional
	public void insertPost(Commu commu) {
		String currentTimestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		if (commu.getCommuRegistDate() == null) {
			commu.setCommuRegistDate(currentTimestamp);
		}

		commuRepository.insertPost(commu);
	}

	@Transactional
	public void insertPostwithFiles(Commu commu, List<CommuFile> files) {
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
	public CommuFile getFile(String commuFileId) {
		return commuRepository.getFile(commuFileId);
	}

	//게시글만 조회
		public Commu selectPostWithoutIncreasingReadCnt(int commuId) {
		    return commuRepository.selectPost(commuId);
		}
		//조회수 증가
		@Transactional
		public void increaseReadCnt(int commuId) {
		    commuRepository.updateReadCnt(commuId);
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
				// commuFileId 값이 없는 경우 새 파일로 간주
				if (file.getCommuFileId() == null || file.getCommuFileId().trim().isEmpty()) {
					file.setCommuFileId(UUID.randomUUID().toString()); // 새 uuid 생성
					if (file.getCommuFileName() != null && !file.getCommuFileName().isEmpty()) {

						file.setCommuFileCommuId(commu.getCommuId());
						commuRepository.insertFileData(file); // 새 파일 정보를 DB에 추가

					}
				}
				// commuFileId 값이 있는 경우 기존 파일로 간주
				else {
					if (file.getCommuFileName() != null && !file.getCommuFileName().isEmpty()) {
						commuRepository.updateFiledata(file); // 기존 파일 정보를 DB에서 업데이트
					} else {
						commuRepository.deleteFileById(file.getCommuFileId()); // 파일 정보를 DB에서 삭제
					}
				}
			}
		}

	}


	@Override
	public List<CommuFile> selectFilesByPostId(int commuId) {
		return commuRepository.selectFilesByPostId(commuId);
	}

	@Transactional
	public void deletePost(int commuId) {
		// 게시글과 연결된 첨부파일 목록을 가져옵니다.
		List<CommuFile> attachedFiles = selectFilesByPostId(commuId);
		// 각 첨부파일을 삭제합니다.
		for (CommuFile file : attachedFiles) {
			deleteFileById(file.getCommuFileId());
		}

		// 게시글 상태를 "삭제상태"로 변경합니다.
		commuRepository.deletePostStatus(commuId);

	}

	@Override
	public void deleteFileById(String commuFileId) {
		commuRepository.deleteFileById(commuFileId);

	}

	@Override
	public List<CommuFile> getAllFilesByCommuId(int commuId) {
		return commuRepository.getAllFilesByCommuId(commuId);
	}

	@Transactional
	public void reportPost(int commuId) {
		Commu commu = commuRepository.selectPost(commuId);
		if (commu == null) {
			throw new IllegalArgumentException("해당 게시글이 존재하지 않습니다.");
		}

		commuRepository.reportPost(commuId);
	}



	public int totalCommu() {
		return commuRepository.totalCommu();
	}

	@Override
	public List<Commu> selectPostListByCategory(String commuCateCode, int page) {
		int start = (page - 1) * 10 + 1;
		return commuRepository.selectPostListByCategory(commuCateCode, start, start+9);
	}

	@Override
	public int totalCommuByCategory(String commuCateCode) {
		return commuRepository.totalCommuByCategory(commuCateCode);
	}

	@Override
	public List<Commu> searchListByContentKeyword(String keyword, int page) {
		int start = (page-1)*10 +1;
		return commuRepository.searchListByContentKeyWord("%"+keyword+"%", start, start+9);
	}

	@Override
	public int selectTotalPostCountByKeyWord(String keyword) {
		return commuRepository.selectTotalPostCountByKeyWord("%"+keyword+"%");
	}

	@Override
	public List<Commu> selectRecentNotice() {
		return commuRepository.selectRecentNotice();
	}

}
