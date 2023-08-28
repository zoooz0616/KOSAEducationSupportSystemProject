package com.finalprj.kess.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.finalprj.kess.model.PostVO;

@Repository
@Mapper
public interface IAdminRepository {
	int getWaitInquiryCnt();
	int getWaitClassCnt();
	int getCompleteClassCnt();
	List<PostVO> getPostVOList(String postValue);
}
