package com.finalprj.kess.repository;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface IAdminRepository {
	int getWaitInquiryCnt();
	int getWaitClassCnt();
	int getCompleteClassCnt();
}
