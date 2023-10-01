package com.example.myapp.user.mber.service;

import java.util.List;

import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	public Mber selectMberbyEmail(String mberEmail) {
		return mberRepository.selectMberbyEmail(mberEmail);
	}

	@Override
	public Mber selectMberbyIdEmail(String mberId, String mberEmail) {
		return mberRepository.selectMberbyIdEmail(mberId, mberEmail);
	}

	@Override
	public List<Mber> selectMberList() {
		return mberRepository.selectMberList();
	}

	@Override
	public int getMberTotalCount() {
		return mberRepository.getMberTotalCount();
	}

	@Override
	public void insertMber(Mber mber) {
		mberRepository.insertMber(mber);
	}

	@Override
	public void updateMber(Mber mber) {
		mberRepository.updateMber(mber);
	}

	@Transactional
	@Override
	public void deleteMber(String mberId) throws NotFoundException {
		// 사용자의 계정 정보를 조회하여 유효성 검사 (selectMberbyId 사용)
		Mber mber = mberRepository.selectMberbyId(mberId);
		if (mber != null) {
			// 계정의 상태를 변경하여 탈퇴 처리 (deleteMber 사용)
			mberRepository.deleteMber(mber.getMberId());
		} else {
			// 사용자가 존재하지 않을 경우 예외 처리 또는 오류 메시지 표시
			throw new NotFoundException("사용자를 찾을 수 없습니다.");
		}
	}

	@Override
	public String mberGenderCodeById(String mberId) {
		return mberRepository.mberGenderCodeById(mberId);
	}

	@Override
	public boolean isMberId(String mberId) {
		return mberRepository.isMberId(mberId);
	}

	@Override
	public boolean isMberEmail(String mberEmail) {
		return mberRepository.isMberEmail(mberEmail);
	}
	
	@Override
	 public void changeMberStatus(String mberId, String newStatus) {
		mberRepository.changeMberStatus(mberId, newStatus);
	}
}
