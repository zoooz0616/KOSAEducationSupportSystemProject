package com.finalprj.kess.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.finalprj.kess.model.FileVO;
import com.finalprj.kess.repository.IFileRepository;

@Service
public class FileService implements IFileService{
	
	@Autowired
	IFileRepository fileRepository;
	
	@Transactional
	public void saveFiles(List<FileVO> files) {
		if (CollectionUtils.isEmpty(files)) {
			return;
		}
		fileRepository.saveAll(files);
	}
}
