package com.finalprj.kess.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finalprj.kess.dto.ApplyDetailDTO;
import com.finalprj.kess.dto.CurriculumDetailDTO;
import com.finalprj.kess.model.ApplyVO;
import com.finalprj.kess.model.ClassVO;
import com.finalprj.kess.model.CurriculumVO;
import com.finalprj.kess.model.FileVO;
import com.finalprj.kess.model.LoginVO;
import com.finalprj.kess.model.PostVO;
import com.finalprj.kess.model.RegistrationVO;
import com.finalprj.kess.repository.IStudentRepository;

@Service
public class StudentService implements IStudentService {
	@Autowired
	IStudentRepository studentRepository;

	public List<PostVO> selectAllNotice() {
		return studentRepository.selectAllNotice();
	}

	@Override
	public List<ClassVO> selectAllClass() {
		return studentRepository.selectAllClass();
	}

	@Override
	public List<PostVO> selectNoticeMain() {
		return studentRepository.selectNoticeMain();
	}

	@Override
	public List<ClassVO> selectClassMain() {
		return studentRepository.selectClassMain();
	}

	@Override
	public List<PostVO> selectAllInquiry() {
		return studentRepository.selectAllInquiry();
	}

	@Override
	public ClassVO selectClass(String clssId) {
		return studentRepository.selectClass(clssId);
	}

	@Override
	public List<FileVO> selectAllClassFile(String clssId) {
		return studentRepository.selectAllClassFile(clssId);
	}

	@Override
	public LoginVO selectUser(String email) {
		return studentRepository.selectUser(email);
	}

	@Override
	public int getCmptClass(String stdtId) {
		return studentRepository.getCmptClass(stdtId);
	}

	@Override
	public int getAplyClass(String stdtId) {
		return studentRepository.getAplyClass(stdtId);
	}

	@Override
	public ClassVO getIngClass(String stdtId) {
		return studentRepository.getIngClass(stdtId);
	}

	@Override
	public List<ClassVO> searchClasses(String keyword, String ingClass) {
		return studentRepository.searchClasses(keyword, ingClass);
	}

	@Override
	public String getMaxAplyId() {
		return studentRepository.getMaxAplyId();
	}

	@Override
	public void uploadAplyFile(ApplyVO apply) {
		studentRepository.uploadAplyFile(apply);
	}

	@Override
	public int getAplyYN(String stdtId, String classId) {
		return studentRepository.getAplyYN(stdtId, classId);
	}

	@Override
	public ClassVO selectviewClass(String viewClass) {
		return studentRepository.selectviewClass(viewClass);
	}

	@Override
	public List<CurriculumDetailDTO> getCurriculumList(String clssId) {
		return studentRepository.getCurriculumList(clssId);
	}

	@Override
	public List<ApplyDetailDTO> searchAplyList(String stdtId) {
		return studentRepository.searchAplyList(stdtId);
	}

	@Override
	public void updateaply(String aplyCd, String aplyId) {
		studentRepository.updateaply(aplyCd, aplyId);
	}

	@Override
	public void insertRgst(String aplyId, String maxRgstId, String stdtId) {
		studentRepository.insertRgst(aplyId, maxRgstId, stdtId);
		
	}

	@Override
	public String getMaxRegistrationId() {
		return studentRepository.getMaxRegistrationId();
	}

	@Override
	public void updateAplyFile(String aplyId, FileVO fileVO, int maxFileSubId) {
		studentRepository.updateAplyFile(aplyId, fileVO, maxFileSubId);
	}

	@Override
	public int getmaxSubId(String aplyId) {
		return studentRepository.getmaxSubId(aplyId);
	}

	@Override
	public void updateAplydt(String aplyId, String stdtId) {
		studentRepository.updateAplydt(aplyId, stdtId);
	}

	@Override
	public List<RegistrationVO> searchRgstList(String stdtId) {
		return studentRepository.searchRgstList(stdtId);
	}

	@Override
	public int getRgstIngCnt(String stdtId) {
		return studentRepository.getRgstIngCnt(stdtId);
	}


}
