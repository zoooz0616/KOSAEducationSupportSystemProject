package com.finalprj.kess.model;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class ManagerVO {
	private String mngrId;//직원id
	private String userEmail;//직원email
	private String userPwd;//직원 비밀번호
	private String mngrNm;//직원 이름
	private String mngrTel;//직원 연락처
	private String roleCd;//역할
	private String userCd;//계정상태
	private Timestamp rgstDt;//등록일시
	private String rgsterId;//등록자id
	private Timestamp updtDt;//수정일시
	private String updterId;//수정자id
	private Timestamp lastLoginDt;//마지막 로그인 일시
}