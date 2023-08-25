package com.finalprj.kess.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.finalprj.kess.model.ClassVO;
import com.finalprj.kess.model.PostVO;
import com.finalprj.kess.model.StudentVO;

@Mapper
@Repository
public interface IStudentRepository {
	StudentVO selectStudent(String stdtEmail);
	List<PostVO> selectAllNotice();
	List<ClassVO> selectAllClass();
	int getCmptClass(String stdtEmail);


}
