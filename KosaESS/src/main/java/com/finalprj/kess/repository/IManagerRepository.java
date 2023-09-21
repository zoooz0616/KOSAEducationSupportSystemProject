package com.finalprj.kess.repository;

import java.sql.Date;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.finalprj.kess.dto.StudentInfoDTO;
import com.finalprj.kess.model.ClassVO;
import com.finalprj.kess.model.CommonCodeVO;
import com.finalprj.kess.model.FileVO;
import com.finalprj.kess.model.StudentVO;
import com.finalprj.kess.model.WorklogVO;

@Mapper
@Repository
public interface IManagerRepository {

//	public List<ClassVO> getClassListByMngrId(String mngrId);
	public List<ClassVO> getClassListByMngrId(@Param("mngrId") String mngrId, @Param("sortBy")String sortBy, @Param("order")String order);

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
	
	public int getCountByClssIdWlogCdStdtId (@Param("clssId") String clssId, @Param("wlogCd") String wlogCd, @Param("stdtId") String stdtId, @Param("startDate") String startDate, @Param("endDate") String endDate);

	public String getLatestClassIdByMngrId(String mngrId);

	public List<ClassVO> getFilteredClassListByMngrId(@Param("mngrId") String mngrId, @Param("filterString") List<String> filterString);
//	public List<ClassVO> getFilteredClassListByMngrId(@Param("mngrId") String mngrId, @Param("filterString") String[] filterString);

	public void updateStdtCmptCd(@Param("mngrId") String mngrId, @Param("stdtId") String stdtId, @Param("clssId") String clssId, @Param("targetCmptId") String targetCmptId);

	public double getTotalTmByClssId(String classId);

	public double getStudentTmSumByIds(@Param("classId") String classId, @Param("stdtId") String stdtId);

	public List<WorklogVO> getWlogListByClssIdDate(@Param("clssId") String clssId, @Param("startDate") String startDate, @Param("endDate") String endDate);

//	public List<StudentInfoDTO> getStudentListBySearch(@Param("clssId") String classId,@Param("startDate") String startDate,@Param("endDate") String endDate);
}
