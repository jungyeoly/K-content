package com.example.myapp.user.inqry.dao;

import java.util.List;

import com.example.myapp.user.inqry.model.Inqry;
import com.example.myapp.user.inqry.model.InqryFile;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;


@Repository
@Mapper
public interface IInqryRepository {
	List<Inqry> selectInqryList(@Param("start") int start, @Param("end") int end);

	Inqry selectInqry(int inqryId);

	int totalInqry();

	void insertInqry(Inqry inqry);

	void insertFile(InqryFile file);

	int selectinqryFileId();

	void updateInqry(Inqry inqry);

	void updateInqryFile(InqryFile file);

	void deleteInqry(int inqryId);

	void deleteInqryFile(String inqryFileId);

	String getInqryFileId(int inqryId);

	void updateCmsInqry(Inqry inqry);

}
