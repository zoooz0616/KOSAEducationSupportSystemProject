package com.finalprj.kess.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.finalprj.kess.dto.ClassDetailDTO;
import com.finalprj.kess.model.ApplyVO;
import com.finalprj.kess.model.ClassVO;
import com.finalprj.kess.model.FileVO;
import com.finalprj.kess.model.LoginVO;
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
	StudentVO student = null;
	LoginVO login = null;

	// 메인화면
	/**
	 * @author : dabin
	 * @date : 2023. 8. 24.
	 * @parameter : model
	 * @return :
	 */
	@GetMapping("")
	public String main(Model model) {
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
	public String login(String userEmail, String userPwd, HttpSession session, Model model) {
		logger.warn("stdtId : " + userEmail + "password : " + userPwd);
		login = studentService.selectUser(userEmail);
		if (student != null) {
			String dataPwd = login.getUserPwd();
			if (dataPwd == null) {
				// 아이디(이메일)이 없는 경우
				logger.warn("비밀번호를 입력하세요");
				model.addAttribute("message", "아이디나 비밀번호를 다시 확인해주세요.");
			} else {
				if (dataPwd.equals(userPwd)) {
					// 아이디(이메일)과 비밀번호가 일치하는 경우 세션에 정보 저장
					session.setAttribute("userEmail", userEmail);
					session.setAttribute("userPwd", login.getUserPwd());
					session.setAttribute("role_cd", login.getRole_cd());
					logger.warn("아이디 비밀번호 일치" + userEmail);
					logger.warn("userPwd" + login.getUserPwd());

					int aplyClassCnt = studentService.getAplyClass(userEmail);
					logger.warn("지원완료 : " + aplyClassCnt);
					int cmptClassCnt = studentService.getCmptClass(userEmail);
					logger.warn("이수완료 : " + cmptClassCnt);

					session.setAttribute("aplyClassCnt", aplyClassCnt);
					session.setAttribute("cmptClassCnt", cmptClassCnt);

					return "redirect:/student";
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
		return "redirect:/student";
	}

	// 로그이웃
	/**
	 * @author : dabin
	 * @date : 2023. 8. 25.
	 * @parameter : session, request
	 * @return :
	 */

	@GetMapping("/logout")
	public String logout(HttpSession session, HttpServletRequest request) {
		session.invalidate(); // 로그아웃
		student = null;
		return "redirect:/student";
	}

	// 공지사항 리스트확인
	/**
	 * @author : dabin
	 * @date : 2023. 8. 28.
	 * @parameter : model
	 * @return :
	 */

	@GetMapping("/notice")
	public String noticeList(Model model) {
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
	 * @parameter : model
	 * @return :
	 */
	@GetMapping("/inquiry")
	public String inquiryList(Model model) {
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
	 * @parameter :model
	 * @return :
	 */

	@GetMapping("/class")
	public String classList(Model model) {
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
	 * @parameter : model
	 * @return :
	 */

	@GetMapping("/class/{clssId}")
	public String classdetail(@PathVariable String clssId, Model model) {
		model.addAttribute("student", student);
		ClassDetailDTO classDetail = studentService.selectClass(clssId);
		model.addAttribute("classDetail", classDetail);
		List<ClassDetailDTO> classDetailList = new ArrayList<ClassDetailDTO>();
		classDetailList = studentService.selectAllClassFile(clssId);
		model.addAttribute("classDetailList", classDetailList);
		logger.warn("classDetailList" + classDetailList);
		logger.warn("student" + student);

		return "student_class_detail";
	}

	// 교욱 지원 모달창(로그인 유무 확인)
	/**
	 * @author : dabin
	 * @date : 2023. 8. 31.
	 * @parameter : session, model
	 * @return :
	 */

	@GetMapping("/class/apply")
	@ResponseBody
	public String classapply(HttpSession session, Model model) {
		String stdtId = (String) session.getAttribute("stdtId");
		logger.warn("stdtId" + stdtId);
		return stdtId;
	}

	// 교욱 이력서 다운로드
	/**
	 * @author : dabin
	 * @date : 2023. 8. 31.
	 * @parameter :model
	 * @return :
	 */

	@RequestMapping("/download/file/{fileId}")
	public ResponseEntity<byte[]> downloadFile(@PathVariable String fileId) {
		FileVO file = studentService.getFile(fileId);

		final HttpHeaders headers = new HttpHeaders();
		if (file != null) {
			String[] mtypes = file.getFileType().split("/");
			headers.setContentType(new MediaType(mtypes[0], mtypes[1]));
			headers.setContentLength(file.getFileSize());
			try {
				String encodedFileName = URLEncoder.encode(file.getFileNm(), "UTF-8");
				headers.setContentDispositionFormData("attachment", encodedFileName);
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException(e);
			}
			return new ResponseEntity<byte[]>(file.getFileContent(), headers, HttpStatus.OK);
		} else {
			return new ResponseEntity<byte[]>(HttpStatus.NOT_FOUND);
		}
	}

	// 교욱 이력서 제출
	/**
	 * @author : dabin
	 * @date : 2023. 8. 31.
	 * @parameter :model
	 * @return :
	 * @throws IOException 
	 */
	@PostMapping("/upload/{clssId}")
	public String uploadFile(@PathVariable String clssId, @RequestPart MultipartFile file, HttpSession session) throws IOException {
		ApplyVO apply = new ApplyVO();
		apply.setStdtId(session.getId());
		apply.setClssId(clssId);
		/*
		 * apply.setFileNm(file.getOriginalFilename());
		 * apply.setFileContent(file.getBytes()); apply.setFileSize(file.getSize());
		 * apply.setFileType(file.getContentType());
		 */
        
        studentService.uploadFile(apply);
		
		return "redirect:/student/class";

	}
}
