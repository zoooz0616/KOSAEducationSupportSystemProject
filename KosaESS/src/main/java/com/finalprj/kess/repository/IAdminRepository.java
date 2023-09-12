package com.finalprj.kess.repository;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.finalprj.kess.dto.ApplyDetailDTO;
import com.finalprj.kess.dto.CurriculumDetailDTO;
import com.finalprj.kess.model.ClassVO;
import com.finalprj.kess.model.CommonCodeVO;
import com.finalprj.kess.model.CompanyVO;
import com.finalprj.kess.model.CurriculumVO;
import com.finalprj.kess.model.LectureVO;
import com.finalprj.kess.model.ManagerVO;
import com.finalprj.kess.model.PostVO;
import com.finalprj.kess.model.ProfessorVO;
import com.finalprj.kess.model.SubjectVO;

@Repository
@Mapper
public interface IAdminRepository {

	
	int getWaitClassCnt();
	List<PostVO> getPostVOList(String postValue);
	List<String> getClassSearch(String term);
	List<ClassVO> getSearchClassVOList(
			@Param("className") String className,
            @Param("status") List<String> status, //접수예정, 접수중, 접수마감, 교육중, 교육완료, 취소, 폐강
            @Param("aplyStartDt") Date aplyStartDt,
            @Param("aplyEndDt") Date aplyEndDt,
            @Param("classStartDd") Date classStartDd,
            @Param("classEndDd") Date classEndDd);



	
	
	
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
	
	List<CommonCodeVO> getCommonCodeList(String tpcdId);
	void insertClassVO(ClassVO classVO);
	void insertCurriculumVO(CurriculumVO curriculumVO);
	
	ClassVO getClass(String clssId);
	List<CurriculumVO> getCurriculumList(String clssId);
	CurriculumDetailDTO getCurriculumDetail(String lctrId);
	
	void deleteFile(String fileId, List<String> fileSubIds);
	int getMaxFileSubId(String fileId);
	void updateClassVO(ClassVO classVO);
	void deleteCurriculum(String clssId);
	List<ApplyDetailDTO> getApplyDetailDTOList(String clssId);
	void updateAplyPass(List<String> aplyIds);
	void updateAplyFail(List<String> aplyIds);
}