package com.finalprj.kess.dto;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class ApplyDetailDTO {
	private String aplyId;
	private String stdtId;
	private String stdtNm;
	private String aplyCd;
	private String cmcdNm;
	private String fileId;//파일id
	private int fileSubId;//파일서브id
	private Timestamp rgstDt;
	
}
