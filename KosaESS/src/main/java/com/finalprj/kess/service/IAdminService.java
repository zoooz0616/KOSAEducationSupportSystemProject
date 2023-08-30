package com.finalprj.kess.service;

import java.sql.Date;
import java.util.List;
import java.util.Map;

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
	List<String> getClassSearch(String term);
	List<ClassVO> getSearchClassVOList(String className, List<String> status, Date aplyStartDt, Date aplyEndDt,
			Date classStartDd, Date classEndDd);
}
