package com.finalprj.kess.dto;

import java.sql.Date;
import java.sql.Timestamp;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class ClassInsertDTO {
	private String clssId;//교육과정id
	private String clssNm;//교육과정명
	private String clssContent;//교육과정 개요
	private int limitCnt;//지원가능인원

	private String aplyStartDt;//지원시작일시
	private String aplyEndDt;//지원종료일시

	private Date clssStartDd;//교육시작일자
	private Date clssEndDd;//교육종료일자

	private String setInTm;//교육시작시간
	private String setOutTm;//교육종료시간

	private String clssAdr;//교육장소
	private int clssTotalTm;//교육과정 이수시간
	private String clssEtc;//기타사항
}