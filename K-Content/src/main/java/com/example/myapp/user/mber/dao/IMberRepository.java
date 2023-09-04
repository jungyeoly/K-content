package com.example.myapp.user.mber.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.example.myapp.user.mber.model.Mber;

@Mapper
@Repository
public interface IMberRepository {
	Mber selectMberbyId(String mberId);

	List<Mber> selectMberAllList();

	void insertMber(Mber mber);

	void updateMber(Mber mber);

	void deleteMber(String mberId);
	
//	String maskingMberId(String mberEmail);
}
