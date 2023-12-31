package com.finalprj.kess.model;

import java.sql.Date;
import java.sql.Timestamp;

import lombok.Data;

@Data
public class PostVO {
	private String postId; //게시글id
	private String masterId;//메인게시글id
	private String postTitle;//게시글제목
	private String postContent;//게시글내용
	private int postHit;//조회수
	private String postCd;//게시상태
	private String fileId;//파일id
	private Date rgstDd;//등록일시(날짜)
	private Timestamp rgstDt;//등록일시
	private String rgsterId;//등록자id
	private Timestamp updtDt;//수정일시
	private String updterId;//수정자id
	private String deleteYn;//삭제여부
	
	private String cmcdNm;//상태이름
	private String stdtNm;//등록자이름
	private String mngrNm;//등록자이름
	
	private int rowNum;//번호
}
