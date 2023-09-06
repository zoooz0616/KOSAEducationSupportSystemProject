package com.finalprj.kess.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finalprj.kess.dto.ClassDetailDTO;
import com.finalprj.kess.model.ApplyVO;
import com.finalprj.kess.model.ClassVO;
import com.finalprj.kess.model.FileVO;
import com.finalprj.kess.model.LoginVO;
import com.finalprj.kess.model.PostVO;
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
	public ClassDetailDTO selectClass(String clssId) {
		return studentRepository.selectClass(clssId);
	}

	@Override
	public List<ClassDetailDTO> selectAllClassFile(String clssId) {
		return studentRepository.selectAllClassFile(clssId);
	}

	@Override
	public FileVO getFile(String fileId, String fileSubId) {
		return studentRepository.getFile(fileId, fileSubId);
	}

	@Override
	public void uploadFile(ApplyVO apply) {		
	}

	@Override
	public LoginVO selectUser(String email) {
		return studentRepository.selectUser(email);
	}

	@Override
	public int getCmptClass(String email) {
		return studentRepository.getCmptClass(email);
	}

	@Override
	public int getAplyClass(String email) {
		return studentRepository.getAplyClass(email);
	}

	@Override
	public String getIngClass(String email) {
		return studentRepository.getIngClass(email);
	}



}
