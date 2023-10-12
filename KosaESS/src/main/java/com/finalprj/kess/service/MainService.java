package com.finalprj.kess.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.finalprj.kess.model.LoginVO;
import com.finalprj.kess.model.ManagerVO;
import com.finalprj.kess.model.StudentVO;
import com.finalprj.kess.repository.IMainRepository;

@Service
public class MainService implements IMainService{
	@Autowired
	IMainRepository mainRepository;

	@Override
	public LoginVO getRole(String email, String pwd) {
		return mainRepository.getRole(email, pwd);
	}

	@Override
	public ManagerVO getManagerVO(String email) {
		return mainRepository.getManagerVO(email);
	}

	@Override
	public void updateLastLoginDt(String userEmail) {
		mainRepository.updateLastLoginDt(userEmail);
	}

	@Override
	public StudentVO getStudentVO(String email) {
		return mainRepository.getStudentVO(email);
	}

	@Override
	public int getEmailCnt(String email) {
		return mainRepository.getEmailCnt(email);
	}

	@Override
	@Transactional
	public void insertStudent(StudentVO student) {
		mainRepository.insertLgin(student);
		mainRepository.insertStudent(student);
	}

	@Override
	public String getMaxStdtId() {
		return mainRepository.getMaxStdtId();
	}

	@Override
	public String getMember(String email) {
		return mainRepository.getMember(email);
	}
}
