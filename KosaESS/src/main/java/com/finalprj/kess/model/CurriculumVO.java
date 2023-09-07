package com.finalprj.kess.model;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class CurriculumVO {
	private String clssId;
	private String lctrId;
	private Timestamp rgstDt;
	private String rgsterId;
	private Timestamp updtDt;
	private String updterId;
	private String deleteYn;
}
