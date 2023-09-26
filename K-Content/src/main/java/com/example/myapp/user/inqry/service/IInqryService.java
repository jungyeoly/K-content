package com.example.myapp.user.inqry.service;

import java.util.List;
import com.example.myapp.user.inqry.model.Inqry;
import com.example.myapp.user.inqry.model.InqryFile;

public interface IInqryService {
	List<Inqry> selectInqryList(int page);
	Inqry selectInqry(int inqryId);

	int totalInqry();

	void insertInqry(Inqry inqry);
	void insertInqry(Inqry inqry, InqryFile file);
	
	int selectinqryFileId();
	
	void updateInqry(Inqry inqry);
	void updateInqry(Inqry inqry, InqryFile file);
	
	String getInqryFileId(int inqryId);
	
	void deleteInqry(int inqryId);
	void deleteInqry(int inqryId, String inqryFileId);
}
