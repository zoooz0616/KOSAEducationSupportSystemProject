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
		List<String> getClassSearch(String term);
	

	
	
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

	void deleteClass(List<String> clssIds);
	
	List<ClassVO> getSearchClassVOList(String className, List<String> status, Date aplyStartDt, Date aplyEndDt,
			Date classStartDd, Date classEndDd);

	List<ProfessorVO> getProfessorList();
	List<SubjectVO> getSubjectList();

	Integer getSubjectNmCnt(String sbjtNm);
	String getMaxSubjectId();
	void insertSubjectVO(SubjectVO subjectVO);
	
	Integer getProfTelCnt(String profTel);
	String getMaxProfId();
	void insertProfessorVO(ProfessorVO professorVO);

	Integer getLctrNmCnt(String lctrNm);
	String getMaxLectureId();
	void insertLectureVO(LectureVO lectureVO);

	void deleteLecture(List<String> lectureIds);
	void deleteSubject(List<String> selectedSubjectIds);
	void deleteProfessor(List<String> selectedProfessorIds);

	List<PostVO> getNoticeList();
	List<PostVO> getInquiryList();
	List<CommonCodeVO> getNoticeCommonCodeList(String string);
	List<CommonCodeVO> getInquriyCommonCodeList(String string);
	
	void deleteAllNotice(List<String> selectedNoticeIds);
	List<CommonCodeVO> getGroupCodeList();
	String getMaxNoticeId();
	void insertNoticeVO(List<FileVO> fileList, PostVO postVO);
	PostVO getPostVO(String postId);
	void deleteAllInquiry(List<String> selectedInquiryIds);
	void updateNoticeVO(List<FileVO> fileList, PostVO postVO);
	List<PostVO> getSearchPostList(String searchInputCategory, String searchInput, List<String> postStatusList);
	List<PostVO> getReplyList(String postId);
	
	String getMaxReplyId();
	void insertReplyVO(PostVO postVO);
	void updateInquiryStatus(String postId);
	String getMaxManagerId();
	void insertManagerVO(ManagerVO managerVO);
	List<ManagerVO> getSearchManagerList(String mngrNm, String mngrEmail);
	void deleteManagerList(List<String> selectedManagerIds);
	ManagerVO getManager(String mngrId);
	void updateManager(ManagerVO managerVO);
	List<SubjectVO> getYSubjectList();
	List<ProfessorVO> getYProfessorList();
	void updateLecture(LectureVO lectureVO);
	List<LectureVO> getYLectureList();
	SubjectVO getSubjectVO(String subjectId);
	void updateSubject(SubjectVO subjectVO);
	ProfessorVO getProfessorVO(String professorId);
	void updateProfessor(ProfessorVO professorVO);
	String getMaxCompanyId();
	void insertCompanyVO(FileVO fileVO, CompanyVO companyVO);
	CompanyVO getCompanyVO(String cmpyId);
	void deleteCompany(List<String> selectedCompanyIds);
	int getTpcdIdCnt(String tpcdId);
	String getMaxGroupCodeId();
	void insertGroupCode(CommonCodeVO commonCodeVO);
	void deleteGroupCode(List<String> selectedGroupCodeIds);
	void updateGroupCode(String cmcdId, String cmcdNm);
	List<CommonCodeVO> getDetailCodeList(String cmcdId);
	String getGroupCodeId(String cmcdId);
	int getDetailCodeNmCnt(String cmcdId, String cmcdNm);
	String getMaxDetailCodeId(String cmcdId);
	void insertDetailCode(CommonCodeVO commonCodeVO);
	void updateDetailCode(String cmcdId, String cmcdNm, String useYn);
}
