package com.finalprj.kess.model;

import java.sql.Date;
import java.sql.Timestamp;

import lombok.Data;

@Data
public class ClassVO {
	private String clssId;//교육과정id
	private String mngrId;//업무담당자id
	private String cmpyId;//업체id
	private String clssNm;//교육과정명
	private String clssContent;//교육과정 개요
	private int limitCnt;//지원가능인원
	private Timestamp aplyStartDd;//지원시작일시
	private Timestamp aplyEndDd;//지원종료일시
	private Date clssStartDd;//교육시작일자
	private Date clssEndDd;//교육종료일자
	private Timestamp setInTm;//교육시작시간
	private Timestamp setOutTm;//교육종료시간
	private String clssCd;//교육과정 상태
	private String clssAdr;//교육장소
	private int totalTm;//교육과정 이수시간
	private String clssEtc;//기타사항
	private Timestamp rgstDt;//등록일시
	private String rgsterId;//등록자id
	private Timestamp updtDt;//수정일시
	private String updterId;//수정자id
}
