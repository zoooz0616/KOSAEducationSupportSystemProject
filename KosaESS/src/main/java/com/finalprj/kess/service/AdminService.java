package com.finalprj.kess.service;

import java.sql.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.finalprj.kess.dto.CurriculumDetailDTO;
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
	public int getWaitClassCnt() {
		return adminRepository.getWaitClassCnt();
	}

	@Override
	public List<PostVO> getPostVOList(String postValue) {
		return adminRepository.getPostVOList(postValue);
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
