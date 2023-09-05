package com.finalprj.kess.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.finalprj.kess.dto.ClassDetailDTO;
import com.finalprj.kess.model.ClassVO;
import com.finalprj.kess.model.FileVO;
import com.finalprj.kess.model.LoginVO;
import com.finalprj.kess.model.PostVO;

@Mapper
@Repository
public interface IStudentRepository {
	List<PostVO> selectAllNotice();

	List<ClassVO> selectAllClass();

	int getAplyClass(String email);

	int getCmptClass(String email);

	List<PostVO> selectNoticeMain();

	List<ClassVO> selectClassMain();

	List<PostVO> selectAllInquiry();

	ClassDetailDTO selectClass(String clssId);

	List<ClassDetailDTO> selectAllClassFile(String clssId);

	FileVO getFile(String fileId);

	LoginVO selectUser(String email);

		
}
