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
import org.springframework.web.bind.annotation.RequestMapping;

import com.finalprj.kess.model.ClassVO;
import com.finalprj.kess.model.PostVO;
import com.finalprj.kess.model.StudentVO;
import com.finalprj.kess.service.IStudentService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/student")
public class StudentController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	IStudentService studentService;
	StudentVO student = new StudentVO();

	// 메인화면
	/**
	 * @author : dabin
	 * @date : 2023. 8. 24.
	 * @parameter : model
	 * @return :
	 */
	@GetMapping("/")
	public String main(Model model, HttpSession session) {
		model.addAttribute("student", student);

		List<PostVO> postList = new ArrayList<PostVO>();
		postList = studentService.selectNoticeMain();
		model.addAttribute("postList", postList);
		logger.warn("postList" + postList);

		List<ClassVO> classList = new ArrayList<ClassVO>();
		classList = studentService.selectClassMain();
		model.addAttribute("classList", classList);
		logger.warn("classList" + classList);

		return "student_main";
	}

	// 로그인
	/**
	 * @author : dabin
	 * @date : 2023. 8. 24.
	 * @parameter :
	 * @return :
	 */

	@GetMapping("/login")
	public String login() {
		return "student_login";
	}

	// 로그인 + 로그인 후 화면 변경
	/**
	 * @author : dabin
	 * @date : 2023. 8. 24.
	 * @parameter : session, model
	 * @return :
	 */

	@PostMapping("/login")
	public String login(String stdtEmail, String stdtPwd, HttpSession session, Model model) {
		logger.warn("stdtId : " + stdtEmail + "password : " + stdtPwd);
		student = studentService.selectStudent(stdtEmail);
		if (student != null) {
			String dataPwd = student.getStdtPwd();
			if (dataPwd == null) {
				// 아이디(이메일)이 없는 경우
				logger.warn("비밀번호를 입력하세요");

				model.addAttribute("message", "아이디나 비밀번호를 다시 확인해주세요.");
			} else {
				if (dataPwd.equals(stdtPwd)) {
					// 아이디(이메일)과 비밀번호가 일치하는 경우 세션에 정보 저장
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

					int aplyClassCnt = studentService.getAplyClass(stdtEmail);
					logger.warn("지원완료 : " + aplyClassCnt);
					int cmptClassCnt = studentService.getCmptClass(stdtEmail);
					logger.warn("이수완료 : " + cmptClassCnt);

					session.setAttribute("aplyClassCnt", aplyClassCnt);
					session.setAttribute("cmptClassCnt", cmptClassCnt);

					return "redirect:/";
				} else {
					// 비밀번호가 일치하지 않는 경우
					logger.warn("비밀번호 불일치");
					model.addAttribute("message", "아이디나 비밀번호를 다시 확인해주세요.");
				}
			}
		} else {
			logger.warn("아이디 없음");
			model.addAttribute("message", "존재하지않는 아이디입니다.");
		}
		session.invalidate();
		return "redirect:/";
	}

	// 로그이웃
	/**
	 * @author : dabin
	 * @date : 2023. 8. 25.
	 * @parameter : session
	 * @return :
	 */

	@GetMapping("/logout")
	public String logout(HttpSession session, HttpServletRequest request) {
		session.invalidate();
		return "redirect:/";
	}

	// 공지사항 리스트확인
	/**
	 * @author : dabin
	 * @date : 2023. 8. 28.
	 * @parameter : session, model
	 * @return :
	 */

	@GetMapping("/notice")
	public String noticeList(HttpSession session, Model model) {
		model.addAttribute("student", student);

		List<PostVO> postList = new ArrayList<PostVO>();
		postList = studentService.selectAllNotice();
		model.addAttribute("postList", postList);
		logger.warn("postList" + postList);

		return "student_notice_list";
	}

	// 문의사항 리스트확인
	/**
	 * @author : dabin
	 * @date : 2023. 8. 28.
	 * @parameter : session, model
	 * @return :
	 */
	@GetMapping("/inquiry")
	public String inquiryList(HttpSession session, Model model) {
		model.addAttribute("student", student);

		List<PostVO> postList = new ArrayList<PostVO>();
		postList = studentService.selectAllInquiry();
		model.addAttribute("postList", postList);
		logger.warn("postList" + postList);

		return "student_inquiry_list";
	}

	// 교육 리스트확인
	/**
	 * @author : dabin
	 * @date : 2023. 8. 28.
	 * @parameter : session, model
	 * @return :
	 */

	@GetMapping("/class")
	public String classList(HttpSession session, Model model) {
		model.addAttribute("student", student);

		List<ClassVO> classList = new ArrayList<ClassVO>();
		classList = studentService.selectAllClass();
		model.addAttribute("classList", classList);
		logger.warn("classList" + classList);

		return "student_class_list";
	}

	// 교욱 상세페이지
	/**
	 * @author : dabin
	 * @date : 2023. 8. 29.
	 * @parameter : session, model
	 * @return :
	 */
	@GetMapping("/class/{clssId}")
	public String classdetail(HttpSession session, Model model) {
		model.addAttribute("student", student);

		List<PostVO> postList = new ArrayList<PostVO>();
		postList = studentService.selectAllInquiry();
		model.addAttribute("postList", postList);
		logger.warn("postList" + postList);

		return "student_class_detail";
	}
}
