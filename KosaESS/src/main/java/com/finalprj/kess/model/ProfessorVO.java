package com.finalprj.kess.model;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class ProfessorVO {
	private String profId;//강사id
	private String profNm;//강사이름
	private String profTel;//강사 연락처
	private String profEmail;//강사 이메일
	private Timestamp rgstDt;//등록일시
	private String rgsterId;//등록자id
	private Timestamp updtDt;//수정일시
	private String updterId;//수정자id
	private String deleteYn; //삭제여부
}
