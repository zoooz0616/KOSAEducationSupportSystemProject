package com.finalprj.kess.dto;

import java.sql.Date;
import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentInfoDTO {
	private String stdtId;//교육생id
	private String userEmail;//이메일
	private String userPwd;//직원 비밀번호
	private String stdtNm;//교육생 이름
	private String genderCd;//교육생 성별
	private Date birthDd;//교육생 생년월일
	private String stdtTel;//교육생 연락처
	private String jobCd;//교육생 직업
	private String roleCd;//역할
	private String userCd;//계정상태

	private String wlogCnt;//출석 갯수의 나열

	private Timestamp rgstDt;//등록일시
	private String rgsterId;//등록자id
	private Timestamp updtDt;//수정일시
	private String updterId;//수정자id

	private String clssNm;//교육과정 이름
	private Timestamp lastLoginDt;//마지막 로그인 일시
	private int countLateArrive;//지각 수
	private int countEalryLeave;//조퇴 수
	private int countAbsent;//결석 수

	public void appendWlogCnt(String wlogCnt) {
		this.wlogCnt = this.wlogCnt.concat(wlogCnt);
	}
}