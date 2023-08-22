package com.finalprj.kess.model;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class ManagerVO {
	private String mngrId;//직원id
	private String mngrPwd;//직원 비밀번호
	private String mngrNm;//직원 이름
	private String mngrTel;//직원 연락처
	private String mngrEmail;//직원 이메일
	private String roleCd;//역할
	private String clss1Id;//담당교육과정1
	private String clss2Id;//담당교육과정2
	private String clss3Id;//담당교육과정3
	private Timestamp rgstDt;//등록일시
	private String rgsterId;//등록자id
	private Timestamp updtDt;//수정일시
	private String updterId;//수정자id
}