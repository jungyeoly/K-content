package com.example.myapp.user.commucomment.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommuComment {
	private int commuCommentId;//댓글의 고유 ID
	private int commuCommentCommuId; //게시글 Id(글번호 1,2,3) 
	private String commuCommentCntnt; //댓글 내용
	private String commuCommentRegistDate;//댓글 등록 일시
	private String commuCommentUpdateDate;//댓글 수정 일시
	private int commuCommentDepth; //댓글 깊이 (원본 게시글에 대한 댓글이나 대댓글)
	private int commuCommentOrder; //댓글 순서(댓글이 달린 순서대로 증가)
	private String commuCommentMberId; // 댓글 작성한 회원 ID
	private int commuCommentRefId; //대댓글의 경우 참조하는 댓글의 ID
	
	private List<CommuComment> replies;// 대댓글 목록		
}

