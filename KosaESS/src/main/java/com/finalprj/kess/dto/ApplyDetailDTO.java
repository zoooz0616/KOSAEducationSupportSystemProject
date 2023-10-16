package com.finalprj.kess.dto;

import java.sql.Date;
import java.sql.Timestamp;

import lombok.Data;

@Data
public class ApplyDetailDTO {
	private String aplyId;
	private String stdtId;
	private String userEmail;
	private String stdtNm;
	private String aplyCd;
	private String cmcdNm;
	private String fileId;// 파일id
	private String fileNm;
	private int fileSubId;// 파일서브id
	private Timestamp rgstDt;
	private Date rgstDd;
	private Date updtDd;
	private String clssId;

	private String clssNm;
	private String clssCd;
	private String clssCdNm;
	private Date aplyStartDd;// 지원시작일시 (날짜)
	private Timestamp aplyEndDt;// 지원종료일시
	private Date aplyEndDd;// 지원종료일시 (날짜)
	private Date clssStartDd;// 교육시작일자
	private Date clssEndDd;// 교육종료일자
	private int limitCnt;// 지원가능인원
}
