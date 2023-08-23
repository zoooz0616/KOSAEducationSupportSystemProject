package com.finalprj.kess.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.finalprj.kess.model.ClassVO;

@Mapper
@Repository
public interface IManagerRepository {

	public List<ClassVO> getClassList(String mngrId);
}
