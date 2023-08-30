package com.finalprj.kess.model;


import lombok.Data;

@Data
public class FileVO {
	private String fileId;//파일id
	private String postId;//게시글id
	private String fileNm;//파일명
	private byte[] fileContent;//파일내용
	private long fileSize;//파일크기
	private String fileType;//파일타입
	
	@Override
	public String toString() {
		return "FileVO [fileId=" + fileId + ", postId=" + postId + ", fileNm=" + fileNm + ", fileSize=" + fileSize + ", fileType=" + fileType + "]";
	}
}
