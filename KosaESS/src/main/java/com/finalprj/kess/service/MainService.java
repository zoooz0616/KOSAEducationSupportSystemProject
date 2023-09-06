package com.finalprj.kess.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finalprj.kess.model.ManagerVO;
import com.finalprj.kess.model.StudentVO;
import com.finalprj.kess.repository.IMainRepository;

@Service
public class MainService implements IMainService{
	@Autowired
	IMainRepository mainRepository;

	@Override
	public String getRole(String email, String pwd) {
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
}
