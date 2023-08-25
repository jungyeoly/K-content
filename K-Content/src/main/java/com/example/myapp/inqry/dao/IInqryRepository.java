package com.example.myapp.inqry.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.example.myapp.inqry.model.Inqry;
import com.example.myapp.inqry.model.InqryFile;

@Repository
@Mapper
public interface IInqryRepository {
	// 문의사항 전체조회(페이징)
	List<Inqry> selectInqryList(@Param("start") int start, @Param("end") int end);
	
	// 문의사항 상세조회
	Inqry selectInqry(int inqryId);
	
	// 페이징 처리를 위한 전체 문의사항글 개수
	int totalInqry();
	
	// 문의사항 글 쓰기
	void insertInqry(Inqry inqry);
	
	// 문의사항 파일 업로드
	void insertFileData(InqryFile file);
}
