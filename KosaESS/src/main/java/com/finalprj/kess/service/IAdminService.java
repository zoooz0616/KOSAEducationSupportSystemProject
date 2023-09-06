package com.finalprj.kess.service;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import com.finalprj.kess.model.ClassVO;
import com.finalprj.kess.model.CompanyVO;
import com.finalprj.kess.model.LectureVO;
import com.finalprj.kess.model.ManagerVO;
import com.finalprj.kess.model.PostVO;
import com.finalprj.kess.model.ProfessorVO;
import com.finalprj.kess.model.SubjectVO;

public interface IAdminService {
	
	int getWaitClassCnt();
	
	List<PostVO> getPostVOList(String postValue);
	List<String> getClassSearch(String term);
	List<ClassVO> getSearchClassVOList(String className, List<String> status, Date aplyStartDt, Date aplyEndDt,
			Date classStartDd, Date classEndDd);

	void insertClassVO(ClassVO classVO);
	
	int getNoticeCnt();
	int getInquiryCnt();
	int getCompanyCnt();
	int getSubjectCnt();
	int getStudentCnt();
	int getClassCnt();
	int getProfesserCnt();
	int getManagerCnt();
	int getWaitInquiryCnt();
	List<PostVO> getWaitInquiryList();
	int getCompleteClassCnt();
	List<ClassVO> getCompleteClassList();
	List<ClassVO> getClassList();
	List<String> getClassCodeNameList();
	
	String getMaxClassId();
	List<CompanyVO> getCompanyList();
	List<ManagerVO> getManagerList();
	List<LectureVO> getLectureList();

	SubjectVO getSubject(String lectureId);
	ProfessorVO getProfessor(String lectureId);
	LectureVO getLecture(String lectureId);
}
