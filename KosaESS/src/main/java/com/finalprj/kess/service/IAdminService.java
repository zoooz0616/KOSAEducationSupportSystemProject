package com.finalprj.kess.service;

import java.util.List;

import com.finalprj.kess.model.ClassVO;
import com.finalprj.kess.model.PostVO;
import com.finalprj.kess.model.ProfessorVO;

public interface IAdminService {
	int getWaitInquiryCnt();
	int getWaitClassCnt();
	int getCompleteClassCnt();
	List<PostVO> getPostVOList(String postValue);
	List<ProfessorVO> getProfessorVOList();
	List<ClassVO> getClassVOList();
}
