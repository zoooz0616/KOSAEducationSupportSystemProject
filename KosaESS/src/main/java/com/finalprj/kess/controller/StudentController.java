package com.finalprj.kess.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.finalprj.kess.model.ClassVO;
import com.finalprj.kess.model.PostVO;
import com.finalprj.kess.model.StudentVO;
import com.finalprj.kess.service.IStudentService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class StudentController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	
	@Autowired
	IStudentService studentService;
	StudentVO student = new StudentVO();
	
	
	/**
	 * @author : dabin
	 * @date : 2023. 8. 24.
	 * @parameter : model
	 * @return : 
	 */
	@GetMapping("/") 
	public String main(Model model, String stdtId, HttpSession session) {
		model.addAttribute("student", student);
		
		List<PostVO> postList = new ArrayList<PostVO>();
		postList = studentService.selectAllNotice();
		model.addAttribute("postList", postList);
		logger.warn("postList" + postList);
		
		List<ClassVO> classList = new ArrayList<ClassVO>();
		classList = studentService.selectAllClass();
		model.addAttribute("classList", classList);
		logger.warn("classList" + classList);
		
		return "main_student";
	}

	
	/**
	 * @author : dabin
	 * @date : 2023. 8. 24.
	 * @parameter :
	 * @return : 
	 */
	
	@GetMapping(value="/login") 
	public String login() {
		return "login";
	}
	
	/**
	 * @author : dabin
	 * @date : 2023. 8. 24.
	 * @parameter : session, model
	 * @return : 
	 */
	
	@PostMapping(value="/login")
	public String login(String stdtEmail, String stdtPwd, HttpSession session, Model model) {
		logger.warn("stdtId : " + stdtEmail + "password : " + stdtPwd );
		student = studentService.selectStudent(stdtEmail);
		if(student != null) {
			String dataPwd = student.getStdtPwd();
				if(dataPwd== null) {
				//아이디(이메일)이 없는 경우
					logger.warn("비밀번호를 입력하세요");
	
				model.addAttribute("message", "아이디나 비밀번호를 다시 확인해주세요.");
				}else {
				if(dataPwd.equals(stdtPwd)) {
					//아이디(이메일)과 비밀번호가 일치하는 경우 세션에 정보 저장
					session.setAttribute("stdtEmail", stdtEmail);
					session.setAttribute("stdtId", student.getStdtId());
					session.setAttribute("stdtPwd", student.getStdtPwd());
					session.setAttribute("stdtNm", student.getStdtNm());
					session.setAttribute("stdtGender", student.getGenderCd());
					session.setAttribute("stdtBirth", student.getBirthDd());
					session.setAttribute("stdtTel", student.getStdtTel());
					session.setAttribute("stdtJod", student.getJobCd());
					session.setAttribute("stdtRgstDt", student.getRgstDt());
					session.setAttribute("stdtRole", student.getRoleCd());
					logger.warn("아이디 비밀번호 일치" + stdtEmail);
					logger.warn("Id" + student.getStdtId());
					
					int cmptClassCnt = studentService.getCmptClass(stdtEmail);
					logger.warn("이수완료 : " +cmptClassCnt);
					model.addAttribute("cmptClassCnt",cmptClassCnt);
					return "redirect:/";
				}else {
					//비밀번호가 일치하지 않는 경우
					logger.warn("비밀번호 불일치");
					model.addAttribute("message", "아이디나 비밀번호를 다시 확인해주세요.");
				}
			}
		}else {
			logger.warn("아이디 없음");
			model.addAttribute("message", "존재하지않는 아이디입니다.");
		}
		session.invalidate();	
		return "redirect:/";
	}
	
	/**
	 * @author : dabin
	 * @date : 2023. 8. 25.
	 * @parameter : session
	 * @return : 
	 */
	
	@GetMapping(value="/logout") 
	public String logout(HttpSession session, HttpServletRequest request) {
		session.invalidate();
		return "redirect:/";
	}
	

}
