package com.finalprj.kess.service;

import java.util.List;

import com.finalprj.kess.model.FileVO;

public interface IUploadFileService {
	String getMaxFileId();
	void uploadFile(FileVO fileVO);
	
	//파일리스트 가져오기
	List<FileVO> getFileList(String fileId);
	
	//파일 한 개 가져오기
	FileVO getFile(String fileId, String fileSubId);
	
	void deleteFile(String fileId);
}
