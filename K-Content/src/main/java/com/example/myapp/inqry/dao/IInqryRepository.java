package com.example.myapp.inqry.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.example.myapp.inqry.model.Inqry;

@Repository
@Mapper
public interface IInqryRepository {
	List<Inqry> selectInqryList();
}
