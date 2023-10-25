package com.finalprj.kess.service;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.finalprj.kess.dto.ApplyDetailDTO;
import com.finalprj.kess.dto.CurriculumDetailDTO;
import com.finalprj.kess.dto.SubsidyDTO;
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
import com.finalprj.kess.model.RegistrationVO;
import com.finalprj.kess.model.StudentVO;
import com.finalprj.kess.model.SubjectVO;

public interface IAdminService {
	
	int getWaitClassCnt();
		
	

	
	
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
	List<ClassVO> getClassList(int page);
	List<String> getClassCodeNameList();
	
	String getMaxClassId();
	List<CompanyVO> getCompanyList(int page);
	List<ManagerVO> getManagerList(int page);
	List<LectureVO> getLectureList(int page);

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

	List<ProfessorVO> getProfessorList();
	List<SubjectVO> getSubjectList();

	Integer getSubjectNmCnt(String sbjtNm);
	String getMaxSubjectId();
	void insertSubjectVO(SubjectVO subjectVO);
	
	Integer getProfTelCnt(String profTel, String profEmail);
	String getMaxProfId();
	void insertProfessorVO(ProfessorVO professorVO);

	Integer getLctrNmCnt(String lctrNm);
	String getMaxLectureId();
	void insertLectureVO(LectureVO lectureVO);

	void deleteLecture(List<String> lectureIds);
	void deleteSubject(List<String> selectedSubjectIds);
	void deleteProfessor(List<String> selectedProfessorIds);

	List<PostVO> getNoticeList(int page);
	List<PostVO> getInquiryList(int page);
	List<CommonCodeVO> getNoticeCommonCodeList(String string);
	List<CommonCodeVO> getInquriyCommonCodeList(String string);
	
	void deleteAllNotice(List<String> selectedNoticeIds);
	List<CommonCodeVO> getGroupCodeList(int page);
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
	List<ManagerVO> getSearchManagerList(String searchInputCategory, String searchInput, String searchMngrStatus, String searchClassId);
	void deleteManagerList(List<String> selectedManagerIds);
	ManagerVO getManager(String mngrId);
	void updateManager(ManagerVO managerVO);
	SubjectVO getSubjectVO(String subjectId);
	ProfessorVO getProfessorVO(String professorId);
	String getMaxCompanyId();
	void insertCompanyVO(FileVO fileVO, CompanyVO companyVO);
	CompanyVO getCompanyVO(String cmpyId);
	void deleteCompany(List<String> selectedCompanyIds);
	int getTpcdIdCnt(String tpcdId);
	String getMaxGroupCodeId();
	void insertGroupCode(CommonCodeVO commonCodeVO);
	void deleteGroupCode(List<String> selectedGroupCodeIds);
	List<CommonCodeVO> getDetailCodeList(String cmcdId);
	String getGroupCodeId(String cmcdId);
	int getDetailCodeNmCnt(String cmcdId, String cmcdNm);
	String getMaxDetailCodeId(String cmcdId);
	void insertDetailCode(CommonCodeVO commonCodeVO);
	String getStudentEmailByAplyId(String aplyId);
	String getClssNmByAplyId(String aplyId);
	void deleteInquiryReply(String replyId);
	int getFileCnt(String fileId);
	List<StudentVO> getStudentList(int page);
	int getManagerEmailCnt(String managerEmail);
	void updateCompany(FileVO fileVO, CompanyVO companyVO);
	List<StudentVO> getSearchStudentList(String stdtNm, String clssId, String genderCd, String jobCd, String userCd);
	void deleteStudentList(List<String> selectedStudentIds);
	StudentVO getStudent(String stdtId);
	
	List<ApplyVO> getApplyListByStudent(String stdtId);
	List<RegistrationVO> getRegistListByStudent(String stdtId);
	
	List<ClassVO> getSearchClassList(String clssNm, String clssCd, String aplyStartDt, String aplyEndDt,
			String clssStartDd, String clssEndDd, String cmpyId);
	
	List<String> getClassSearch(String term);

	void updateLecture(LectureVO[] lectureList);
	void updateSubject(SubjectVO[] updateSubjectList);
	void updateProfessor(ProfessorVO[] updateProfessorList);

	void updateDetailCode(CommonCodeVO[] updateDetailList);
	void updateGroupCode(CommonCodeVO[] updateGroupList);

	List<CommonCodeVO> getSearchDetailCodeList(String tpcdId, String cmcdNm);
	List<CommonCodeVO> getSearchGroupCodeList(String tpcdId, String cmcdNm, String useYn);

	int getGroupCodeCnt();
	
	List<PostVO> getNoticeListAll();
	List<PostVO> getInquiryListAll();
	List<ManagerVO> getManagerListAll();
	List<CompanyVO> getCompanyListAll();
	List<ClassVO> getClassListAll();
	List<StudentVO> getStudentListAll();
	List<CommonCodeVO> getGroupCodeListAll();

	List<CommonCodeVO> getNoticeCommonCodeListByInsert();

	List<CompanyVO> getSearchCompanyList(String cmpyNm);

	List<SubsidyDTO> getSubsidyList(int page);
	List<SubsidyDTO> getSubsidyListAll();

	void updateSubsidyStatus(List<String> selectedSubsidyIds, String cmcdId);

	List<SubsidyDTO> getSearchSubsidyList(String clssId, String startDate, String endDate, String keyword,
			String subsidyStatus);

	List<ManagerVO> getManagerListAllByInsert();
	List<SubjectVO> getSearchSubjectList(String sbjtNm);
	List<ProfessorVO> getSearchProfessorList(String keyword);
	List<LectureVO> getSearchLectureList(String lctrNm, String sbjtId, String profId);

	List<LectureVO> getLectureListAll();
}
