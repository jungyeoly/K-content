package com.example.myapp.user.mber.dao;

import java.util.List;
import java.util.Map;

import com.example.myapp.user.mber.model.Mber;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface IMberRepository {
	Mber selectMberbyId(String mberId);

	Mber selectMberbyEmail(String mberEmail);

	Mber selectMberbyIdEmail(String mberId, String mberEmail);

	List<Mber> selectMberList(@Param("start") int start, @Param("end") int end);

	int getMberTotalCount();

	int getActiveMberCount();

	void insertMber(Mber mber);

	void updateMber(Mber mber);

	void deleteMber(String mberId);

	String maskMberId(String mberEmail);

	String mberGenderCodeById(String mberId);

	boolean isMberId(String mberId);

	boolean isMberEmail(String mberEmail);

	void changeMberStatus(String mberId, String newStatus);

	List<Mber> searchMber(@Param("findType") String findType, @Param("findKeyword") String findKeyword, @Param("start") int start, @Param("end") int end);

	int cntSeach(@Param("findType") String findType, @Param("findKeyword") String findKeyword);

	List<Map<String, Object>> getGenderStat();

	List<Map<String, Object>> getSignupStat();
}
