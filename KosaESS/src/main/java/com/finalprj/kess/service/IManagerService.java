package com.finalprj.kess.service;

import java.util.List;

import com.finalprj.kess.model.ClassVO;

public interface IManagerService {
	public List<ClassVO> getClassList(String mngrId);
	public int getApplyCount(String clssCd);
	public String getClassCodeName(String clssId);
}