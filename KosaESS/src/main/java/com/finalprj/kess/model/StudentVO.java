package com.finalprj.kess.model;

import java.sql.Date;
import java.sql.Timestamp;

import lombok.Data;

@Data
public class StudentVO {
	private String stdtId;//교육생id
	private String stdtEmail;//교육생이메일
	private String stdtPwd;//교육생 비밀번호
	private String stdtNm;//교육생 이름
	private String genderCd;//교육생 성별
	private Date birthDd;//교육생 생년월일
	private String stdtTel;//교육생 연락처
	private String jobCd;//교육생 직업
	private String roleCd;//역할
	private String stdtCd;//교육생 회원 상태
	private Timestamp rgstDt;//등록일시
	private String rgsterId;//등록자id
	private Timestamp updtDt;//수정일시
	private String updterId;//수정자id
	private String clssNm;//교육과정 이름
}
