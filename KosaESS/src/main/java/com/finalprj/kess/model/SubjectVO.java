package com.finalprj.kess.model;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class SubjectVO {
	private String sbjtId;//과목id
	private String sbjtNm;//과목명
	private Timestamp rgstDt;//등록일시
	private String rgsterId;//등록자id
	private Timestamp updtDt;//수정일시
	private String updterId;//수정자id
	private Character deleteYn;//삭제여부
}
