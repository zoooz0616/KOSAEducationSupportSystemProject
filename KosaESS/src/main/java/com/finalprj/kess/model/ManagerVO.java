package com.finalprj.kess.model;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class ManagerVO {
	private String mngrId;//직원id
	private String userEmail;//이메일 -기존 mngrEmail
	private String mngrNm;//직원 이름
	private String mngrTel;//직원 연락처
	private Timestamp rgstDt;//등록일시
	private String rgsterId;//등록자id
	private Timestamp updtDt;//수정일시
	private String updterId;//수정자id
}