package com.finalprj.kess.dto;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import lombok.Data;

@Data
public class ReasonDTO {
	private String resnId;//사유서 id
	private String resnContent;//사유서 내용
	private String fileId;//파일id
	private String resnCd;//처리 상태
	private String prcsNm;//처리 상태 이름
	
	private Timestamp rgstDt;//등록일시
	private Timestamp updtDt;//수정일시
	
	private String updterId;//수정자id
	private String deleteYn; //삭제여부
	
	private String rgsterId;//등록자id <<< 교육생 아이디임
	private String stdtNm;//교육생 이름
	private String userEmail;//교육생 이메일
	
	private String wlogId;//출퇴근 id
	private String clssId;//교육과정id
	private String clssNm;//교육과정명
	private Timestamp inTm;//출근시간
	private Timestamp outTm;//퇴근시간
	private String strInTmDd;//출근시간
	private String strOutTmDd;//퇴근시간
	private String wlogCd;//출퇴근 상태
	private String wlogNm;//출퇴근 이름
	
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