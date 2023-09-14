package com.finalprj.kess.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.finalprj.kess.model.FileVO;
import com.finalprj.kess.model.ManagerVO;
import com.finalprj.kess.model.StudentVO;
import com.finalprj.kess.service.IMainService;
import com.finalprj.kess.service.IStudentService;
import com.finalprj.kess.service.IUploadFileService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class MainController {

	@Autowired
	IMainService mainService;
	IStudentService studentService;
	
	@Autowired
	IUploadFileService uploadFileService;

	/**
	 * @updater : seungwoo
	 * @update : 2023. 9. 8.
	 * @parameter : session
	 * @return : string(rold code 기준으로 login 시도 또는 메인창으로 이동)
	 */
	@GetMapping("/login")
	public String login(HttpSession session) {
		String roleCd = (String) session.getAttribute("roleCd");
		if (roleCd == null) {
			return "login";
		} else {
			if (roleCd.equals("ROL0000001")) {
				return "redirect:/student";
			} else if (roleCd.equals("ROL0000002")) {
				return "redirect:/admin";
			} else if (roleCd.equals("ROL0000003")) {
				return "redirect:/manager";
			} else {
				return "redirect:/student";//추후 수정 요망
			}
		}
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
		String role = mainService.getRole(email, pwd);
		if (role.equals("ROL0000001")) {
			//학생
			StudentVO studentVO = mainService.getStudentVO(email);
			session.setAttribute("stdtId", studentVO.getStdtId());
			session.setAttribute("userEmail", studentVO.getUserEmail());
			session.setAttribute("userPwd", studentVO.getUserPwd());
			session.setAttribute("stdtNm", studentVO.getStdtNm());
			session.setAttribute("stdtId", studentVO.getStdtId());
			session.setAttribute("roleCd", studentVO.getRoleCd());
			session.setAttribute("lastLoginDt", studentVO.getLastLoginDt());

			mainService.updateLastLoginDt(studentVO.getUserEmail());

			return "redirect:/student";
		} else if (role.equals("ROL0000002")) {
			//관리자
			ManagerVO managerVO = mainService.getManagerVO(email);
			session.setAttribute("mngrId", managerVO.getMngrId());
			session.setAttribute("userEmail", managerVO.getUserEmail());
			session.setAttribute("userPwd", managerVO.getUserPwd());
			session.setAttribute("mngrNm", managerVO.getMngrNm());
			session.setAttribute("roleCd", managerVO.getRoleCd());
			session.setAttribute("lastLoginDt", managerVO.getLastLoginDt());

			session.setAttribute("role", "admin");

			mainService.updateLastLoginDt(managerVO.getUserEmail());

			return "redirect:/admin";
		} else if (role.equals("ROL0000003")) {
			//업무담당자
			ManagerVO managerVO = mainService.getManagerVO(email);
			session.setAttribute("mngrId", managerVO.getMngrId());
			session.setAttribute("userEmail", managerVO.getUserEmail());
			session.setAttribute("mngrNm", managerVO.getMngrNm());
			session.setAttribute("roleCd", managerVO.getRoleCd());
			session.setAttribute("lastLoginDt", managerVO.getLastLoginDt());
			session.setAttribute("role", "manager");
			mainService.updateLastLoginDt(managerVO.getUserEmail());
			return "redirect:/manager";
		} else {
			return "redirect:/login";
		}
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
		return "redirect:/student";
	}
	
	@RequestMapping("/download/file/{fileId}/{fileSubId}")
	public ResponseEntity<byte[]> downloadFile(@PathVariable String fileId, @PathVariable String fileSubId) {
		FileVO file = uploadFileService.getFile(fileId, fileSubId);

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
}
