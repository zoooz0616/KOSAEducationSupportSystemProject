package com.finalprj.kess.service;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.finalprj.kess.dto.ApplyDetailDTO;
import com.finalprj.kess.dto.CurriculumDetailDTO;
import com.finalprj.kess.model.ApplyVO;
import com.finalprj.kess.model.ClassVO;
import com.finalprj.kess.model.CommonCodeVO;
import com.finalprj.kess.model.CompanyVO;
import com.finalprj.kess.model.CurriculumVO;
import com.finalprj.kess.model.FileVO;
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
	void createClass(List<FileVO> fileList, ClassVO classVO, List<CurriculumVO> curriculumList);

	ClassVO getClass(String clssId);
	List<CurriculumVO> getCurriculumList(String clssId);
	CurriculumDetailDTO getCurriculumDetail(String lctrId);

	void deleteFile(String fileId, List<String> fileSubIds);
	Integer getMaxFileSubId(String fileId);

	void updateClass(List<FileVO> fileList, ClassVO classVO, List<CurriculumVO> curriculumList);

	List<ApplyDetailDTO> getApplyDetailDTOList(String clssId);
	void updateAplyPass(List<String> aplyIds);
	void updateAplyFail(List<String> aplyIds);
}
