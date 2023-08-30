package com.finalprj.kess.repository;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.finalprj.kess.model.ClassVO;
import com.finalprj.kess.model.PostVO;
import com.finalprj.kess.model.ProfessorVO;

@Repository
@Mapper
public interface IAdminRepository {
	int getWaitInquiryCnt();
	int getWaitClassCnt();
	int getCompleteClassCnt();
	List<PostVO> getPostVOList(String postValue);
	List<ProfessorVO> getProfessorVOList();
	List<ClassVO> getClassVOList();
	List<String> getClassSearch(String term);
	List<ClassVO> getSearchClassVOList(
			@Param("className") String className,
            @Param("status") List<String> status, //접수예정, 접수중, 접수마감, 교육중, 교육완료, 취소, 폐강
            @Param("aplyStartDt") Date aplyStartDt,
            @Param("aplyEndDt") Date aplyEndDt,
            @Param("classStartDd") Date classStartDd,
            @Param("classEndDd") Date classEndDd);
}