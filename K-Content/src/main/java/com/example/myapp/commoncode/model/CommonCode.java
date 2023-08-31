package com.example.myapp.commoncode.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommonCode {
	 private String commonCode; //공통코드
	    private String upperCommonCode; //상위공통코드
	    private String commonCodeVal; //공통코드명
	    private String commonCodeDscr; //코드설명
	    private String commonRegistDate; //공통코드 등록일
	    private String commonUpdateDate; // 공통코드 수정일
	    private char commonDeleteYn; //공통코드 삭제여부
	    private Integer commonOrder; // 공통코드 순서

}
