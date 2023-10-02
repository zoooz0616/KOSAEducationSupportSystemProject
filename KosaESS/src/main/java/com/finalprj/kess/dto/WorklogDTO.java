package com.finalprj.kess.dto;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import lombok.Data;

@Data
public class WorklogDTO {
	private String wlogId;//출퇴근 id
	private String stdtId;//교육생id
	private String stdtNm;//교육생 이름
	private String userEmail;//교육생 이메일
	private String clssId;//교육과정id
	
	private Timestamp inTm;//출근시간
	private Timestamp outTm;//퇴근시간
	private Date inTmDd;//출근시간
	private Date outTmDd;//퇴근시간
	private String strInTmDd;//출근시간
	private String strOutTmDd;//퇴근시간
	
	private String wlogCd;//출퇴근 상태
	private double wlogTotalTm;//총 근무시간
	private Timestamp rgstDt;//등록일시
	private String rgsterId;//등록자id
	private Timestamp updtDt;//수정일시
	private String updterId;//수정자id
	private String deleteYn; //삭제여부
	
	private String resnId;//사유서 id
	private String resnNm;//사유서 제목
	private String clssNm;//교육과정명
	private String wlogNm;//출퇴근 상태
	private String prcsCd;//처리 상태
		
	public String getInTmAsString() {
		if (inTm != null) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return dateFormat.format(inTm);
		} else {
			return null;
		}
	}

// 퇴근시간을 문자열로 반환
	public String getOutTmAsString() {
		if (outTm != null) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return dateFormat.format(outTm);
		} else {
			return null;
		}
	}
}
