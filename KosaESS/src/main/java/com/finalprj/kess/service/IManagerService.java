package com.finalprj.kess.service;

import java.sql.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.finalprj.kess.dto.StudentInfoDTO;
import com.finalprj.kess.model.ClassVO;
import com.finalprj.kess.model.CommonCodeVO;
import com.finalprj.kess.model.FileVO;
import com.finalprj.kess.model.StudentVO;
import com.finalprj.kess.model.WorklogVO;

public interface IManagerService {
	public List<ClassVO> getClassListByMngrId(String mngrId, String sortBy, String order);

	public int getApplyCountByClssId(String clssId);

	public ClassVO getClassDetailByClssId(String clssId);

	public List<StudentInfoDTO> getStudentListByClssId(String clssId);

	public String getClassNameByClssId(String clssId);

	public int getRgstCountByClssId(String clssId);

	public List<Integer> getFileSubIdListByFileId(String fileId);
	
	public List<CommonCodeVO> getCodeNameList(String keyword);
	
	public FileVO getFileByIds(String fileId, int fileSubId);

	public FileVO getFileInfoByIds(String fileId, int fileSubId);

	public int getCountLateArriveByStdtId(String stdtId);

	public int getCountEalryLeaveByStdtId(String stdtId);

	public int getCountAbsentByStdtId(String stdtId);

	public int getCountByClssIdWlogCdStdtId (String clssId, String wlogCd, String stdtId, String startDate, String endDate);

	public String getLatestClassIdByMngrId(String mngrId);

	public List<ClassVO> getFilteredClassListByMngrId(String mngrId, List<String> filterString);
//	public List<ClassVO> getFilteredClassListByMngrId(String mngrId, String[] filterString);

	public void updateStdtCmptCd(String mngrId, String stdtId, String clssId, String targetCmptId);

	public double getTotalTmByClssId(String classId);

	public double getStudentTmSumByIds(String classId, String stdtId);

	public List<WorklogVO> getWlogListByClssIdDate(String clssId, String startDate, String endDate);

//	public List<StudentInfoDTO> getStudentListBySearch(String classId, String startDate, String endDate);
}