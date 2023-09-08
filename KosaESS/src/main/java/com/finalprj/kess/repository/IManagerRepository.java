package com.finalprj.kess.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.finalprj.kess.dto.StudentInfoDTO;
import com.finalprj.kess.model.ClassVO;
import com.finalprj.kess.model.CommonCodeVO;
import com.finalprj.kess.model.FileVO;
import com.finalprj.kess.model.StudentVO;

@Mapper
@Repository
public interface IManagerRepository {

	public List<ClassVO> getClassListByMngrId(String mngrId);

	public int getApplyCountByClssId(String clssId);

	public List<CommonCodeVO> getCodeNameListByKeyword(String keyword);

	public ClassVO getClassDetailByClssId(String clssId);

	public List<StudentInfoDTO> getStudentListByClssId(String clssId);

	public String getClassNameByClssId(String clssId);

	public int getRgstCountByClssId(String clssId);

	public List<Integer> getFileSubIdListByFileId(String fileId);

	public FileVO getFileByIds(@Param("fileId") String fileId, @Param("fileSubId") int fileSubId);

	public FileVO getFileInfoByIds(@Param("fileId") String fileId, @Param("fileSubId") int fileSubId);

	public int getCountLateArriveByStdtId(String stdtId);

	public int getCountAbsentByStdtId(String stdtId);

	public int getCountEalryLeaveByStdtId(String stdtId);
	
	public int getCountByClssIdWlogCdStdtId (@Param("clssId") String clssId, @Param("wlogCd") String wlogCd, @Param("stdtId") String stdtId);
}
