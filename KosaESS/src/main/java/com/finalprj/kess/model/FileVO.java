package com.finalprj.kess.model;


import java.sql.Timestamp;

import lombok.Data;

@Data
public class FileVO {
	private String fileId;//파일id
	private int fileSubId;//파일서브id
	private String fileNm;//파일명
	private byte[] fileContent;//파일내용
	private long fileSize;//파일크기
	private String fileType;//파일타입
	private String deleteYn; //삭제여부
	private Timestamp rgstDt;//등록일시
	private String rgsterId;//등록자id

	
	private String fType;//파일id
	
	@Override
	public String toString() {
		return "FileVO [fileId=" + fileId + ", fileSubId=" + fileSubId + ", fileNm=" + fileNm + ", fileSize=" + fileSize + ", fileType=" + fileType + ", deleteYn=" + deleteYn + "]";
	}
}
