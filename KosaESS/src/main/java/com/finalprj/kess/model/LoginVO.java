package com.finalprj.kess.model;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class LoginVO {
	private String userEmail;//이메일
	private String userPwd;//비밀번호
	private Timestamp LastLoginDt;//마지막 로그인 일시
	private String roleCd;//역할
	private String userCd;//계정상태
}
