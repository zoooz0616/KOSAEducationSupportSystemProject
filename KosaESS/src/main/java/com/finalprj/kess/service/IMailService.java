package com.finalprj.kess.service;

import java.util.List;

public interface IMailService {
	public void sendMail(String recipient, String clssNm) throws Exception;
	public void sendCode(String email, String code) throws Exception;
	
}
