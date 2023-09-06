package com.finalprj.kess.controller;

import java.net.http.HttpRequest;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.sql.rowset.Joinable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.finalprj.kess.model.ClassVO;
import com.finalprj.kess.model.CompanyVO;
import com.finalprj.kess.model.FileVO;
import com.finalprj.kess.model.LectureVO;
import com.finalprj.kess.model.ManagerVO;
import com.finalprj.kess.model.PostVO;
import com.finalprj.kess.model.ProfessorVO;
import com.finalprj.kess.model.SubjectVO;
import com.finalprj.kess.repository.IUploadFileRepository;
import com.finalprj.kess.service.IAdminService;
import com.finalprj.kess.service.IManagerService;
import com.finalprj.kess.service.IUploadFileService;

import jakarta.servlet.http.HttpSession;



@Controller
@RequestMapping("/admin")
public class AdminController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	IAdminService adminService;
	@Autowired
	IManagerService managerService;
	@Autowired
	IUploadFileService uploadFileService;

	/**
	 * @author : eunji
	 * @date : 2023. 8. 22.
	 * @parameter : session, model
	 * @return : String
	 */
	@RequestMapping("")
	public String main(HttpSession session, Model model) {
		if(session.getAttribute("userEmail") == null) {
			return "redirect:/login";
		}

		//공지사항 개수
		int noticeCnt = adminService.getNoticeCnt();
		model.addAttribute("noticeCnt", noticeCnt);

		//문의사항 개수
		int inquiryCnt = adminService.getInquiryCnt();
		model.addAttribute("inquiryCnt", inquiryCnt);

		//기업 개수
		int companyCnt = adminService.getCompanyCnt();
		model.addAttribute("companyCnt", companyCnt);

		//과목 개수
		int subjectCnt = adminService.getSubjectCnt();
		model.addAttribute("subjectCnt", subjectCnt);

		//교육생 개수
		int studentCnt = adminService.getStudentCnt();
		model.addAttribute("studentCnt", studentCnt);

		//교육과정 개수
		int classCnt = adminService.getClassCnt();
		model.addAttribute("classCnt", classCnt);

		//강사 개수
		int professorCnt = adminService.getProfesserCnt();
		model.addAttribute("professorCnt", professorCnt);

		//업무담당자 개수
		int managerCnt = adminService.getManagerCnt();
		model.addAttribute("managerCnt", managerCnt);

		//답변대기 문의 개수
		int waitInquiryCnt = adminService.getWaitInquiryCnt();
		model.addAttribute("waitInquiryCnt", waitInquiryCnt);

		//답변대기 문의List
		List<PostVO> waitInquiryList = adminService.getWaitInquiryList();
		model.addAttribute("waitInquiryList", waitInquiryList);

		//교육완료 개수
		int completeClassCnt = adminService.getCompleteClassCnt();
		model.addAttribute("completeClassCnt", completeClassCnt);

		//교육완료List
		List<ClassVO> completeClassList = adminService.getCompleteClassList();
		model.addAttribute("completeClassList", completeClassList);

		return "admin/dashboard";
	}

	@RequestMapping("/inquiry")
	public String inquiry(HttpSession session, Model model) {
		//로그인 정보 저장
		session.setAttribute("role", "admin");
		session.setAttribute("id", "admin");

		//title
		String title ="문의사항 관리";
		model.addAttribute("title", title);

		//link 연결시 필요한 값
		String url="inquiry";
		model.addAttribute("url", url);

		//문의사항 리스트 객체 생성
		List<PostVO> postVOList = new ArrayList<PostVO>();

		//service로부터 문의사항 전체 리스트 가져오기
		//id가 INQ-----인것만
		String postValue = "INQ";
		postVOList = adminService.getPostVOList(postValue);
		//logger.warn(postVOList.toString());
		model.addAttribute("postVOList", postVOList);


		return "manager_post_list";
	}

	@RequestMapping("/notice")
	public String notice(HttpSession session, Model model) {
		//로그인 정보 저장
		session.setAttribute("role", "admin");
		session.setAttribute("id", "admin");

		//title
		String title ="공지사항 관리";
		model.addAttribute("title", title);

		//link 연결시 필요한 값
		String url="inquiry";
		model.addAttribute("url", url);

		//공지사항 리스트 객체 생성
		List<PostVO> postVOList = new ArrayList<PostVO>();

		//service로부터 문의사항 전체 리스트 가져오기
		//id가 INQ-----인것만
		String postValue = "NTC";
		postVOList = adminService.getPostVOList(postValue);
		//logger.warn(postVOList.toString());
		model.addAttribute("postVOList", postVOList);


		return "manager_post_list";
	}

	@RequestMapping("/class")
	public String classList(HttpSession session, Model model) {
		//교육과정 리스트 객체 생성
		List<ClassVO> classList = adminService.getClassList();
		model.addAttribute("classList", classList);

		//교육과정 상태
		List<String> classCodeNameList = adminService.getClassCodeNameList();
		model.addAttribute("classCodeNameList", classCodeNameList);


		return "admin/class_list";
	}

	@GetMapping("/class/insert")
	public String insertClass(HttpSession session, Model model) {

		//title
		String title = "교육과정 등록";
		model.addAttribute("title", title);

		//insert or select or update
		String act="insert";
		model.addAttribute("act", act);

		//클래스 하나 생성하고 classId값 지정해서 넘기기
		ClassVO classVO = new ClassVO();
		classVO.setClssId(adminService.getMaxClassId());

		model.addAttribute("classVO", classVO);

		//교육상태 리스트
		List<String> classCodeNameList = adminService.getClassCodeNameList();
		model.addAttribute("classCodeNameList", classCodeNameList);

		//업체 리스트
		List<CompanyVO> companyList = adminService.getCompanyList();
		model.addAttribute("companyList", companyList);

		//업무담당자
		List<ManagerVO> managerList = adminService.getManagerList();
		model.addAttribute("managerList", managerList);

		//강의 리스트
		List<LectureVO> lectureList = adminService.getLectureList();
		model.addAttribute("lectureList", lectureList);

		return "admin/class_form";
	}

	@PostMapping("/class/insert")
	public String insertClass(HttpSession session,
			@RequestParam("files") MultipartFile[] files, RedirectAttributes redirectAttrs) {
		//adminService.insertClassVO(classVO);
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

		//교육과정 업로드하기
		return "redirect:/admin/class";
	}



	@RequestMapping("/professor")
	public String professor(HttpSession session, Model model) {
		//로그인 정보 저장
		session.setAttribute("role", "admin");
		session.setAttribute("id", "admin");

		//title
		String title = "강사 관리";
		model.addAttribute("title", title);

		//강사 리스트 객체 생성
		//List<ProfessorVO> professorList = adminService.getProfessorList();
		//model.addAttribute("professorList", professorList);

		return "manager_professor_list";
	}

	@RequestMapping("/manager")
	public String manager(HttpSession session, Model model) {
		//로그인 정보 저장
		session.setAttribute("role", "admin");
		session.setAttribute("id", "admin");

		//title
		String title = "업무담당자 관리";
		model.addAttribute("title", title);

		return "manager_manager_list";
	}

	@GetMapping("/class/autocomplete")
	@ResponseBody
	public String classAutocomplete(@RequestParam String term) {
		//자동완성 검색어 목록 생성
		List<String> autocompleteResults = new ArrayList<>();
		//결과 생성
		autocompleteResults = adminService.getClassSearch(term);

		// 결과를 HTML 형태로 변환
		StringBuilder resultBuilder = new StringBuilder();
		for (String result : autocompleteResults) {
			resultBuilder.append("<option value=\"").append(result).append("\">").append(result).append("</option>");
		}

		return resultBuilder.toString();
	}

	@GetMapping("/class/search")
	@ResponseBody
	public List<ClassVO> classSearch(@RequestParam String className, @RequestParam(name="statusArray[]") List<String> statusArray,
			@RequestParam Date aplyStartDt, @RequestParam Date aplyEndDt,
			@RequestParam Date classStartDd, @RequestParam Date classEndDd) {
		logger.warn("className: "+className+" statusArray: "+statusArray);
		logger.warn("aply: "+aplyStartDt+"~"+aplyEndDt);
		logger.warn("class: "+classStartDd+"~"+classEndDd);

		//검색 결과통해서 교육과정 리스트 객체 생성
		List<ClassVO> classVOList = adminService.getSearchClassVOList(className, statusArray, aplyStartDt, aplyEndDt, classStartDd, classEndDd);
		logger.warn(classVOList.toString());
		return classVOList;
	}

	@PostMapping("/class/insert/lectureselect")
	@ResponseBody
	public Map<String, Object> fetchLectureSelect(@RequestParam("lectureId") String lectureId){
		SubjectVO subjectVO = adminService.getSubject(lectureId);
		ProfessorVO professorVO = adminService.getProfessor(lectureId);
		LectureVO lectureVO = adminService.getLecture(lectureId);
		
		
		Map<String, Object> response = new HashMap<>();
		// subjectName과 professorName을 설정합니다.
		response.put("subject", subjectVO);
		response.put("professor", professorVO);
		response.put("lecture", lectureVO);
		
		return response;
	}

	@PostMapping("/class/insert/getlecturelist")
	@ResponseBody
	public Map<String, Object> fetchLectureSelect(){
		//강의 리스트
		List<LectureVO> lectureList = adminService.getLectureList();

		Map<String, Object> response = new HashMap<>();
		//lectureList담기
		response.put("lectureList", lectureList);

		return response;
	}
}
