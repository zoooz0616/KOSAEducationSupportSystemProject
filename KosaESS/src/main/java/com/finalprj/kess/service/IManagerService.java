package com.finalprj.kess.service;

import java.sql.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestParam;

import com.finalprj.kess.dto.ReasonDTO;
import com.finalprj.kess.dto.StudentInfoDTO;
import com.finalprj.kess.dto.SubsidyDTO;
import com.finalprj.kess.dto.WorklogDTO;
import com.finalprj.kess.model.ClassVO;
import com.finalprj.kess.model.CommonCodeVO;
import com.finalprj.kess.model.FileVO;
import com.finalprj.kess.model.ManagerVO;
import com.finalprj.kess.model.ReasonVO;
import com.finalprj.kess.model.StudentVO;
import com.finalprj.kess.model.SubsidyVO;
import com.finalprj.kess.model.WorklogVO;

public interface IManagerService {
	public List<ClassVO> getClassListByMngrId(String mngrId, Integer year);

	public int getApplyCountByClssId(String clssId);

	public ClassVO getClassDetailByClssId(String clssId);

	public List<StudentInfoDTO> getStudentListByClssId(String clssId, String keyword, List<String> cmptList);

	public String getClassNameByClssId(String clssId);

	public int getRgstCountByClssId(String clssId);

	public List<Integer> getFileSubIdListByFileId(String fileId);
	
	public List<CommonCodeVO> getCodeNameList(String keyword);
	
	public FileVO getFileByIds(String fileId, int fileSubId);

	public FileVO getFileInfoByIds(String fileId, int fileSubId);

	public int getCountByClssIdWlogCdStdtId (String clssId, String wlogCd, String stdtId, String startDate, String endDate);

	public String getLatestClassIdByMngrId(String mngrId);

	public List<ClassVO> getFilteredClassListByMngrId(String mngrId, List<String> filterString, String searchKeyword, Integer year);

	public void updateStdtCmptCd(String mngrId, String stdtId, String clssId, String targetCmptId);

	public double getTotalTmByClssId(String classId);

	public double getStudentTmSumByIds(String classId, String stdtId);

	public List<WorklogDTO> getWlogListByClssIdDate(String mngrId, String clssId, String startDate, String endDate, String keyword, String isDelete, String resnOnly, List<String> filterString);

	public List<StudentInfoDTO> getStudentListByOnlyClssId(String classId);

	public ReasonDTO getResnDetailByResnId(String resnId);

	public void updateResnCd(String resnId, String resnCd, String mngrId);

	public void updateWlogCd(String wlogId, String wlogCd, String mngrId);

	public void updateManagerInfo(ManagerVO updateManager);

	public List<SubsidyDTO> getSubsidyList(String mngrId, String clssId, String startDate, String endDate, String keyword, List<String> filterString);

	public void deleteWlog(String wlogId);

	public ClassVO getCodeVO(String resnCd);

	public List<Integer> getYearList(String mngrId);
	
	public void insertSubsidy(SubsidyVO subsidyVO);
}