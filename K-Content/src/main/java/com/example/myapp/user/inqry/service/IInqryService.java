package com.example.myapp.user.inqry.service;

import java.util.List;

import com.example.myapp.user.inqry.model.Inqry;

public interface IInqryService {
	List<Inqry> selectInqryList(int page);
	Inqry selectInqry(int inqryId);

	int totalInqry();

	void insertInqry(Inqry inqry);
}
