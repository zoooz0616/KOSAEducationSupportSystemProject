package com.finalprj.kess.service;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finalprj.kess.dto.StudentInfoDTO;
import com.finalprj.kess.model.ClassVO;
import com.finalprj.kess.model.CommonCodeVO;
import com.finalprj.kess.model.FileVO;
import com.finalprj.kess.model.StudentVO;
import com.finalprj.kess.repository.IManagerRepository;

@Service
public class ManagerService implements IManagerService {
	@Autowired
	IManagerRepository managerRepository;

	@Override
	public List<ClassVO> getClassListByMngrId(String mngrId, String sortBy, String order) {
		return managerRepository.getClassListByMngrId(mngrId, sortBy, order);
	}

	@Override
	public int getApplyCountByClssId(String clssId) {
		return managerRepository.getApplyCountByClssId(clssId);
	}

	@Override
	public ClassVO getClassDetailByClssId(String clssId) {
		return managerRepository.getClassDetailByClssId(clssId);
	}

	@Override
	public List<StudentInfoDTO> getStudentListByClssId(String clssId) {
		return managerRepository.getStudentListByClssId(clssId);
	}

	@Override
	public String getClassNameByClssId(String clssId) {
		return managerRepository.getClassNameByClssId(clssId);
	}

	@Override
	public int getRgstCountByClssId(String clssId) {
		return managerRepository.getRgstCountByClssId(clssId);
	}

	@Override
	public List<CommonCodeVO> getCodeNameList(String keyword) {
		return managerRepository.getCodeNameListByKeyword(keyword);
	}

	@Override
	public List<Integer> getFileSubIdListByFileId(String fileId) {
		return managerRepository.getFileSubIdListByFileId(fileId);
	}

	@Override
	public FileVO getFileByIds(String fileId, int fileSubId) {
		return managerRepository.getFileByIds(fileId, fileSubId);
	}

	@Override
	public FileVO getFileInfoByIds(String fileId, int fileSubId) {
		return managerRepository.getFileInfoByIds(fileId, fileSubId);
	}

	@Override
	public int getCountLateArriveByStdtId(String stdtId) {
		return managerRepository.getCountLateArriveByStdtId(stdtId);
	}

	@Override
	public int getCountEalryLeaveByStdtId(String stdtId) {
		return managerRepository.getCountEalryLeaveByStdtId(stdtId);
	}

	@Override
	public int getCountAbsentByStdtId(String stdtId) {
		return managerRepository.getCountAbsentByStdtId(stdtId);
	}

	@Override
	public int getCountByClssIdWlogCdStdtId(String clssId, String wlogCd, String stdtId, String startDate, String endDate) {
		return managerRepository.getCountByClssIdWlogCdStdtId(clssId, wlogCd, stdtId, startDate, endDate);
	}

	@Override
	public String getLatestClassIdByMngrId(String mngrId) {
		return managerRepository.getLatestClassIdByMngrId(mngrId);
	}

	@Override
	public List<ClassVO> getFilteredClassListByMngrId(String mngrId, List<String> filterString) {
		return managerRepository.getFilteredClassListByMngrId(mngrId, filterString);
	}
//	@Override
//	public List<ClassVO> getFilteredClassListByMngrId(String mngrId, String[] filterString) {
//		return managerRepository.getFilteredClassListByMngrId(mngrId, filterString);
//	}

	@Override
	public void updateStdtCmptCd(String stdtId, String clssId, String targetCmptId) {
		managerRepository.updateStdtCmptCd(stdtId, clssId, targetCmptId);
	}

	@Override
	public double getTotalTmByClssId(String classId) {
		return managerRepository.getTotalTmByClssId(classId);
	}

	@Override
	public double getStudentTmSumByIds(String classId, String stdtId) {
		return managerRepository.getStudentTmSumByIds(classId, stdtId);
	}

//	@Override
//	public List<StudentInfoDTO> getStudentListBySearch(String classId, String startDate, String endDate) {
//		return managerRepository.getStudentListBySearch(classId, startDate, endDate);
//	}

}