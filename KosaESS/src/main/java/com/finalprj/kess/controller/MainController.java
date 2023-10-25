package com.finalprj.kess.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.sql.Date;
import java.util.List;
import java.util.Random;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.finalprj.kess.model.CommonCodeVO;
import com.finalprj.kess.model.FileVO;
import com.finalprj.kess.model.LoginVO;
import com.finalprj.kess.model.ManagerVO;
import com.finalprj.kess.model.StudentVO;
import com.finalprj.kess.service.IAdminService;
import com.finalprj.kess.service.IMailService;
import com.finalprj.kess.service.IMainService;
import com.finalprj.kess.service.IStudentService;
import com.finalprj.kess.service.IUploadFileService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class MainController {

	@Autowired
	IMainService mainService;

	@Autowired
	IStudentService studentService;

	@Autowired
	IAdminService adminService;

	@Autowired
	IMailService mailService;

	@Autowired
	IUploadFileService uploadFileService;

	@GetMapping("/")
	public String main(){
		return "redirect:/student";
	}
	
	
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
				return "redirect:/login_error";// 추후 수정 요망
			}
		}
	}

	/**
	 * @author : eunji
	 * @return
	 * @date : 2023. 9. 4.
	 * @parameter : session, request
	 * @return : string
	 */
	@PostMapping("/login")
	public String login(HttpSession session, HttpServletRequest request) {
		// form에서 이메일, 비밀번호 가져옴
		String email = request.getParameter("email");
		String pwd = request.getParameter("pwd");

		// login 테이블에 회원이 있는지 확인하기
		String memberYN = mainService.getMember(email);
		if (memberYN.equals("1")) {
			LoginVO role = mainService.getRole(email, pwd);
			System.out.println(role);
			String roleCd = role.getRoleCd();
			System.out.println(roleCd);

			if (roleCd.equals("ROL0000001")) {
				// 학생
				StudentVO studentVO = mainService.getStudentVO(email);
				session.setAttribute("stdtId", studentVO.getStdtId());
				session.setAttribute("userEmail", studentVO.getUserEmail());
				session.setAttribute("userPwd", studentVO.getUserPwd());
				session.setAttribute("stdtNm", studentVO.getStdtNm());
				session.setAttribute("roleCd", studentVO.getRoleCd());
				session.setAttribute("lastLoginDt", studentVO.getLastLoginDt());

				mainService.updateLastLoginDt(studentVO.getUserEmail());

				return "redirect:/student";
			} else if (roleCd.equals("ROL0000002")) {
				// 관리자
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
			} else if (roleCd.equals("ROL0000003")) {
				// 업무담당자
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
				return "redirect:/login_error";
			}
		} else {
			return "redirect:/login_error";
		}
	}

	@GetMapping("/login_error")
	public String loginError() {
		return "login_error";
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

	// 파일다운로드
	/**
	 * @author : dabin
	 * @date : 2023. 8. 25.
	 * @parameter : fileId, fileSubId
	 * @return :
	 */
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

	// 회원가입 화면
	/**
	 * @author : eunji
	 * @date : 2023. 10. 1.
	 * @updater : dabin
	 * @update : 2023. 10. 15.
	 * @parameter : model
	 * @return :
	 */
	@GetMapping("/student/join")
	public String join(Model model) {
		// 성별 리스트
		List<CommonCodeVO> genderList = studentService.getCommonCodeList("GRP0000006");
		model.addAttribute("genderList", genderList);

		// 직업 리스트
		List<CommonCodeVO> jobList = studentService.getCommonCodeList("GRP0000007");
		model.addAttribute("jobList", jobList);
		return "signup";
	}

	// 회원가입
	/**
	 * @author : eunji
	 * @date : 2023. 10. 1.
	 * @parameter : model
	 * @return :
	 */
	@PostMapping("/student/join")
	public String signup(StudentVO student) {
		String maxStdtId = mainService.getMaxStdtId();
		student.setStdtId(maxStdtId);
		mainService.insertStudent(student);

		return "redirect:/student";
	}

	// 이메일 중복확인
	/**
	 * @author : eunji
	 * @date : 2023. 10. 2.
	 * @parameter : email
	 * @return :
	 */
	@GetMapping("/join/confirm/{email}")
	@ResponseBody
	public String confirmEmail(@PathVariable String email) {
		int emailCnt = mainService.getEmailCnt(email);
		if (emailCnt == 0) {
			return "success";
		} else {
			return "fail";
		}
	}

	// 이메일 인증코드 발송
	/**
	 * @author : eunji
	 * @date : 2023. 10. 2.
	 * @parameter : email
	 * @return :
	 */
	@PostMapping("/join/send/{email}")
	@ResponseBody
	public String sendEmail(@PathVariable String email) throws Exception {
		// 랜덤코드 생성
		String code = "";
		// Random 객체 생성
		Random random = new Random();
		// 랜덤 코드를 저장할 StringBuilder 객체 생성
		StringBuilder randomCode = new StringBuilder();

		// 6자리 숫자 랜덤 코드 생성
		for (int i = 0; i < 6; i++) {
			// 0부터 9까지의 랜덤 숫자를 생성하고 문자열로 변환하여 StringBuilder에 추가
			int randomNumber = random.nextInt(10);
			randomCode.append(Integer.toString(randomNumber));
		}

		// 생성된 랜덤 코드 출력
		code = randomCode.toString();
		// 인증번호 전송
		mailService.sendCode(email, code);

		return code;
	}

	// 아이디/비밀번호 찾기
	/**
	 * @author : dabin
	 * @date : 2023. 10. 21.
	 * @parameter : model
	 * @return :
	 */
	@GetMapping("/student/find")
	public String find(Model model) {
		return "find_id_pwd";
	}

	@PostMapping("/findUserId")
	@ResponseBody
	public String findUserId(Model model, @RequestParam("name") String name, @RequestParam("phone") String phone) {
		String userId = mainService.getMemberId(name, phone);
		if (userId != null && !userId.isEmpty()) {
			String MemberYN = mainService.checkMemberCd(userId);
			if (MemberYN != null && !MemberYN.isEmpty()) {
				return MemberYN;
			}
		}
		return "no";
	}

	@PostMapping("/findUserPwd")
	@ResponseBody
	public String findUserPwd(Model model, @RequestParam("name") String name, @RequestParam("email") String email,
			@RequestParam("phone") String phone) throws Exception {
		String userEmail = mainService.getMemberPwd(name, email, phone);

		if (userEmail != null && !userEmail.isEmpty()) {
			String randomCode = generateRandomCode(10); // 변경 가능한 길이

			mailService.sendCode(email, randomCode);
			mainService.changePwd(email, randomCode);
			return userEmail;
		}

		return "no";
	}

	public static String generateRandomCode(int length) {
		if (length < 8 || length > 20) {
			throw new IllegalArgumentException("Length should be between 8 and 20 characters.");
		}

		String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_+";

		SecureRandom random = new SecureRandom();
		StringBuilder code = new StringBuilder();

		for (int i = 0; i < length; i++) {
			int randomIndex = random.nextInt(characters.length());
			code.append(characters.charAt(randomIndex));
		}

		return code.toString();
	}
}