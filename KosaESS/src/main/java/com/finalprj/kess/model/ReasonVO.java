package com.finalprj.kess.model;

import java.sql.Clob;
import java.sql.Timestamp;

import lombok.Data;

@Data
public class ReasonVO {
	private String resnId;//사유서 id
	private String wlogId;//출퇴근 id
	private String resnContent;//사유서 내용
	private String resnCd;//사유서 상태
	private String fileId;//파일id
	private Timestamp rgstDt;//등록일시
	private String rgsterId;//등록자id
	private Timestamp updtDt;//수정일시
	private String updterId;//수정자id
	private String deleteYn; //삭제여부
}
