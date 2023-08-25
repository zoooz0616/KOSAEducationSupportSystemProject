package com.finalprj.kess.model;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class RegistrationVO {
	private String rgstId;//수강id
	private String stdtId;//교육생id
	private String clssId;//교육과정id
	private String rgstCd;//수강상태
	private String cmptCd;//이수여부
	private Timestamp rgstDt;//등록일시
	private String rgsterId;//등록자id
	private Timestamp updtDt;//수정일시
	private String updterId;//수정자id
}