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
import com.finalprj.kess.model.SubjectVO;
import com.finalprj.kess.repository.IAdminRepository;
import com.finalprj.kess.repository.IUploadFileRepository;

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
	public List<ClassVO> getClassList() {
		return adminRepository.getClassList();
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
	public List<CompanyVO> getCompanyList() {
		return adminRepository.getCompanyList();
	}

	@Override
	public List<ManagerVO> getManagerList() {
		return adminRepository.getManagerList();
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

	//삭제할거임
	@Override
	public void insertClassVO(ClassVO classVO) {
		adminRepository.insertClassVO(classVO);
	}

	//삭제할거임
	@Override
	public void insertCurriculumVO(CurriculumVO curriculumVO) {
		adminRepository.insertCurriculumVO(curriculumVO);
	}

	@Override
	@Transactional
	public void createClass(List<FileVO> fileList, ClassVO classVO, List<CurriculumVO> curriculumList) {
		if (fileList != null) {
			for(FileVO fileVO : fileList) {
				uploadFileRepository.uploadFile(fileVO);
			}
		}

		adminRepository.insertClassVO(classVO);

		if (curriculumList != null) {
			for(CurriculumVO curriculumVO : curriculumList) {
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
			for(FileVO fileVO : fileList) {
				uploadFileRepository.uploadFile(fileVO);
			}
		}
		adminRepository.updateClassVO(classVO);

		if (curriculumList != null) {
			adminRepository.deleteCurriculum(classVO.getClssId());
			for(CurriculumVO curriculumVO : curriculumList) {
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
	public List<PostVO> getNoticeList() {
		return adminRepository.getNoticeList();
	}

	@Override
	public List<PostVO> getInquiryList() {
		return adminRepository.getInquiryList();
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
	public List<CommonCodeVO> getGroupCodeList() {
		return adminRepository.getGroupCodeList();
	}

	@Override
	public String getMaxNoticeId() {
		return adminRepository.getMaxNoticeId();
	}

	@Override
	@Transactional
	public void insertNoticeVO(List<FileVO> fileList, PostVO postVO) {
		if (fileList != null) {
			for(FileVO fileVO : fileList) {
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
			for(FileVO fileVO : fileList) {
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
	public int getWaitClassCnt() {
		return adminRepository.getWaitClassCnt();
	}


	@Override
	public List<String> getClassSearch(String term) {
		return adminRepository.getClassSearch(term);
	}

	@Override
	public List<ClassVO> getSearchClassVOList(String className, List<String> status, Date aplyStartDt,
			Date aplyEndDt, Date classStartDd, Date classEndDd) {
		return adminRepository.getSearchClassVOList(className, status, aplyStartDt,
				aplyEndDt, classStartDd, classEndDd);
	}
}
