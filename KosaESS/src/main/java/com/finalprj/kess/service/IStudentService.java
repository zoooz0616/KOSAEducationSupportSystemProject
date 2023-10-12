package com.finalprj.kess.service;

import java.sql.Clob;
import java.sql.Timestamp;
import java.util.List;

import com.finalprj.kess.dto.ApplyDetailDTO;
import com.finalprj.kess.dto.CurriculumDetailDTO;
import com.finalprj.kess.model.ApplyVO;
import com.finalprj.kess.model.ClassVO;
import com.finalprj.kess.model.CommonCodeVO;
import com.finalprj.kess.model.CurriculumVO;
import com.finalprj.kess.model.FileVO;
import com.finalprj.kess.model.LoginVO;
import com.finalprj.kess.model.PostVO;
import com.finalprj.kess.model.ReasonVO;
import com.finalprj.kess.model.RegistrationVO;
import com.finalprj.kess.model.StudentVO;
import com.finalprj.kess.model.WorklogVO;

public interface IStudentService {

	List<PostVO> selectAllNotice();

	List<ClassVO> selectAllClass();

	int getCmptClass(String stdtId);
	
	int getAplyClass(String stdtId);

	List<PostVO> selectNoticeMain();

	List<ClassVO> selectClassMain();

	List<PostVO> selectAllInquiry();

	ClassVO selectClass(String clssId);

	List<FileVO> selectAllClassFile(String clssId);


	LoginVO selectUser(String email);

	ClassVO getIngClass(String stdtId);

	List<ClassVO> searchClasses(String keyword, String ingClass);

	String getMaxAplyId();

	void uploadAplyFile(ApplyVO apply);

	int getAplyYN(String stdtId, String classId);

	ClassVO selectviewClass(String viewClass);

	List<CurriculumDetailDTO> getCurriculumList(String clssId);

	List<ApplyDetailDTO> searchAplyList(String stdtId);

	void updateaply(String aplyCd, String aplyId);

	void insertRgst(String aplyId, String maxRgstId, String stdtId);

	String getMaxRegistrationId();

	void updateAplyFile(String aplyId, FileVO fileVO, int maxFileSubId);

	int getmaxSubId(String aplyId);

	void updateAplydt(String aplyId, String stdtId);

	List<RegistrationVO> searchRgstList(String stdtId);

	int getRgstIngCnt(String stdtId);

	int getWlogIdCnt(String stdtId, String clssId);

	String getLastWlogId(String stdtId, String clssId);

	WorklogVO getLastWlogVO(String lastWlogId);

	String getMaxWlogId();

	void insertNewWlog(WorklogVO inWlogVO);

	ClassVO getWlogClass(String clssId);

	List<WorklogVO> searchWlogList(String stdtId);

	String getMaxResnId();

	void uploadResnFile(ReasonVO resn);

	void updateResnFile(String resnId, FileVO fileVO);

	void updateResndt(String resnId, String stdtId, String resnText, String maxFileId);

	StudentVO getstdtInfo(String stdtId);

	String getPassword(String stdtId);
	
	WorklogVO getNewWlog(String maxWlogId);

	void insertPastWlog(WorklogVO pastwlogVO);

	void getUpdateOutlog(Timestamp newOutTm, String outlogCd, String lastWlogId, Double totalTm);

	Timestamp getlastLogTime(String stdtId);

	PostVO selectNotice(String postId);

	List<FileVO> selectAllNoticeFile(String postId);

	boolean incrementHit(String postId);

	PostVO selectInquiry(String postId);

	List<FileVO> selectAllInquiryFile(String postId);

	List<PostVO> selectReply(String postId);

	List<FileVO> selectAllReplyFile(String postId, String replyId);

	List<PostVO> searchNotices(String keyword);

	List<PostVO> searchInquiries(String keyword);

	List<PostVO> searchPostList(String stdtId);

	List<PostVO> getReply(String postId);

	String getMaxPostId();

	void uploadInquiry(PostVO post);

	void deleteInquiry(String postId);

	PostVO getPostVO(String postId);

	void deleteFile(String fileId, List<String> deleteFiles);

	int getFileCnt(String fileId);

	void updatePostVO(PostVO postVO);

	List<ClassVO> getAllEvents();

	List<ClassVO> getStdtRgstEvents(String stdtId);

	List<ClassVO> getStdtAplyEvents(String stdtId);

	String getContent(String postId);

	List<CommonCodeVO> getCommonCodeList(String string);

	void updateInfo(StudentVO userInfo);

	void updatePwd(String pwd, String userEmail);

	void quit(String stdtEmail);

	String checkMember(String email);

	void updateSubcript(String memberYN);

}