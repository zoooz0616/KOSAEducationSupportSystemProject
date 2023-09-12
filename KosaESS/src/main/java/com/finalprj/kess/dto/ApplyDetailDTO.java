package com.finalprj.kess.dto;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class ApplyDetailDTO {
	private String aplyId;
	private String stdtId;
	private String stdtNm;
	private String aplyCd;
	private String aplyNm;
	private String fileId;
	private String fileNm;
	private byte[] fileContent;
	private Timestamp rgstD;
	
}
