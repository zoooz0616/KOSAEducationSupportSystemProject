package com.finalprj.kess.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finalprj.kess.model.ClassVO;
import com.finalprj.kess.model.PostVO;
import com.finalprj.kess.model.ProfessorVO;
import com.finalprj.kess.repository.IAdminRepository;

@Service
public class AdminService implements IAdminService {

	@Autowired
	IAdminRepository adminRepository;
	
	@Override
	public int getWaitInquiryCnt() {
		return adminRepository.getWaitInquiryCnt();
	}

	@Override
	public int getWaitClassCnt() {
		return adminRepository.getWaitClassCnt();
	}

	@Override
	public int getCompleteClassCnt() {
		return adminRepository.getCompleteClassCnt();
	}

	@Override
	public List<PostVO> getPostVOList(String postValue) {
		return adminRepository.getPostVOList(postValue);
	}

	@Override
	public List<ProfessorVO> getProfessorVOList() {
		return adminRepository.getProfessorVOList();
	}

	@Override
	public List<ClassVO> getClassVOList() {
		return adminRepository.getClassVOList();
	}

}
