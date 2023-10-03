package com.finalprj.kess.repository;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.finalprj.kess.model.ManagerVO;
import com.finalprj.kess.model.StudentVO;

@Repository
@Mapper
public interface IMainRepository {
	String getRole(String email, String pwd);
	ManagerVO getManagerVO(String email);
	void updateLastLoginDt(String userEmail);
	StudentVO getStudentVO(String email);
	int getEmailCnt(String email);
	void insertLgin(StudentVO student);
	void insertStudent(StudentVO student);
	String getMaxStdtId();
}
