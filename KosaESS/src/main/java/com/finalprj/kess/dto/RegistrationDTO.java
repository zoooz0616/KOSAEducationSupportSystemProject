package com.finalprj.kess.dto;

import java.sql.Date;
import java.sql.Timestamp;

import lombok.Data;

@Data
public class RegistrationDTO {
	private String rgstId;
	private String stdtId;
	private String stdtNm;
	private String rgstCd;
	private String rgstNm;
	private String cmptCd;
	private String cmptNm;
	private Timestamp printDd;
	private Timestamp rgstDt;
	private Date rgstDd;
	private Date updtDd;
	private String clssId;

	private String clssNm;
	private String clssCd;
	private String clssCdNm;
	private Date aplyEndDd;// 지원종료일시 (날짜)
	private Date clssStartDd;// 교육시작일자
	private Date clssEndDd;// 교육종료일자
	private int clssTotalTm;// 교육과정 이수시간
	private Double cmptRate;
	private Double stdtTmSum;
}
