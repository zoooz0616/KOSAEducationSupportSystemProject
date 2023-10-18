package com.finalprj.kess.service;

import java.sql.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
import com.finalprj.kess.model.RegistrationVO;
import com.finalprj.kess.model.StudentVO;
import com.finalprj.kess.model.SubjectVO;
import com.finalprj.kess.repository.IAdminRepository;
import com.finalprj.kess.repository.IUploadFileRepository;
import com.itextpdf.text.pdf.PdfStructTreeController.returnType;

@Service
public class AdminService implements IAdminService {

	@Autowired
	IAdminRepository adminRepository;

	@Autowired
	IUploadFileRepository uploadFileRepository;

	@Override
	public int getNoticeCnt() {
		return adminRepository.getNoticeCnt();
	}

	@Override
	public int getInquiryCnt() {
		return adminRepository.getInquiryCnt();
	}

	@Override
	public int getCompanyCnt() {
		return adminRepository.getCompanyCnt();
	}

	@Override
	public int getSubjectCnt() {
		return adminRepository.getSubjectCnt();
	}

	@Override
	public int getStudentCnt() {
		return adminRepository.getStudentCnt();
	}

	@Override
	public int getClassCnt() {
		return adminRepository.getClassCnt();
	}

	@Override
	public int getProfesserCnt() {
		return adminRepository.getProfesserCnt();
	}

	@Override
	public int getManagerCnt() {
		return adminRepository.getManagerCnt();
	}

	@Override
	public int getWaitInquiryCnt() {
		return adminRepository.getWaitInquiryCnt();
	}

	@Override
	public List<PostVO> getWaitInquiryList() {
		return adminRepository.getWaitInquiryList();
	}

	@Override
	public int getCompleteClassCnt() {
		return adminRepository.getCompleteClassCnt();
	}

	@Override
	public List<ClassVO> getCompleteClassList() {
		return adminRepository.getCompleteClassList();
	}

	@Override
	public List<ClassVO> getClassList(int page) {
		int start = (page - 1) * 20 + 1;
		
		return adminRepository.getClassList(start,start+19);
	}

	@Override
	public List<String> getClassCodeNameList() {
		return adminRepository.getClassCodeNameList();
	}

	@Override
	public String getMaxClassId() {
		return adminRepository.getMaxClassId();
	}

	@Override
	public List<CompanyVO> getCompanyList(int page) {
		int start = (page - 1) * 20 + 1;
		
		return adminRepository.getCompanyList(start,start+19);
	}

	@Override
	public List<ManagerVO> getManagerList(int page) {
		int start = (page - 1) * 20 + 1;
		
		return adminRepository.getManagerList(start,start+19);
	}

	@Override
	public List<LectureVO> getLectureList() {
		return adminRepository.getLectureList();
	}

	@Override
	public SubjectVO getSubject(String lectureId) {
		return adminRepository.getSubject(lectureId);
	}

	@Override
	public ProfessorVO getProfessor(String lectureId) {
		return adminRepository.getProfessor(lectureId);
	}

	@Override
	public LectureVO getLecture(String lectureId) {
		return adminRepository.getLecture(lectureId);
	}

	@Override
	public List<CommonCodeVO> getCommonCodeList(String tpcdId) {
		return adminRepository.getCommonCodeList(tpcdId);
	}

	// 삭제할거임
	@Override
	public void insertClassVO(ClassVO classVO) {
		adminRepository.insertClassVO(classVO);
	}

	// 삭제할거임
	@Override
	public void insertCurriculumVO(CurriculumVO curriculumVO) {
		adminRepository.insertCurriculumVO(curriculumVO);
	}

	@Override
	@Transactional
	public void createClass(List<FileVO> fileList, ClassVO classVO, List<CurriculumVO> curriculumList) {
		if (fileList != null) {
			for (FileVO fileVO : fileList) {
				uploadFileRepository.uploadFile(fileVO);
			}
		}

		adminRepository.insertClassVO(classVO);

		if (curriculumList != null) {
			for (CurriculumVO curriculumVO : curriculumList) {
				adminRepository.insertCurriculumVO(curriculumVO);
			}
		}

	}

	@Override
	public ClassVO getClass(String clssId) {
		return adminRepository.getClass(clssId);
	}

	@Override
	public List<CurriculumVO> getCurriculumList(String clssId) {
		return adminRepository.getCurriculumList(clssId);
	}

	@Override
	public CurriculumDetailDTO getCurriculumDetail(String lctrId) {
		return adminRepository.getCurriculumDetail(lctrId);
	}

	@Override
	public void deleteFile(String fileId, List<String> fileSubIds) {
		adminRepository.deleteFile(fileId, fileSubIds);
	}

	@Override
	public Integer getMaxFileSubId(String fileId) {
		return adminRepository.getMaxFileSubId(fileId);
	}

	@Override
	@Transactional
	public void updateClass(List<FileVO> fileList, ClassVO classVO, List<CurriculumVO> curriculumList) {
		if (fileList != null) {
			for (FileVO fileVO : fileList) {
				uploadFileRepository.uploadFile(fileVO);
			}
		}
		adminRepository.updateClassVO(classVO);

		if (curriculumList != null) {
			adminRepository.deleteCurriculum(classVO.getClssId());
			for (CurriculumVO curriculumVO : curriculumList) {
				adminRepository.insertCurriculumVO(curriculumVO);
			}
		}
	}

	@Override
	public List<ApplyDetailDTO> getApplyDetailDTOList(String clssId) {
		return adminRepository.getApplyDetailDTOList(clssId);
	}

	@Override
	public void updateAplyPass(List<String> aplyIds) {
		adminRepository.updateAplyPass(aplyIds);
	}

	@Override
	public void updateAplyFail(List<String> aplyIds) {
		adminRepository.updateAplyFail(aplyIds);
	}

	@Override
	public void deleteClass(List<String> clssIds) {
		adminRepository.deleteClass(clssIds);
	}

	@Override
	public List<ProfessorVO> getProfessorList() {
		return adminRepository.getProfessorList();
	}

	@Override
	public List<SubjectVO> getSubjectList() {
		return adminRepository.getSubjectList();
	}

	@Override
	public Integer getSubjectNmCnt(String sbjtNm) {
		return adminRepository.getSubjectNmCnt(sbjtNm);
	}

	@Override
	public String getMaxSubjectId() {
		return adminRepository.getMaxSubjectId();
	}

	@Override
	public void insertSubjectVO(SubjectVO subjectVO) {
		adminRepository.insertSubjectVO(subjectVO);
	}

	@Override
	public Integer getProfTelCnt(String profTel) {
		return adminRepository.getProfTelCnt(profTel);
	}

	@Override
	public String getMaxProfId() {
		return adminRepository.getMaxProfId();
	}

	@Override
	public void insertProfessorVO(ProfessorVO professorVO) {
		adminRepository.insertProfessorVO(professorVO);
	}

	@Override
	public Integer getLctrNmCnt(String lctrNm) {
		return adminRepository.getLctrNmCnt(lctrNm);
	}

	@Override
	public String getMaxLectureId() {
		return adminRepository.getMaxLectureId();
	}

	@Override
	public void insertLectureVO(LectureVO lectureVO) {
		adminRepository.insertLectureVO(lectureVO);
	}

	@Override
	public void deleteLecture(List<String> lectureIds) {
		adminRepository.deleteLecture(lectureIds);
	}

	@Override
	public void deleteSubject(List<String> selectedSubjectIds) {
		adminRepository.deleteSubject(selectedSubjectIds);
	}

	@Override
	public void deleteProfessor(List<String> selectedProfessorIds) {
		adminRepository.deleteProfessor(selectedProfessorIds);
	}

	@Override
	public List<PostVO> getNoticeList(int page) {
		int start = (page - 1) * 20 + 1;
		
		return adminRepository.getNoticeList(start,start+19);
	}

	@Override
	public List<PostVO> getInquiryList(int page) {
		int start = (page - 1) * 20 + 1;
		
		return adminRepository.getInquiryList(start,start+19);
	}

	@Override
	public List<CommonCodeVO> getNoticeCommonCodeList(String string) {
		return adminRepository.getNoticeCommonCodeList(string);
	}

	@Override
	public List<CommonCodeVO> getInquriyCommonCodeList(String string) {
		return adminRepository.getInquriyCommonCodeList(string);
	}

	@Override
	public void deleteAllNotice(List<String> selectedNoticeIds) {
		adminRepository.deleteAllNotice(selectedNoticeIds);
	}

	@Override
	public List<CommonCodeVO> getGroupCodeList(int page) {
		int start = (page - 1) * 20 + 1;
		
		return adminRepository.getGroupCodeList(start, start+19);
	}

	@Override
	public String getMaxNoticeId() {
		return adminRepository.getMaxNoticeId();
	}

	@Override
	@Transactional
	public void insertNoticeVO(List<FileVO> fileList, PostVO postVO) {
		if (fileList != null) {
			for (FileVO fileVO : fileList) {
				uploadFileRepository.uploadFile(fileVO);
			}
		}

		adminRepository.insertNoticeVO(postVO);
	}

	@Override
	public PostVO getPostVO(String postId) {
		return adminRepository.getPostVO(postId);
	}

	@Override
	public void deleteAllInquiry(List<String> selectedInquiryIds) {
		adminRepository.deleteAllInquiry(selectedInquiryIds);
	}

	@Override
	public void updateNoticeVO(List<FileVO> fileList, PostVO postVO) {
		if (fileList != null) {
			for (FileVO fileVO : fileList) {
				uploadFileRepository.uploadFile(fileVO);
			}
		}

		adminRepository.updateNoticeVO(postVO);
	}

	@Override
	public List<PostVO> getSearchPostList(String searchInputCategory, String searchInput, List<String> postStatusList) {
		return adminRepository.getSearchPostList(searchInputCategory, searchInput, postStatusList);
	}

	@Override
	public List<PostVO> getReplyList(String postId) {
		return adminRepository.getReplyList(postId);
	}

	@Override
	public String getMaxReplyId() {
		return adminRepository.getMaxReplyId();
	}

	@Override
	public void insertReplyVO(PostVO postVO) {
		adminRepository.insertReplyVO(postVO);
	}

	@Override
	public void updateInquiryStatus(String postId) {
		adminRepository.updateInquiryStatus(postId);
	}

	@Override
	public String getMaxManagerId() {
		return adminRepository.getMaxManagerId();
	}

	@Override
	@Transactional
	public void insertManagerVO(ManagerVO managerVO) {
		// lgin에 먼저 insert
		adminRepository.insertLgin(managerVO);
		// mngr에 insert
		adminRepository.insertManager(managerVO);
	}

	@Override
	public List<ManagerVO> getSearchManagerList(String searchInputCategory, String searchInput) {
		return adminRepository.getSearchManagerList(searchInputCategory, searchInput);
	}

	@Override
	@Transactional
	public void deleteManagerList(List<String> selectedManagerIds) {
		adminRepository.deleteLgin(selectedManagerIds);
		adminRepository.deleteManagerList(selectedManagerIds);
	}

	@Override
	public ManagerVO getManager(String mngrId) {
		return adminRepository.getManager(mngrId);
	}

	@Override
	@Transactional
	public void updateManager(ManagerVO managerVO) {
		adminRepository.updateLgin(managerVO);
		adminRepository.updateManager(managerVO);
	}

	@Override
	public SubjectVO getSubjectVO(String subjectId) {
		return adminRepository.getSubjectVO(subjectId);
	}

	@Override
	public ProfessorVO getProfessorVO(String professorId) {
		return adminRepository.getProfessorVO(professorId);
	}

	@Override
	public String getMaxCompanyId() {
		return adminRepository.getMaxCompanyId();
	}

	@Override
	@Transactional
	public void insertCompanyVO(FileVO fileVO, CompanyVO companyVO) {
		if (fileVO != null) {
			uploadFileRepository.uploadFile(fileVO);
		}

		adminRepository.insertCompanyVO(companyVO);
	}

	@Override
	public CompanyVO getCompanyVO(String cmpyId) {
		return adminRepository.getCompanyVO(cmpyId);
	}

	@Override
	public void deleteCompany(List<String> selectedCompanyIds) {
		adminRepository.deleteCompany(selectedCompanyIds);
	}

	@Override
	public int getTpcdIdCnt(String tpcdId) {
		return adminRepository.getTpcdIdCnt(tpcdId);
	}

	@Override
	public String getMaxGroupCodeId() {
		return adminRepository.getMaxGroupCodeId();
	}

	@Override
	public void insertGroupCode(CommonCodeVO commonCodeVO) {
		adminRepository.insertGroupCode(commonCodeVO);
	}

	@Override
	public void deleteGroupCode(List<String> selectedGroupCodeIds) {
		adminRepository.deleteGroupCode(selectedGroupCodeIds);
	}

	@Override
	public List<CommonCodeVO> getDetailCodeList(String cmcdId) {
		return adminRepository.getDetailCodeList(cmcdId);
	}

	@Override
	public String getGroupCodeId(String cmcdId) {
		return adminRepository.getGroupCodeId(cmcdId);
	}

	@Override
	public int getDetailCodeNmCnt(String cmcdId, String cmcdNm) {
		return adminRepository.getDetailCodeNmCnt(cmcdId, cmcdNm);
	}

	@Override
	public String getMaxDetailCodeId(String cmcdId) {
		return adminRepository.getMaxDetailCodeId(cmcdId);
	}

	@Override
	public void insertDetailCode(CommonCodeVO commonCodeVO) {
		adminRepository.insertDetailcode(commonCodeVO);
	}

	@Override
	public String getStudentEmailByAplyId(String aplyId) {
		return adminRepository.getStudentEmailByAplyId(aplyId);
	}

	@Override
	public String getClssNmByAplyId(String aplyId) {
		return adminRepository.getClssNmByAplyId(aplyId);
	}

	@Override
	public void deleteInquiryReply(String replyId) {
		adminRepository.deleteInquiryReply(replyId);
	}

	@Override
	public int getFileCnt(String fileId) {
		return adminRepository.getFileCnt(fileId);
	}

	@Override
	public List<StudentVO> getStudentList(int page) {
		int start = (page - 1) * 20 + 1;
		
		return adminRepository.getStudentList(start,start+19);
	}

	@Override
	public int getManagerEmailCnt(String managerEmail) {
		return adminRepository.getManagerEmailCnt(managerEmail);
	}

	@Override
	@Transactional
	public void updateCompany(FileVO fileVO, CompanyVO companyVO) {
		if (fileVO != null) {
			uploadFileRepository.uploadFile(fileVO);
		}

		adminRepository.updateCompany(companyVO);
	}

	@Override
	public List<StudentVO> getSearchStudentList(String stdtNm, String clssId, String genderCd, String jobCd,
			String userCd) {
		return adminRepository.getSearchStudentList(stdtNm, clssId, genderCd, jobCd, userCd);
	}

	@Override
	@Transactional
	public void deleteStudentList(List<String> selectedStudentIds) {
		adminRepository.deleteLginStudent(selectedStudentIds);
		adminRepository.deleteStudentList(selectedStudentIds);
	}

	@Override
	public StudentVO getStudent(String stdtId) {
		return adminRepository.getStudent(stdtId);
	}

	@Override
	public List<ApplyVO> getApplyListByStudent(String stdtId) {
		return adminRepository.getApplyListByStudent(stdtId);
	}

	@Override
	public List<RegistrationVO> getRegistListByStudent(String stdtId) {
		return adminRepository.getRegistListByStudent(stdtId);
	}

	@Override
	public List<ClassVO> getSearchClassList(String clssNm, String clssCd, String aplyStartDt, String aplyEndDt,
			String clssStartDd, String clssEndDd, String cmpyId) {
		return adminRepository.getSearchClassList(clssNm, clssCd, aplyStartDt, aplyEndDt, clssStartDd, clssEndDd,
				cmpyId);
	}

	@Override
	public List<String> getClassSearch(String term) {
		return adminRepository.getClassSearch(term);
	}

	@Override
	public void updateLecture(LectureVO[] lectureList) {
		adminRepository.updateLecture(lectureList);
	}

	@Override
	public void updateSubject(SubjectVO[] updateSubjectList) {
		adminRepository.updateSubject(updateSubjectList);
	}

	@Override
	public void updateProfessor(ProfessorVO[] updateProfessorList) {
		adminRepository.updateProfessor(updateProfessorList);
	}

	@Override
	public void updateDetailCode(CommonCodeVO[] updateDetailList) {
		adminRepository.updateDetailCode(updateDetailList);
	}

	@Override
	public void updateGroupCode(CommonCodeVO[] updateGroupList) {
		adminRepository.updateGroupCode(updateGroupList);
		adminRepository.updateUseYnDetailCode(updateGroupList);
	}

	@Override
	public List<CommonCodeVO> getSearchDetailCodeList(String tpcdId, String cmcdNm) {
		return adminRepository.getSearchDetailCodeList(tpcdId, cmcdNm);
	}

	@Override
	public List<CommonCodeVO> getSearchGroupCodeList(String tpcdId, String cmcdNm, String useYn) {
		return adminRepository.getSearchGroupCodeList(tpcdId, cmcdNm, useYn);
	}
	
	@Override
	public int getGroupCodeCnt() {
		return adminRepository.getGroupCodeCnt();
	}
	
	@Override
	public List<PostVO> getNoticeListAll() {
		return adminRepository.getNoticeListAll();
	}
	
	@Override
	public List<PostVO> getInquiryListAll() {
		return adminRepository.getInquiryListAll();
	}
	
	@Override
	public List<ManagerVO> getManagerListAll() {
		return adminRepository.getManagerListAll();
	}
	
	@Override
	public List<CompanyVO> getCompanyListAll() {
		return adminRepository.getCompanyListAll();
	}

	@Override
	public List<ClassVO> getClassListAll() {
		return adminRepository.getClassListAll();
	}
	
	@Override
	public List<StudentVO> getStudentListAll() {
		return adminRepository.getStudentListAll();
	}
	
	@Override
	public List<CommonCodeVO> getGroupCodeListAll() {
		return adminRepository.getGroupCodeListAll();
	}
	
	@Override
	public int getWaitClassCnt() {
		return adminRepository.getWaitClassCnt();
	}

	@Override
	public List<CommonCodeVO> getNoticeCommonCodeListByInsert() {
		return adminRepository.getNoticeCommonCodeListByInsert();
	}
}
