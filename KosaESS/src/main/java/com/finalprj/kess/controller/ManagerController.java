package com.finalprj.kess.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.finalprj.kess.service.IManagerService;

import jakarta.servlet.http.HttpSession;

@Controller
public class ManagerController {
	@Autowired
	IManagerService managerService;

//	담당 교육 목록 조회
	@GetMapping("/manager/class")
	public String getClassList(Model model, HttpSession session) {
		session.setAttribute("role", "GRP0004003");//삭제 예정
		session.setAttribute("id", "MNG0000001");//삭제 예정
		model.addAttribute("classList", managerService.getClassList((String) session.getAttribute("id")));
		//session의 key-value쌍을 set 할 때 value는 object로 업캐스팅 된다. get 할 때 다운캐스팅 할 것 
		return "managerClassList";
	}

//	교육 상세 조회
	@GetMapping("/manager/class/{classId}")
	public String getClassDetail(Model model, @PathVariable String classId) {
		return "managerClassDetail";
	}

//	교육 과정별 교육생 조회
	@GetMapping("/manager/class/{classId}/student")
	public String getStudentList(Model model) {
		return "managerStudentList";
	}

//	교육생 상세 조회
	@GetMapping("/manager/class/{classId}/{studentId}")
	public String getStudentDetail(Model model) {
		return "managerStudentDetail";
	}

//	개인정보 조회
	@GetMapping("/manager/mypage")
	public String getMngrInfo(Model model) {
		return "managerMypage";
	}
}