package com.finalprj.kess.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finalprj.kess.model.ClassVO;
import com.finalprj.kess.model.PostVO;
import com.finalprj.kess.model.StudentVO;
import com.finalprj.kess.repository.IStudentRepository;

@Service
public class StudentService implements IStudentService {
	@Autowired
	IStudentRepository studentRepository;

	@Override
	public StudentVO selectStudent(String stdtEmail) {
		return studentRepository.selectStudent(stdtEmail);
	}

	public List<PostVO> selectAllNotice() {
		return studentRepository.selectAllNotice();
	}

	@Override
	public List<ClassVO> selectAllClass() {
		return studentRepository.selectAllClass();
	}

	@Override
	public int getCmptClass(String stdtEmail) {
		return studentRepository.getCmptClass(stdtEmail);
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
	public int getAplyClass(String stdtEmail) {
		return studentRepository.getAplyClass(stdtEmail);
	}
}
