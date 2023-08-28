package com.finalprj.kess.service;

import java.util.List;

import com.finalprj.kess.model.PostVO;

public interface IAdminService {
	int getWaitInquiryCnt();
	int getWaitClassCnt();
	int getCompleteClassCnt();
	List<PostVO> getPostVOList(String postValue);
}
