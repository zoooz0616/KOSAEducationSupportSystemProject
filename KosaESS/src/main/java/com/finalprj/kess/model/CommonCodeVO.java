package com.finalprj.kess.model;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class CommonCodeVO {
	private String cmcdId;//공통코드id
	private String tpcdId;//상위코드id
	private String cmcdNm;//공통코드명
	private Timestamp rgstDt;//등록일시
	private String rgsterId;//등록자id
	private Timestamp updtDt;//수정일시
	private String updterId;//수정자id
	private String useYn;//삭제여부
	private int cmcdOrder;//우선순위
}
