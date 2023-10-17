package com.finalprj.kess.repository;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

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
import com.finalprj.kess.model.RegistrationVO;
import com.finalprj.kess.model.StudentVO;
import com.finalprj.kess.model.SubjectVO;

@Repository
@Mapper
public interface IAdminRepository {
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
	List<ClassVO> getClassList(@Param("start")int start, @Param("end")int end);
	List<String> getClassCodeNameList();

	String getMaxClassId();
	List<CompanyVO> getCompanyList(@Param("start")int start, @Param("end")int end);
	List<ManagerVO> getManagerList(@Param("start")int start, @Param("end")int end);
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
	void deleteClass(List<String> clssIds);
	
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
	List<PostVO> getNoticeList(@Param("start")int start, @Param("end")int end);
	List<PostVO> getInquiryList(@Param("start")int start, @Param("end")int end);
	List<CommonCodeVO> getNoticeCommonCodeList(String string);
	List<CommonCodeVO> getInquriyCommonCodeList(String string);
	void deleteAllNotice(List<String> selectedNoticeIds);
	List<CommonCodeVO> getGroupCodeList();
	String getMaxNoticeId();
	void insertNoticeVO(PostVO postVO);
	PostVO getPostVO(String postId);
	void deleteAllInquiry(List<String> selectedInquiryIds);
	void updateNoticeVO(PostVO postVO);
	List<PostVO> getSearchPostList(String searchInputCategory, String searchInput, List<String> postStatusList);
	List<PostVO> getReplyList(String postId);
	String getMaxReplyId();
	void insertReplyVO(PostVO postVO);
	void updateInquiryStatus(String postId);
	String getMaxManagerId();
	void insertLgin(ManagerVO managerVO);
	void insertManager(ManagerVO managerVO);
	List<ManagerVO> getSearchManagerList(String searchInputCategory, String searchInput);
	void deleteManagerList(List<String> selectedManagerIds);
	void deleteLgin(List<String> selectedManagerIds);
	ManagerVO getManager(String mngrId);
	void updateLgin(ManagerVO managerVO);
	void updateManager(ManagerVO managerVO);
	SubjectVO getSubjectVO(String subjectId);
	ProfessorVO getProfessorVO(String professorId);
	String getMaxCompanyId();
	void insertCompanyVO(CompanyVO companyVO);
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
	void insertDetailcode(CommonCodeVO commonCodeVO);
	String getStudentEmailByAplyId(String aplyId);
	String getClssNmByAplyId(String aplyId);
	void deleteInquiryReply(String replyId);
	int getFileCnt(String fileId);
	List<StudentVO> getStudentList();
	int getManagerEmailCnt(String managerEmail);
	void updateCompany(CompanyVO companyVO);
	List<StudentVO> getSearchStudentList(String stdtNm, String clssId, String genderCd, String jobCd, String userCd);
	void deleteStudentList(List<String> selectedStudentIds);
	void deleteLginStudent(List<String> selectedStudentIds);
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
	void updateUseYnDetailCode(CommonCodeVO[] updateGroupList);

	List<CommonCodeVO> getSearchDetailCodeList(String tpcdId, String cmcdNm);
	List<CommonCodeVO> getSearchGroupCodeList(String tpcdId, String cmcdNm, String useYn);

}