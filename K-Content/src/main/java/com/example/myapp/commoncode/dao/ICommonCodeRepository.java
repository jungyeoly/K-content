package com.example.myapp.commoncode.dao;


import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.example.myapp.commoncode.model.CommonCode;


@Repository
@Mapper
public interface ICommonCodeRepository  {
	
	List<CommonCode> findByUpperCommonCode(String upperCommonCode);

	

}
