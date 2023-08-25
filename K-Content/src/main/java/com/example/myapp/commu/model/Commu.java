package com.example.myapp.commu.model;

import com.example.myapp.commu.status.CommuStatus;
import com.example.myapp.commu.status.CommuType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Commu {
	private int commuId; //게시글 ID (글번호 1,2,3)
	private String commuTitle; // 게시글 제목
	private String commuCntnt; // 게시글 내용
	private String commuRegistDate; // 등록일시
	private String commuUpdateDate; // 수정일시
	private CommuStatus commuStatus; //게시글 상태 NO:신고 안한거 YES:사용자가 삭제, REPORT:사용자가 신고, KILL:관리자가 신고된거 삭제처리
	private CommuType commuType; // 글종류 (NORMAL:일반글, ANNOUNCEMENT:공지글
	private int commuReadCnt; //조회수
	private int commuRefId; //참조댓글 ID
	private int commuDepth; // 댓글 깊이
	private int commuOrder; // 댓글 순서
	private String commuCateCode; // 게시판 카테고리 코드 (공통코드)
	private String commuMberId; //회원 ID 
	

}