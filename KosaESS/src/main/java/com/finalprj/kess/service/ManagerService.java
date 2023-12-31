package com.finalprj.kess.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finalprj.kess.dto.ReasonDTO;
import com.finalprj.kess.dto.StudentInfoDTO;
import com.finalprj.kess.dto.SubsidyDTO;
import com.finalprj.kess.dto.WorklogDTO;
import com.finalprj.kess.model.ClassVO;
import com.finalprj.kess.model.CommonCodeVO;
import com.finalprj.kess.model.FileVO;
import com.finalprj.kess.model.ManagerVO;
import com.finalprj.kess.model.SubsidyVO;
import com.finalprj.kess.repository.IManagerRepository;

@Service
public class ManagerService implements IManagerService {
	@Autowired
	IManagerRepository managerRepository;

	@Override
	public List<ClassVO> getClassListByMngrId(String mngrId, Integer year) {
		return managerRepository.getClassListByMngrId(mngrId, year);
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
	public List<StudentInfoDTO> getStudentListByClssId(String clssId, String keyword, List<String> cmptList) {
		return managerRepository.getStudentListByClssId(clssId, keyword, cmptList);
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
	public int getCountByClssIdWlogCdStdtId(String clssId, String wlogCd, String stdtId, String startDate, String endDate) {
		return managerRepository.getCountByClssIdWlogCdStdtId(clssId, wlogCd, stdtId, startDate, endDate);
	}

	@Override
	public String getLatestClassIdByMngrId(String mngrId) {
		return managerRepository.getLatestClassIdByMngrId(mngrId);
	}

	@Override
	public List<ClassVO> getFilteredClassListByMngrId(String mngrId, List<String> filterString, String searchKeyword, Integer year) {
		return managerRepository.getFilteredClassListByMngrId(mngrId, filterString,  searchKeyword, year);
	}

	@Override
	public void updateStdtCmptCd(String mngrId, String stdtId, String clssId, String targetCmptId) {
		managerRepository.updateStdtCmptCd(mngrId, stdtId, clssId, targetCmptId);
	}

	@Override
	public double getTotalTmByClssId(String classId) {
		return managerRepository.getTotalTmByClssId(classId);
	}

	@Override
	public double getStudentTmSumByIds(String classId, String stdtId) {
		return managerRepository.getStudentTmSumByIds(classId, stdtId);
	}

	@Override
	public List<WorklogDTO> getWlogListByClssIdDate(String mngrId, String clssId, String startDate, String endDate, String keyword, String isDelete, String resnOnly, List<String> filterString, Integer page, Integer size) {
		return managerRepository.getWlogListByClssIds(mngrId, clssId, startDate, endDate, keyword, isDelete, resnOnly, filterString, page, size);
	}

	@Override
	public List<StudentInfoDTO> getStudentListByOnlyClssId(String classId) {
		return managerRepository.getStudentListByOnlyClssId(classId);
	}

	@Override
	public ReasonDTO getResnDetailByResnId(String resnId) {
		return managerRepository.getResnDetailByResnId(resnId);
	}

	@Override
	public void updateResnCd(String resnId, String resnCd, String mngrId) {
		managerRepository.updateResnCd(resnId, resnCd, mngrId);
	}

	@Override
	public void updateWlogCd(String wlogId, String wlogCd, String mngrId) {
		managerRepository.updateWlogCd(wlogId, wlogCd, mngrId);
	}

	@Override
	public void updateManagerInfo(ManagerVO updateManager) {
		managerRepository.updateManagerInfo(updateManager);
		managerRepository.updateManagerLoginInfo(updateManager);
	}

	@Override
	public List<SubsidyDTO> getSubsidyList(String mngrId, String clssId, String startDate, String endDate, String keyword, List<String> filterString, Integer page, Integer size) {
		return managerRepository.getSubsidyList(mngrId, clssId, startDate, endDate, keyword, filterString, page, size);
	}

	@Override
	public void deleteWlog(String wlogId) {
		managerRepository.deleteWlog(wlogId);
	}

	@Override
	public ClassVO getCodeVO(String resnCd) {
		return managerRepository.getCodeVO(resnCd);
	}

	@Override
	public List<Integer> getYearList(String mngrId) {
		return managerRepository.getYearList(mngrId);
	}

	@Override
	public void insertSubsidy(SubsidyVO subsidyVO) {
		managerRepository.insertSubsidy(subsidyVO);
	}

	@Override
	public int getMaxId(String tableName, String columnName) {
		return managerRepository.getMaxId(tableName, columnName);
	}

	@Override
	public void updateWlogTotalTime(String wlogId, String mngrId) {
		managerRepository.updateWlogTotalTime(wlogId, mngrId);
	}

	@Override
	public Integer getWlogListSize(String mngrId, String clssId, String startDate, String endDate, String keyword, String isDelete, String resnOnly, List<String> filterString) {
		return managerRepository.getWlogListSize(mngrId, clssId, startDate, endDate, keyword, isDelete, resnOnly, filterString);
	}
}