package com.finalprj.kess.repository;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.finalprj.kess.model.LoginVO;
import com.finalprj.kess.model.ManagerVO;
import com.finalprj.kess.model.StudentVO;

@Repository
@Mapper
public interface IMainRepository {
	LoginVO getRole(String email, String pwd);
	ManagerVO getManagerVO(String email);
	void updateLastLoginDt(String userEmail);
	StudentVO getStudentVO(String email);
	int getEmailCnt(String email);
	void insertLgin(StudentVO student);
	void insertStudent(StudentVO student);
	String getMaxStdtId();
	String getMember(String email);
	String getMemberId(String name, String phone);
	String getMemberPwd(String name, String email, String phone);
	void changePwd(String email, String randomCode);
	String checkMemberCd(String userId);
}
