package com.finalprj.kess.model;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import lombok.Data;

@Data
public class WorklogVO {
	private String wlogId;//출퇴근 id
	private String stdtId;//교육생id
	private String clssId;//교육과정id
	private Timestamp InTm;//출근시간
	private Timestamp OutTm;//퇴근시간
	private String wlogCd;//출퇴근 상태
	private int wlogTotalTm;//총 근무시간
	private Timestamp rgstDt;//등록일시
	private String rgsterId;//등록자id
	private Timestamp updtDt;//수정일시
	private String updterId;//수정자id
	private String deleteYn; //삭제여부
	
	public String getInTmAsString() {
        if (InTm != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return dateFormat.format(InTm);
        } else {
            return null;
        }
    }

    // 퇴근시간을 문자열로 반환
    public String getOutTmAsString() {
        if (OutTm != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return dateFormat.format(OutTm);
        } else {
            return null;
        }
    }
}
