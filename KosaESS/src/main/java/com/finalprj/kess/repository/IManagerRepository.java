package com.finalprj.kess.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.finalprj.kess.model.ClassVO;
import com.finalprj.kess.model.FileVO;

@Mapper
@Repository
public interface IManagerRepository {

	public List<ClassVO> getClassList(String mngrId);
	public int getApplyCount(String clssId);
	public String getClassCodeName(String clssCd);
	public List<String> getClassCodeNameList();
	public ClassVO getClassDetail(String classId);
	public FileVO getFile(String classId);
	public List<String> getFileIdList(String classId);
}
