package com.finalprj.kess.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.finalprj.kess.model.FileVO;

@Repository
@Mapper
public interface IUploadFileRepository {
	String getMaxFileId();
	void uploadFile(FileVO fileVO);
	
	//파일리스트 가져오기
	List<FileVO> getFileList(String fileId);
	
	//파일 한 개 가져오기
	FileVO getFile(String fileId, int fileSubId);
	
	void deleteFile(String fileId);
}
