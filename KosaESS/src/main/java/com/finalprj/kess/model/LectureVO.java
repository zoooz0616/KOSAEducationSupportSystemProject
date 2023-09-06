package com.finalprj.kess.model;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class LectureVO {
	private String lctrId;//강의id
	private String sbjtId;//과목id
	private String profId;//강사id
	private String lctrNm;//강의명
	private int lctrTm;//강의 이수시간
	private String lctrEtc;//강의 부가정보
	private Timestamp rgstDt;//등록일시
	private String rgsterId;//등록자id
	private Timestamp updtDt;//수정일시
	private String updterId;//수정자id
	private String deleteYn; //삭제여부
}
