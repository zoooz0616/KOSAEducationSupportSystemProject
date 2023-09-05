package com.finalprj.kess.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.finalprj.kess.model.ClassVO;
import com.finalprj.kess.model.FileVO;
import com.finalprj.kess.model.StudentVO;

@Mapper
@Repository
public interface IManagerRepository {

	public List<ClassVO> getClassListByMngrId(String mngrId);

	public int getApplyCountByClssId(String clssId);

	public String getClassCodeNameByClssId(String clssCd);

	public List<String> getClassCodeNameList();

	public ClassVO getClassDetailByClssId(String clssId);

	public FileVO getFileByFileId(String fileId);//이거 수정해야 될 예감

	public List<String> getFileIdListByclssId(String clssId);

	public List<StudentVO> getStudentListByClssId(String clssId);

	public List<ClassVO> getClassNameListByMngrId(String mngrId);

	public String getClassNameByClssId(String clssId);

	public int getRgstCountByClssId(String clssId);
}
