package com.finalprj.kess.model;

import java.sql.Blob;

import lombok.Data;

@Data
public class FileVO {
	private String fileId;//파일id
	private String postId;//게시글id
	private String clssId;//교육과정id
	private String cmptId;//업체id
	private String fileNm;//파일명
	private Blob fileContent;//파일내용
	private long fileSize;//파일크기
	private String fileType;//파일타입
}
