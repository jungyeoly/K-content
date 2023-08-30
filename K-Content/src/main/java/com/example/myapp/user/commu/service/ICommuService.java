package com.example.myapp.user.commu.service;

import java.util.List;

import com.example.myapp.user.commu.model.Commu;
import com.example.myapp.user.commu.model.CommuFile;

public interface ICommuService {
	
	List<Commu> selectAllPost();
	
	Commu selectPost(int commuId);
	
	void insertPost(Commu commuId);
	void insertPost(Commu commuId, CommuFile file);
	
	CommuFile getFile(int commuFileid);
	
	List<Commu> selectAllPost();

	void insertPost(Commu commuId);
	void insertPost(Commu commuId, CommuFile file);

	CommuFile getFile(int commuFileid);
}

