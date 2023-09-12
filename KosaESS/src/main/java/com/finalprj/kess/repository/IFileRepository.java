package com.finalprj.kess.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.finalprj.kess.model.FileVO;

@Repository
@Mapper
public interface IFileRepository {
	void saveAll(List<FileVO> files);
}
