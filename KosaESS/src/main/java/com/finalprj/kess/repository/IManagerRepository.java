package com.finalprj.kess.repository;

import java.sql.Date;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import com.finalprj.kess.dto.ReasonDTO;
import com.finalprj.kess.dto.StudentInfoDTO;
import com.finalprj.kess.dto.WorklogDTO;
import com.finalprj.kess.model.ClassVO;
import com.finalprj.kess.model.CommonCodeVO;
import com.finalprj.kess.model.FileVO;
import com.finalprj.kess.model.ManagerVO;
import com.finalprj.kess.model.ReasonVO;
import com.finalprj.kess.model.StudentVO;
import com.finalprj.kess.model.SubsidyVO;
import com.finalprj.kess.dto.SubsidyDTO;
import com.finalprj.kess.model.WorklogVO;

@Mapper
@Repository
public interface IManagerRepository {

	public List<ClassVO> getClassListByMngrId(@Param("mngrId") String mngrId, @Param("year")Integer year);

	public int getApplyCountByClssId(String clssId);

	public List<CommonCodeVO> getCodeNameListByKeyword(String keyword);

	public ClassVO getClassDetailByClssId(String clssId);

	public List<StudentInfoDTO> getStudentListByClssId(@Param("clssId") String clssId, @Param("keyword") String keyword, @Param("cmptList") List<String> cmptList);

	public String getClassNameByClssId(String clssId);

	public int getRgstCountByClssId(String clssId);

	public List<Integer> getFileSubIdListByFileId(String fileId);

	public FileVO getFileByIds(@Param("fileId") String fileId, @Param("fileSubId") int fileSubId);

	public FileVO getFileInfoByIds(@Param("fileId") String fileId, @Param("fileSubId") int fileSubId);

	public int getCountByClssIdWlogCdStdtId (@Param("clssId") String clssId, @Param("wlogCd") String wlogCd, @Param("stdtId") String stdtId, @Param("startDate") String startDate, @Param("endDate") String endDate);

	public String getLatestClassIdByMngrId(String mngrId);

	public List<ClassVO> getFilteredClassListByMngrId(@Param("mngrId") String mngrId, @Param("filterString") List<String> filterString, @Param("searchKeyword") String searchKeyword, @Param("year") Integer year);

	public void updateStdtCmptCd(@Param("mngrId") String mngrId, @Param("stdtId") String stdtId, @Param("clssId") String clssId, @Param("targetCmptId") String targetCmptId);

	public double getTotalTmByClssId(String classId);

	public double getStudentTmSumByIds(@Param("classId") String classId, @Param("stdtId") String stdtId);

	public List<WorklogDTO> getWlogListByClssIds(@Param("mngrId") String mngrId, @Param("clssId") String clssId, @Param("startDate") String startDate, @Param("endDate") String endDate, @Param("keyword") String keyword, @Param("isDelete") String isDelete, @Param("resnOnly") String resnOnly, @Param("filterString") List<String> filterString);

	public List<StudentInfoDTO> getStudentListByOnlyClssId(String classId);

	public ReasonDTO getResnDetailByResnId(String resnId);

	public void updateResnCd(@Param("resnId") String resnId, @Param("resnCd") String resnCd, @Param("mngrId") String mngrId);

	public void updateWlogCd(@Param("wlogId") String wlogId, @Param("wlogCd") String wlogCd, @Param("mngrId") String mngrId);

	public void updateManagerInfo(ManagerVO updateManager);

	public void updateManagerLoginInfo(ManagerVO updateManager);

	public List<SubsidyDTO> getSubsidyList(@Param("mngrId") String mngrId, @Param("clssId") String clssId, @Param("startDate") String startDate, @Param("endDate") String endDate, @Param("keyword") String keyword, @Param("filterString") List<String> filterString);

	public void insertSubsidy(SubsidyVO subsidyVO);

	public void deleteWlog(String wlogId);

	public ClassVO getCodeVO(String resnCd);

	public List<Integer> getYearList(String mngrId);

	public int getMaxId(@Param("tableName") String tableName, @Param("columnName") String columnName);
}