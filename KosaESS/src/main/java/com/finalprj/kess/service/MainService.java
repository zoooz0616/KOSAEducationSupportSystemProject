package com.finalprj.kess.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.finalprj.kess.model.LoginVO;
import com.finalprj.kess.model.ManagerVO;
import com.finalprj.kess.model.StudentVO;
import com.finalprj.kess.repository.IMainRepository;

@Service
public class MainService implements IMainService {
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

	@Override
	public String getMemberId(String name, String phone) {
		return mainRepository.getMemberId(name, phone);
	}

	@Override
	public String getMemberPwd(String name, String email, String phone) {
		return mainRepository.getMemberPwd(name, email, phone);
	}

	@Override
	public void changePwd(String email, String randomCode) {
		mainRepository.changePwd(email, randomCode);
	}

	@Override
	public String checkMemberCd(String userId) {
		return mainRepository.checkMemberCd(userId) ;
	}
}
