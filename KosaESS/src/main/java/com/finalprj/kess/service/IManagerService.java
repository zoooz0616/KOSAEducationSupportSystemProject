package com.finalprj.kess.service;

import java.util.List;

import com.finalprj.kess.model.ClassVO;
import com.finalprj.kess.model.FileVO;
import com.finalprj.kess.model.StudentVO;

public interface IManagerService {
	public List<ClassVO> getClassListByMngrId(String mngrId);

	public int getApplyCountByClssId(String clssId);

	public String getClassCodeNameByClssId(String clssCd);

	public List<String> getClassCodeNameList();

	public ClassVO getClassDetailByClssId(String clssId);

	public FileVO getFileByFileId(String fileId);

	public List<String> getFileIdListByClssId(String clssId);

	public List<StudentVO> getStudentListByClssId(String clssId);

	public List<ClassVO> getClassNameListByMngrId(String mngrId);

	public String getClassNameByClssId(String clssId);

	public int getRgstCountByClssId(String clssId);
}