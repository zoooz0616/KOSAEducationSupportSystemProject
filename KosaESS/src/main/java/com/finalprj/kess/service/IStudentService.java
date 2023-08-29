package com.finalprj.kess.service;

import java.util.List;

import com.finalprj.kess.model.ClassVO;
import com.finalprj.kess.model.PostVO;
import com.finalprj.kess.model.StudentVO;

public interface IStudentService {

	StudentVO selectStudent(String stdtEmail);

	List<PostVO> selectAllNotice();

	List<ClassVO> selectAllClass();

	int getCmptClass(String stdtEmail);

	List<PostVO> selectNoticeMain();

	List<ClassVO> selectClassMain();

	List<PostVO> selectAllInquiry();

}