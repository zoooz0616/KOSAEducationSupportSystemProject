package com.finalprj.kess.dto;

import java.sql.Date;
import lombok.Getter;
import lombok.Setter;



@Setter @Getter
public class ClassDetailDTO {
	private String clssId;//교육과정id
	private String cmpyId;//업체id
	private String clssNm;//교육과정명
	private String clssContent;//교육과정 개요
	private int limitCnt;//지원가능인원
	private Date aplyStartDd;//지원시작일시 (날짜)
	private Date aplyEndDd;//지원종료일시 (날짜)
	private Date clssStartDd;//교육시작일자
	private Date clssEndDd;//교육종료일자
	private String setInTm;//교육시작시간
	private String setOutTm;//교육종료시간
	private String clssCd;//교육과정 상태
	private String clssAdr;//교육장소
	private int totalTm;//교육과정 이수시간
	private String clssEtc;//기타사항
	private Date rgstDd;//등록일시(날짜)
	private String cmpyNm; //회사명
	private String clssFile; //교육관련 파일
}
