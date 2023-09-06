package com.finalprj.kess.model;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class ApplyVO {
	private String aplyId;//교유과정지원id
	private String stdtId;//교육생id
	private String clssId;//교육과정id
	private String aplyCd;//지원상태
	private String fileId; //파일id
	private Timestamp rgstDt;//등록일시
	private String rgsterId;//등록자id
	private Timestamp updtDt;//수정일시
	private String updterId;//수정자id
}
