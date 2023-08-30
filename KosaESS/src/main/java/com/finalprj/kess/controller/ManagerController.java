package com.finalprj.kess.controller;

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
import org.springframework.web.bind.annotation.RequestParam;

import com.finalprj.kess.model.ClassVO;
import com.finalprj.kess.model.FileVO;
import com.finalprj.kess.model.StudentVO;
import com.finalprj.kess.service.IManagerService;

import jakarta.servlet.http.HttpSession;

@Controller
public class ManagerController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	IManagerService managerService;

//	메인 페이지
	@GetMapping("/manager/main")
	public String managerMain(Model model, HttpSession session) {
		session.setAttribute("role", "GRP0004003");// 삭제 예정
		session.setAttribute("id", "MNG0000005");// 삭제 예정
		return "manager_main";
	}

//	담당 교육 목록 조회
	@GetMapping("/manager/class")
	public String getClassList(Model model, HttpSession session) {
		model.addAttribute("title", "교육 과정 목록");
		session.setAttribute("role", "GRP0004003");// 삭제 예정
		session.setAttribute("id", "MNG0000005");// 삭제 예정
		List<ClassVO> classList = managerService.getClassList((String) session.getAttribute("id"));
		// session의 key-value쌍을 set 할 때 value는 object로 업캐스팅 된다. get 할 때 다운캐스팅 할 것
		for (ClassVO vo : classList) {
			vo.setRgstCnt(managerService.getRgstCount(vo.getClssId()));
		}
		model.addAttribute("classList", classList);
		List<String> classCodeNameList = managerService.getClassCodeNameList();
		model.addAttribute("classCodeNameList", classCodeNameList);
		return "manager_class_list";
	}

//	교육 상세 조회
	@GetMapping("/manager/class/{classId}")
	public String getClassDetail(Model model, @PathVariable String classId) {
		model.addAttribute("title", "교육 과정 상세");
		ClassVO thisClass = new ClassVO();
		thisClass = managerService.getClassDetail(classId);
		thisClass.setRgstCnt(managerService.getRgstCount(classId));
		List<String> fileIdList = managerService.getFileIdList(classId);
		model.addAttribute("clss", thisClass);
		model.addAttribute("fileIdList", fileIdList);
		return "manager_class_detail";
	}

	// 교육 상세 정보 > 파일 요청
	@GetMapping("/manager/fileId/{fileId}")
	public ResponseEntity<byte[]> getFile(@PathVariable String fileId) {
		FileVO file = managerService.getFile(fileId);
		final HttpHeaders headers = new HttpHeaders();
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
	}

//	교육생 목록 조회
	@GetMapping("/manager/student-list/{classId}")
	public String getStudentList(Model model, HttpSession session, @PathVariable String classId) {
		model.addAttribute("title", "교육생 목록");
		session.setAttribute("role", "GRP0004003");// 삭제 예정
		session.setAttribute("id", "MNG0000005");// 삭제 예정
		List<StudentVO> stdtList = managerService.getStudentList(classId);
		List<ClassVO> classNameList =managerService.getClassNameList((String) session.getAttribute("id"));
		List<String> classCodeNameList = managerService.getClassCodeNameList();
		model.addAttribute("classCodeNameList", classCodeNameList);
		model.addAttribute("classNameList", classNameList);
		model.addAttribute("stdtCnt", stdtList.size());
		model.addAttribute("stdtList", stdtList);
		model.addAttribute("thisClassName", (String) managerService.getClassName(classId));
		return "manager_student_list";
	}

//	교육생 상세 조회
	@GetMapping("/manager/student/{studentId}")
	public String getStudentDetail(Model model) {
		return "manager_student_detail";
	}

//	개인정보 조회
	@GetMapping("/manager/mypage")
	public String getMngrInfo(Model model) {
		return "manager_mypage";
	}
}