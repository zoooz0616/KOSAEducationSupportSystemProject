package com.finalprj.kess.dto;

import java.sql.Date;
import java.sql.Timestamp;

import lombok.Data;

@Data
public class SubsidyDTO {
	private int rowNum;// 순번
	private String monyId;// 지원금id
	private String clssId;// 교육과정id
	private String clssNm;// 교육과정 이름
	private String clssSubsidy;// 교육과정 지원금
	private String stdtId;// 교육생id
	private String stdtNm;// 교육생 이름
	private String userEmail;// 교육생 이메일
	private String wlog;// 교육생 이메일
	
	private String monyCd;// 지급 상태
	private String monyNm;// 지급 상태 이름
	private Date paidDd;//지급 확정 일자
	private int payment;//지급액
	private Date subsidyDd;//지원금 해당 연월
	private int maxWlogCnt;//교육일수(최대값)
	private int wlogCnt;//출석일수
	
	private Timestamp rgstDt;// 등록일시
	private String rgsterId;// 등록자id
	private Timestamp updtDt;// 수정일시
	private String updterId;// 수정자id
	
	public void appendWlog(String wlog) {
		this.wlog = this.wlog.concat(wlog);
	}
}