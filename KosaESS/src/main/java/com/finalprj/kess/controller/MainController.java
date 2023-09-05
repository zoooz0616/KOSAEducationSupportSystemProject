package com.finalprj.kess.controller;

import java.net.http.HttpRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.finalprj.kess.model.ManagerVO;
import com.finalprj.kess.service.IMainService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class MainController {
	
	@Autowired
	IMainService mainService;

	@GetMapping("/login")
	public String login() {
		return "login";
	}
	
	
	/**
	 * @author : eunji
	 * @date : 2023. 9. 4.
	 * @parameter : session, request
	 * @return : string
	 */
	@PostMapping("/login")
	public String login(HttpSession session, HttpServletRequest request) {
		//form에서 이메일, 비밀번호 가져옴
		String email = request.getParameter("email");
		String pwd = request.getParameter("pwd");
		
		//login 테이블에 회원이 있는지 확인하기
		String role = mainService.getRole(email,pwd);
		if (role.equals("ROL0000001")) {
			//학생
			session.setAttribute("email", email);
			
			return "redirect:/student";
		} else if (role.equals("ROL0000002")) {
			//관리자
			ManagerVO managerVO = mainService.getManagerVO(email);
			session.setAttribute("mngrId", managerVO.getMngrId());
			session.setAttribute("userEmail", managerVO.getUserEmail());
			session.setAttribute("userPwd", managerVO.getUserPwd());
			session.setAttribute("mngrNm", managerVO.getMngrNm());
			session.setAttribute("mngrId", managerVO.getMngrId());
			session.setAttribute("roleCd", managerVO.getRoleCd());
			session.setAttribute("lastLoginDt", managerVO.getLastLoginDt());
			
			session.setAttribute("role", "admin");
			
			mainService.updateLastLoginDt(managerVO.getUserEmail());
			
			return "redirect:/admin";
		} else if (role.equals("ROL0000003")) {
			//업무담당자
			session.setAttribute("email", email);
			
			return "redirect:/manager";
		} else {
			return "redirect:/login";
		}
	}
}
