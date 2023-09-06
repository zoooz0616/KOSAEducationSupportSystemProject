package com.finalprj.kess.model;


import lombok.Data;

@Data
public class FileVO {
	private String fileId;//파일id
	private int fileSubId;//파일서브id
	private String fileNm;//파일명
	private byte[] fileContent;//파일내용
	private long fileSize;//파일크기
	private String fileType;//파일타입
	private Character deleteYn; //삭제여부
}
