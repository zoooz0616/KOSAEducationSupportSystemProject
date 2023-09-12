package com.finalprj.kess.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finalprj.kess.model.FileVO;
import com.finalprj.kess.repository.IUploadFileRepository;

@Service
public class UploadFileService implements IUploadFileService {

	@Autowired
	IUploadFileRepository uploadFileRepository;
	
	@Override
	public String getMaxFileId() {
		return uploadFileRepository.getMaxFileId();
	}

	@Override
	public void uploadFile(FileVO fileVO) {
		uploadFileRepository.uploadFile(fileVO);
	}

	@Override
	public List<FileVO> getFileList(String fileId) {
		return uploadFileRepository.getFileList(fileId);
	}

	@Override
	public FileVO getFile(String fileId, String fileSubId) {
		return uploadFileRepository.getFile(fileId, fileSubId);
	}

	@Override
	public void deleteFile(String fileId) {
		uploadFileRepository.deleteFile(fileId);
	}

}
