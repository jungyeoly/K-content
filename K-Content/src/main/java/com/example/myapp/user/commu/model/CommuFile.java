package com.example.myapp.user.commu.model;


import com.example.myapp.user.commu.status.CommuFileDeleteYn;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommuFile {
	 private String commuFileId; //파일 ID(랜덤으로 생성된 파일 고유번호)
	    private String commuFileName; // 파일이름
	    private long commuFileSize; //파일크기
	    private String commuFileExt; //파일 확장자
	    private String commuFilePath; //파일경로

	    private String commuFileRegistDate; //등록일시  
	    private String commuFileUpdateDate; //수정일시
	    private CommuFileDeleteYn commuFileDeleteYn; // 삭제여부 Yes, No로 구분
	    private long commuFileCommuId; // 게시글 ID (첨부파일이 있는 글번호)
}
