package com.finalprj.kess.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.finalprj.kess.dto.ApplyDetailDTO;
import com.finalprj.kess.dto.CurriculumDetailDTO;
import com.finalprj.kess.model.ApplyVO;
import com.finalprj.kess.model.ClassVO;
import com.finalprj.kess.model.CurriculumVO;
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

	ClassVO selectClass(String clssId);

	List<ClassVO> selectAllClassFile(String clssId);

	FileVO getFile(String fileId, String fileSubId);

	LoginVO selectUser(String email);

	String getIngClass(String email);

	List<ClassVO> searchClasses(String keyword, String ingClass);

	String getMaxAplyId();

	void uploadAplyFile(ApplyVO apply);

	int getAplyYN(String stdtId, String classId);

	ClassVO selectviewClass(String viewClass);

	List<CurriculumDetailDTO> getCurriculumList(String clssId);

	List<ApplyDetailDTO> searchAplyList(String stdtId);

	void  updateaply(String aplyCd, String aplyId);

	void insertRgst(String aplyId, String maxRgstId, String stdtId);

	String getMaxRegistrationId();

	void updateAplyFile(String aplyId, FileVO fileVO, int maxFileSubId);

	int getmaxSubId(String aplyId);

	void updateAplydt(String aplyId, String stdtId);


		
}
