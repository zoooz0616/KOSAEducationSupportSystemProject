package com.finalprj.kess.service;

import java.sql.Clob;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finalprj.kess.dto.ApplyDetailDTO;
import com.finalprj.kess.dto.CurriculumDetailDTO;
import com.finalprj.kess.model.ApplyVO;
import com.finalprj.kess.model.ClassVO;
import com.finalprj.kess.model.FileVO;
import com.finalprj.kess.model.LoginVO;
import com.finalprj.kess.model.PostVO;
import com.finalprj.kess.model.ReasonVO;
import com.finalprj.kess.model.RegistrationVO;
import com.finalprj.kess.model.StudentVO;
import com.finalprj.kess.model.WorklogVO;
import com.finalprj.kess.repository.IStudentRepository;
import com.finalprj.kess.repository.IUploadFileRepository;

@Service
public class StudentService implements IStudentService {
	@Autowired
	IStudentRepository studentRepository;

	@Autowired
	IUploadFileRepository uploadFileRepository;

	public List<PostVO> selectAllNotice() {
		return studentRepository.selectAllNotice();
	}

	@Override
	public List<ClassVO> selectAllClass() {
		return studentRepository.selectAllClass();
	}

	@Override
	public List<PostVO> selectNoticeMain() {
		return studentRepository.selectNoticeMain();
	}

	@Override
	public List<ClassVO> selectClassMain() {
		return studentRepository.selectClassMain();
	}

	@Override
	public List<PostVO> selectAllInquiry() {
		return studentRepository.selectAllInquiry();
	}

	@Override
	public ClassVO selectClass(String clssId) {
		return studentRepository.selectClass(clssId);
	}

	@Override
	public List<FileVO> selectAllClassFile(String clssId) {
		return studentRepository.selectAllClassFile(clssId);
	}

	@Override
	public LoginVO selectUser(String email) {
		return studentRepository.selectUser(email);
	}

	@Override
	public int getCmptClass(String stdtId) {
		return studentRepository.getCmptClass(stdtId);
	}

	@Override
	public int getAplyClass(String stdtId) {
		return studentRepository.getAplyClass(stdtId);
	}

	@Override
	public ClassVO getIngClass(String stdtId) {
		return studentRepository.getIngClass(stdtId);
	}

	@Override
	public List<ClassVO> searchClasses(String keyword, String ingClass) {
		return studentRepository.searchClasses(keyword, ingClass);
	}

	@Override
	public String getMaxAplyId() {
		return studentRepository.getMaxAplyId();
	}

	@Override
	public void uploadAplyFile(ApplyVO apply) {
		studentRepository.uploadAplyFile(apply);
	}

	@Override
	public int getAplyYN(String stdtId, String classId) {
		return studentRepository.getAplyYN(stdtId, classId);
	}

	@Override
	public ClassVO selectviewClass(String viewClass) {
		return studentRepository.selectviewClass(viewClass);
	}

	@Override
	public List<CurriculumDetailDTO> getCurriculumList(String clssId) {
		return studentRepository.getCurriculumList(clssId);
	}

	@Override
	public List<ApplyDetailDTO> searchAplyList(String stdtId) {
		return studentRepository.searchAplyList(stdtId);
	}

	@Override
	public void updateaply(String aplyCd, String aplyId) {
		studentRepository.updateaply(aplyCd, aplyId);
	}

	@Override
	public void insertRgst(String aplyId, String maxRgstId, String stdtId) {
		studentRepository.insertRgst(aplyId, maxRgstId, stdtId);

	}

	@Override
	public String getMaxRegistrationId() {
		return studentRepository.getMaxRegistrationId();
	}

	@Override
	public void updateAplyFile(String aplyId, FileVO fileVO, int maxFileSubId) {
		studentRepository.updateAplyFile(aplyId, fileVO, maxFileSubId);
	}

	@Override
	public int getmaxSubId(String aplyId) {
		return studentRepository.getmaxSubId(aplyId);
	}

	@Override
	public void updateAplydt(String aplyId, String stdtId) {
		studentRepository.updateAplydt(aplyId, stdtId);
	}

	@Override
	public List<RegistrationVO> searchRgstList(String stdtId) {
		return studentRepository.searchRgstList(stdtId);
	}

	@Override
	public int getRgstIngCnt(String stdtId) {
		return studentRepository.getRgstIngCnt(stdtId);
	}

	@Override
	public int getWlogIdCnt(String stdtId, String clssId) {
		return studentRepository.getWlogIdCnt(stdtId, clssId);
	}

	@Override
	public String getLastWlogId(String stdtId, String clssId) {
		return studentRepository.getLastWlogId(stdtId, clssId);
	}

	@Override
	public WorklogVO getLastWlogVO(String lastWlogId) {
		return studentRepository.getLastWlogVO(lastWlogId);
	}

	@Override
	public String getMaxWlogId() {
		return studentRepository.getMaxWlogId();
	}

	@Override
	public void insertNewWlog(WorklogVO inWlogVO) {
		studentRepository.insertNewWlog(inWlogVO);
	}

	@Override
	public ClassVO getWlogClass(String clssId) {
		return studentRepository.getWlogClass(clssId);
	}

	@Override
	public List<WorklogVO> searchWlogList(String stdtId) {
		return studentRepository.searchWlogList(stdtId);
	}

	@Override
	public String getMaxResnId() {
		return studentRepository.getMaxResnId();
	}

	@Override
	public void uploadResnFile(ReasonVO resn) {
		studentRepository.uploadResnFile(resn);
	}

	@Override
	public void updateResnFile(String resnId, FileVO fileVO) {
		studentRepository.updateResnFile(resnId, fileVO);
	}

	@Override
	public void updateResndt(String resnId, String stdtId, String resnText) {
		studentRepository.updateResndt(resnId, stdtId, resnText);
	}

	@Override
	public StudentVO getstdtInfo(String stdtId) {
		return studentRepository.getstdtInfo(stdtId);
	}

	@Override
	public String getPassword(String stdtId) {
		return studentRepository.getPassword(stdtId);
	}

	@Override
	public WorklogVO getNewWlog(String maxWlogId) {
		return studentRepository.getNewWlog(maxWlogId);
	}

	@Override
	public void insertPastWlog(WorklogVO pastwlogVO) {
		studentRepository.insertPastWlog(pastwlogVO);
	}

	@Override
	public void getUpdateOutlog(Timestamp newOutTm, String outlogCd, String lastWlogId, Double totalTm) {
		studentRepository.getUpdateOutlog(newOutTm, outlogCd, lastWlogId, totalTm);
	}

	@Override
	public Timestamp getlastLogTime(String stdtId) {
		return studentRepository.getlastLogTime(stdtId);
	}

	@Override
	public PostVO selectNotice(String postId) {
		return studentRepository.selectNotice(postId);
	}

	@Override
	public List<FileVO> selectAllNoticeFile(String postId) {
		return studentRepository.selectAllNoticeFile(postId);
	}

	@Override
	public boolean incrementHit(String postId) {
		return studentRepository.incrementHit(postId);
	}

	@Override
	public PostVO selectInquiry(String postId) {
		return studentRepository.selectInquiry(postId);
	}

	@Override
	public List<FileVO> selectAllInquiryFile(String postId) {
		return studentRepository.selectAllInquiryFile(postId);
	}

	@Override
	public List<PostVO> selectReply(String postId) {
		return studentRepository.selectReply(postId);
	}

	@Override
	public List<FileVO> selectAllReplyFile(String postId, String replyId) {
		return studentRepository.selectAllReplyFile(postId, replyId);
	}

	@Override
	public List<PostVO> searchNotices(String keyword) {
		return studentRepository.searchNotices(keyword);
	}

	@Override
	public List<PostVO> searchInquiries(String keyword) {
		return studentRepository.searchInquiries(keyword);
	}

	@Override
	public List<PostVO> searchPostList(String stdtId) {
		return studentRepository.searchPostList(stdtId);
	}

	@Override
	public List<PostVO> getReply(String postId) {
		return studentRepository.getReply(postId);
	}

	@Override
	public String getMaxPostId() {
		return studentRepository.getMaxPostId();
	}

	@Override
	public void uploadInquiry(PostVO post) {
		studentRepository.uploadInquiry(post);
	}

	@Override
	public void deleteInquiry(String postId) {
		studentRepository.deleteInquiry(postId);
	}

	@Override
	public PostVO getPostVO(String postId) {
		return studentRepository.getPostVO(postId);
	}

	@Override
	public void deleteFile(String fileId, List<String> deleteFiles) {
		studentRepository.deleteFile(fileId, deleteFiles);
	}

	@Override
	public int getFileCnt(String fileId) {
		return studentRepository.getFileCnt(fileId);
	}

	@Override
	public void updatePostVO(List<FileVO> fileList, PostVO postVO) {
		if (fileList != null) {
			for (FileVO fileVO : fileList) {
				uploadFileRepository.uploadFile(fileVO);
			}
		}

		studentRepository.updatePostVO(postVO);
	}

	public List<ClassVO> getAllEvents() {
		return studentRepository.getAllEvents();
	}

	public List<ClassVO> getStdtRgstEvents(String stdtId) {
		return studentRepository.getStdtRgstEvents(stdtId);
	}
	
	public List<ClassVO> getStdtAplyEvents(String stdtId) {
		return studentRepository.getStdtAplyEvents(stdtId);
	}

	@Override
	public String getContent(String postId) {
		return studentRepository.getContent(postId);
	}

}
