package com.finalprj.kess.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.finalprj.kess.service.IManagerService;

@Controller
public class ManagerController {
	@Autowired
	IManagerService managerService;

//	담당 교육 목록 조회
	@GetMapping("/manager/class")
	public String getClassList(Model model) {
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