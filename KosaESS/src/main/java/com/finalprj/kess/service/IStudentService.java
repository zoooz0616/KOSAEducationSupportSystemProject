package com.finalprj.kess.service;

import java.util.List;

import com.finalprj.kess.dto.ClassDetailDTO;
import com.finalprj.kess.model.ApplyVO;
import com.finalprj.kess.model.ClassVO;
import com.finalprj.kess.model.FileVO;
import com.finalprj.kess.model.LoginVO;
import com.finalprj.kess.model.PostVO;

public interface IStudentService {

	List<PostVO> selectAllNotice();

	List<ClassVO> selectAllClass();

	int getCmptClass(String email);
	
	int getAplyClass(String email);

	List<PostVO> selectNoticeMain();

	List<ClassVO> selectClassMain();

	List<PostVO> selectAllInquiry();

	ClassDetailDTO selectClass(String clssId);

	List<ClassDetailDTO> selectAllClassFile(String clssId);

	FileVO getFile(String fileId, String fileSubId);

	void uploadFile(ApplyVO apply);

	LoginVO selectUser(String email);

	String getIngClass(String email);


}