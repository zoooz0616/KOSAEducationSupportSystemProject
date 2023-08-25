package com.finalprj.kess.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finalprj.kess.model.ClassVO;
import com.finalprj.kess.repository.IManagerRepository;

@Service
public class ManagerService implements IManagerService {
	@Autowired
	IManagerRepository managerRepository;

	@Override
	public List<ClassVO> getClassList(String mngrId) {
		return managerRepository.getClassList(mngrId);
	}

	@Override
	public int getApplyCount(String clssId) {
		return managerRepository.getApplyCount(clssId);
	}

	@Override
	public String getClassCodeName(String clssCd) {
		return managerRepository.getClassCodeName(clssCd);
	}

	@Override
	public List<String> getClassCodeNameList() {
		return managerRepository.getClassCodeNameList();
	}
}