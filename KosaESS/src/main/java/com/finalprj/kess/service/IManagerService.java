package com.finalprj.kess.service;

import java.util.List;

import com.finalprj.kess.model.ClassVO;
import com.finalprj.kess.model.CommonCodeVO;
import com.finalprj.kess.model.FileVO;
import com.finalprj.kess.model.StudentVO;

public interface IManagerService {
	public List<ClassVO> getClassListByMngrId(String mngrId);

	public int getApplyCountByClssId(String clssId);

	public ClassVO getClassDetailByClssId(String clssId);

	public List<StudentVO> getStudentListByClssId(String clssId);

	public String getClassNameByClssId(String clssId);

	public int getRgstCountByClssId(String clssId);

	public List<Integer> getFileSubIdListByFileId(String fileId);
	
	public List<CommonCodeVO> getCodeNameList(String keyword);
	
	public FileVO getFileByIds(String fileId, int fileSubId);

	public FileVO getFileInfoByIds(String fileId, int fileSubId);

	public int getCountLateArriveByStdtId(String stdtId);

	public int getCountEalryLeaveByStdtId(String stdtId);

	public int getCountAbsentByStdtId(String stdtId);

}