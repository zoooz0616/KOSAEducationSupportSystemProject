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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.finalprj.kess.dto.ApplyDetailDTO;
import com.finalprj.kess.dto.CurriculumDetailDTO;
import com.finalprj.kess.model.ApplyVO;
import com.finalprj.kess.model.ClassVO;
import com.finalprj.kess.model.CurriculumVO;
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
	@Autowired
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
	public String classdetail(@PathVariable String clssId, Model model, HttpSession session) {
		// 학생 정보를 세션에서 가져와서 student 객체에 저장
		String student = (String) session.getAttribute("student");
		model.addAttribute("student", student);

		// classDetail 객체를 가져와서 모델에 추가
		ClassVO classDetail = studentService.selectClass(clssId);
		model.addAttribute("classDetail", classDetail);

		List<CurriculumDetailDTO> curriculumlist = studentService.getCurriculumList(clssId);
		model.addAttribute("curriculumlist", curriculumlist);

		// classDetailList 객체를 가져와서 모델에 추가
		List<ClassVO> classDetailList = studentService.selectAllClassFile(clssId);
		model.addAttribute("classDetailList", classDetailList);

		// viewClass1과 viewClass2, viewClass3 세션에서 가져와서 모델에 추가
		String viewClass1 = (String) session.getAttribute("viewClass1");
		String viewClass2 = (String) session.getAttribute("viewClass2");
		String viewClass3 = (String) session.getAttribute("viewClass3");

		// viewClass3가 null인 경우 처리
		if (viewClass3 == null) {
			viewClass3 = "";
		}
		if (viewClass2 == null) {
			viewClass2 = "";
		}

		if (viewClass3.equals(clssId)) {
			// viewClass3 세션을 수정해서 새로운 clssId로 설정
			session.setAttribute("viewClass3", clssId);
			session.setAttribute("viewClass2", viewClass2);
			session.setAttribute("viewClass1", viewClass1);
		} else if (viewClass2.equals(clssId)) {
			// viewClass2 세션을 수정해서 새로운 clssId로 설정
			session.setAttribute("viewClass2", viewClass3);
			session.setAttribute("viewClass1", viewClass1);
			session.setAttribute("viewClass3", clssId);
		} else {
			// viewClass1 세션을 수정해서 새로운 clssId로 설정
			session.setAttribute("viewClass1", viewClass2);
			session.setAttribute("viewClass2", viewClass3);
			session.setAttribute("viewClass3", clssId);
		}

		ClassVO class1 = studentService.selectviewClass((String) session.getAttribute("viewClass1"));
		model.addAttribute("class1", class1);
		ClassVO class2 = studentService.selectviewClass((String) session.getAttribute("viewClass2"));
		model.addAttribute("class2", class2);

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

	// 교육 지원 중복 내여 체크
	/**
	 * @author : dabin
	 * @return
	 * @date : 2023. 9. 10.
	 * @parameter :model
	 * @throws IOException
	 */
	@GetMapping("/upload/{classId}/check")
	@ResponseBody
	public int checkApplyYN(HttpSession session, @PathVariable String classId, Model model) {

		// 지원 내역 중복 확인
		String stdtId = (String) session.getAttribute("stdtId");
		int applyYN = studentService.getAplyYN(stdtId, classId);
		return applyYN;

	}

	// 교욱 이력서 제출
	/**
	 * @author : dabin
	 * @date : 2023. 8. 31.
	 * @parameter :model
	 * @return :
	 * @throws IOException
	 */

	@PostMapping("/upload/{classId}")
	public String uplodApplyFile(HttpSession session, RedirectAttributes redirectAttrs,
			@RequestParam("file") MultipartFile file, @PathVariable String classId, Model model) throws IOException {
		String stdtId = (String) session.getAttribute("stdtId");

		// 업로드하기
		String maxFileId = uploadFileService.getMaxFileId();
		int subFileId = 1;
		FileVO fileVO = new FileVO();
		fileVO.setFileId(maxFileId);
		fileVO.setFileSubId(subFileId);
		fileVO.setFileNm(file.getOriginalFilename());
		fileVO.setFileSize(file.getSize());
		fileVO.setFileType(file.getContentType());
		fileVO.setFileContent(file.getBytes());
		uploadFileService.uploadFile(fileVO);
		subFileId++;

		// 이력서 내역 추가하기
		String maxApplyId = studentService.getMaxAplyId();
		ApplyVO apply = new ApplyVO();
		apply.setAplyId(maxApplyId);
		apply.setStdtId(stdtId);
		apply.setClssId(classId);
		apply.setAplyCd("APL0000002");
		apply.setFileId(maxFileId);
		apply.setRgsterId(stdtId);
		studentService.uploadAplyFile(apply);

		return "redirect:/student/class";
	}

	// 마이페이지 이동
	/**
	 * @author : dabin
	 * @date : 2023. 8. 31.
	 * @parameter :model
	 * @return :
	 * @throws IOException
	 */
	@GetMapping("/mypage")
	public String mypageMain(HttpSession session, Model model) {
		model.addAttribute("student", student);
		String stdtId = (String) session.getAttribute("stdtId");
		List<ApplyDetailDTO> applyList = studentService.searchAplyList(stdtId);
		model.addAttribute("applyList", applyList);
		return "student/mypage";
	}

	// 교욱 리스트 검색 버튼
	/**
	 * @author : dabin
	 * @date : 2023. 9 .8
	 * @parameter : session, model
	 * @return :
	 */

	@GetMapping("/class/search")
	@ResponseBody
	public List<ClassVO> searchClasses(@RequestParam("keyword") String keyword,
			@RequestParam("ingClass") String ingClass, Model model) {
		// 검색어(keyword)를 사용하여 교육 과정을 검색하고 결과를 모델에 담습니다.
		List<ClassVO> searchResults = studentService.searchClasses(keyword, ingClass);
		model.addAttribute("classList", searchResults);
		return searchResults;
	}

	// 마이페이지 지원내역 조회
	/**
	 * @author : dabin
	 * @date : 2023. 9 .13
	 * @parameter : session, model
	 * @return :
	 */

	@GetMapping("/mypage/aplyList")
	@ResponseBody
	public List<ApplyDetailDTO> searchAplyList(HttpSession session, Model model) {
		String stdtId = (String) session.getAttribute("stdtId");
		List<ApplyDetailDTO> applyList = studentService.searchAplyList(stdtId);
		model.addAttribute("applyList", applyList);
		return applyList;
	}

	// 마이페이지 지원내역 업데이트
	/**
	 * @author : dabin
	 * @date : 2023. 9 .13
	 * @parameter : session, model
	 * @return :
	 */

	@PostMapping("/mypage/aplyList/update")
	@ResponseBody
	public void updateAply(@RequestParam("aplyCd") String aplyCd, @RequestParam("aplyId") String aplyId,
			HttpSession session) {
		/* String stdtId = (String) session.getAttribute("stdtId"); */
		studentService.updateaply(aplyCd, aplyId);
	}

	// 마이페이지 수강내역 추가
	/**
	 * @author : dabin
	 * @date : 2023. 9 .13
	 * @parameter : session, model
	 * @return :
	 */

	@PostMapping("/insert/rgstList")
	@ResponseBody
	public void insertRgst(@RequestParam("aplyId") String aplyId, HttpSession session) {

		String maxRgstId = studentService.getMaxRegistrationId();
		String stdtId = (String) session.getAttribute("stdtId");
		studentService.insertRgst(aplyId, maxRgstId, stdtId);
	}

	// 마이페이지 수강내역 업데이트
	/**
	 * @author : dabin
	 * @return 
	 * @date : 2023. 9. 14
	 * @parameter :model
	 * @throws IOException
	 */

	@PostMapping("/mypage/uploadFile/{aplyId}")
	public String updateAplyFile(@PathVariable String aplyId, @RequestParam("formData") MultipartFile file,HttpSession session)
			throws IOException {
		String stdtId = (String) session.getAttribute("stdtId");

		int maxFileSubId = studentService.getmaxSubId(aplyId);

		FileVO fileVO = new FileVO();
		fileVO.setFileNm(file.getOriginalFilename());
		fileVO.setFileSize(file.getSize());
		fileVO.setFileType(file.getContentType());
		fileVO.setFileContent(file.getBytes());
		studentService.updateAplyFile(aplyId, fileVO, maxFileSubId);
		studentService.updateAplydt(aplyId, stdtId);
		
		return "redirect:/student/mypage";
	}
}
