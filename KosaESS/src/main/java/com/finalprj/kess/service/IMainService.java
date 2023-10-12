package com.finalprj.kess.service;

import com.finalprj.kess.model.LoginVO;
import com.finalprj.kess.model.ManagerVO;
import com.finalprj.kess.model.StudentVO;

public interface IMainService {
	LoginVO getRole(String email, String pwd);
	ManagerVO getManagerVO(String email);
	void updateLastLoginDt(String userEmail);
	StudentVO getStudentVO(String email);
	int getEmailCnt(String email);
	void insertStudent(StudentVO student);
	String getMaxStdtId();
	String getMember(String email);
}
