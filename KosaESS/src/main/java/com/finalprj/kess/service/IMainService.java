package com.finalprj.kess.service;

import com.finalprj.kess.model.ManagerVO;

public interface IMainService {
	String getRole(String email, String pwd);
	ManagerVO getManagerVO(String email);
	void updateLastLoginDt(String userEmail);
}
