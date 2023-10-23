package com.finalprj.kess.model;

import java.sql.Date;
import java.sql.Timestamp;

import lombok.Data;

@Data
public class SubsidyVO {
	private String sbsdId;// 지원금id
	private String clssId;// 교육과정id
	private String stdtId;// 교육생id
	
	private String sbsdCd;// 지급 상태
	private Date paidDd;//지급 확정 일자
	private int payment;//지급액
	private Date subsidyDd;//지원금 해당 연월
	private int maxWlogCnt;//교육일수(최대값)
	private int wlogCnt;//출석일수
	
	private Timestamp rgstDt;// 등록일시
	private String rgsterId;// 등록자id
	private Timestamp updtDt;// 수정일시
	private String updterId;// 수정자id
	
	private String sbsdEtc;//기타사항
}