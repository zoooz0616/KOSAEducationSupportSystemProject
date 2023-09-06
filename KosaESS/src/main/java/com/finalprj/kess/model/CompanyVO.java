package com.finalprj.kess.model;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class CompanyVO {
	private String cmpyId;//업체id
	private String cmpyNm;//업체이름
	private String cmpyTel;//업체연락처
	private String cmpyAdr;//업체주소
	private Timestamp rgstDt;//등록일시
	private String rgsterId;//등록자id
	private Timestamp updtDt;//수정일시
	private String updterId;//수정자id
	private Character deleteYn;//삭제여부
}
