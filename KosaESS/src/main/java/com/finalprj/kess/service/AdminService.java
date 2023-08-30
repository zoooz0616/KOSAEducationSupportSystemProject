package com.finalprj.kess.service;

import java.sql.Date;
import java.util.List;
import java.util.Map;

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

	@Override
	public List<String> getClassSearch(String term) {
		return adminRepository.getClassSearch(term);
	}

	@Override
	public List<ClassVO> getSearchClassVOList(String className, List<String> status, Date aplyStartDt,
			Date aplyEndDt, Date classStartDd, Date classEndDd) {
		return adminRepository.getSearchClassVOList(className, status, aplyStartDt,
				aplyEndDt, classStartDd, classEndDd);
	}

	
}
