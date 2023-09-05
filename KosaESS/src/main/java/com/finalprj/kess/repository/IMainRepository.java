package com.finalprj.kess.repository;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.finalprj.kess.model.ManagerVO;

@Repository
@Mapper
public interface IMainRepository {
	String getRole(String email, String pwd);
	ManagerVO getManagerVO(String email);
}
