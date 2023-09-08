package com.finalprj.kess.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.finalprj.kess.dto.ClassDetailDTO;
import com.finalprj.kess.model.ApplyVO;
import com.finalprj.kess.model.ClassVO;
import com.finalprj.kess.model.FileVO;
import com.finalprj.kess.model.LoginVO;
import com.finalprj.kess.model.PostVO;
import com.finalprj.kess.model.StudentVO;
import com.finalprj.kess.service.IStudentService;
import com.finalprj.kess.service.IUploadFileService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/student")
public class StudentController {

	@Autowired
	IStudentService studentService;
	IUploadFileService uploadFileService;
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
	public String main(Model model, HttpSession session) {
		model.addAttribute("student", student);

		List<PostVO> postList = new ArrayList<PostVO>();
		postList = studentService.selectNoticeMain();
		model.addAttribute("postList", postList);

		List<ClassVO> classList = new ArrayList<ClassVO>();
		classList = studentService.selectClassMain();
		model.addAttribute("classList", classList);

		int aplyClassCnt = studentService.getAplyClass((String) session.getAttribute("userEmail"));
		int cmptClassCnt = studentService.getCmptClass((String) session.getAttribute("userEmail"));
		String ingClassNm = studentService.getIngClass((String) session.getAttribute("userEmail"));
		
		model.addAttribute("aplyClassCnt", aplyClassCnt);
		model.addAttribute("cmptClassCnt", cmptClassCnt);
		model.addAttribute("ingClassNm", ingClassNm);

		return "student/main";
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

		return "student/notice_list";
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

		return "student/inquiry_list";
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

		return "student/class_list";
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
		ClassVO classDetail = studentService.selectClass(clssId);
		model.addAttribute("classDetail", classDetail);
		List<ClassDetailDTO> classDetailList = new ArrayList<ClassDetailDTO>();
		classDetailList = studentService.selectAllClassFile(clssId);
		model.addAttribute("classDetailList", classDetailList);

		return "student/class_detail";
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
		return stdtId;
	}

	// 교욱 이력서 다운로드
	/**
	 * @author : dabin
	 * @date : 2023. 8. 31.
	 * @parameter :model
	 * @return :
	 */

	@RequestMapping("/download/file/{fileId}/{fileSubId}")
	public ResponseEntity<byte[]> downloadFile(@PathVariable String fileId,@PathVariable String fileSubId) {
		FileVO file = studentService.getFile(fileId,fileSubId);

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
	public String uploadFile(@PathVariable String clssId, @RequestPart MultipartFile file, HttpSession session)
			throws IOException {
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
	
	@PostMapping("/upload/{classId}")
	public String insertClass(HttpSession session,
			@RequestParam("files") MultipartFile[] files, RedirectAttributes redirectAttrs) {
		//파일 업로드하기
		String maxFileId = uploadFileService.getMaxFileId();
		int subFileId=1;
		try {
			for(MultipartFile file: files) {
				if(file!=null && !file.isEmpty()) {
					FileVO fileVO = new FileVO();
					fileVO.setFileId(maxFileId);
					fileVO.setFileSubId(subFileId);
					fileVO.setFileNm(file.getOriginalFilename());
					fileVO.setFileSize(file.getSize());
					fileVO.setFileType(file.getContentType());
					fileVO.setFileContent(file.getBytes());
					uploadFileService.uploadFile(fileVO);
					subFileId++;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			redirectAttrs.addFlashAttribute("message", e.getMessage());
		}

		return "redirect:/student/clss_list";
	}
	
	// 교욱 이력서 제출
		/**
		 * @author : dabin
		 * @date : 2023. 8. 31.
		 * @parameter :model
		 * @return :
		 * @throws IOException
		 */
	@GetMapping("/mypage")
	public String mypageMain(Model model) {
		model.addAttribute("student", student);

		

		return "student/mypage";
	}

}
