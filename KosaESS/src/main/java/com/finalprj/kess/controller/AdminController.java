package com.finalprj.kess.controller;

import java.net.http.HttpRequest;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.sql.rowset.Joinable;

import org.apache.naming.java.javaURLContextFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

import com.finalprj.kess.dto.ClassInsertDTO;
import com.finalprj.kess.dto.CurriculumDetailDTO;
import com.finalprj.kess.model.ClassVO;
import com.finalprj.kess.model.CommonCodeVO;
import com.finalprj.kess.model.CompanyVO;
import com.finalprj.kess.model.CurriculumVO;
import com.finalprj.kess.model.FileVO;
import com.finalprj.kess.model.LectureVO;
import com.finalprj.kess.model.ManagerVO;
import com.finalprj.kess.model.PostVO;
import com.finalprj.kess.model.ProfessorVO;
import com.finalprj.kess.model.SubjectVO;
import com.finalprj.kess.repository.IUploadFileRepository;
import com.finalprj.kess.service.AdminService;
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
		if(session.getAttribute("mngrId")== null) {
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

	@RequestMapping("/class/{clssId}")
	public String classDetail(@PathVariable String clssId, HttpSession session, Model model) {
		//title
		String title = "교육과정 상세";
		model.addAttribute("title", title);

		//insert or select or update
		String act="select";
		model.addAttribute("act", act);
		
		//해당 교육과정 객체 가져오기
		ClassVO classVO = adminService.getClass(clssId);
		model.addAttribute("classVO", classVO);
		
		//파일 가져오기
		List<FileVO> fileList = null;
		if(classVO.getFileId() != null) {
			fileList = uploadFileService.getFileList(classVO.getFileId());
		}
		model.addAttribute("fileList", fileList);
		
		//강의 가져오기
		List<CurriculumVO> curriculumList = adminService.getCurriculumList(classVO.getClssId());
		List<CurriculumDetailDTO> curriculumDetailList = null;
		
		if(curriculumList != null) {
			curriculumDetailList = new ArrayList<CurriculumDetailDTO>();
			for(CurriculumVO curriculumVO: curriculumList) {
				CurriculumDetailDTO curriculumDetailDTO= adminService.getCurriculumDetail(curriculumVO.getLctrId());
				curriculumDetailList.add(curriculumDetailDTO);
			}
		}
		model.addAttribute("curriculumDetailList", curriculumDetailList);
		
		return "admin/class_form";
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
		List<CommonCodeVO> classCommonCodeList = adminService.getCommonCodeList("GRP0000002");
		model.addAttribute("classCommonCodeList", classCommonCodeList);

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
			@RequestParam("files") MultipartFile[] files, RedirectAttributes redirectAttrs,
			ClassInsertDTO classInsertDTO, @RequestParam String clssCd, @RequestParam(required = false) String cmpyId,
			@RequestParam(required = false) String mngrId, @RequestParam(name="lctrId", required = false) List<String> lctrIds) {

		if(session.getAttribute("mngrId")== null) {
			return "redirect:/login";
		}

		//파일을 첨부하지 않았다면 maxFileId와 fileList는 null.
		String maxFileId = null;	
		List<FileVO> fileList=null;

		//파일을 첨부했다면 List생성해서 전달
		if(files[0]!=null && !files[0].isEmpty()) {
			maxFileId = uploadFileService.getMaxFileId();
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
						fileList.add(fileVO);
						subFileId++;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				redirectAttrs.addFlashAttribute("message", e.getMessage());
			}
		}

		//교육과정 생성하기
		ClassVO classVO = new ClassVO();
		classVO.setClssId(classInsertDTO.getClssId());
		classVO.setMngrId(mngrId);
		classVO.setCmpyId(cmpyId);
		classVO.setClssNm(classInsertDTO.getClssNm());
		classVO.setClssContent(classInsertDTO.getClssContent());
		classVO.setLimitCnt(classInsertDTO.getLimitCnt());

		//Date-time(String) to Timestamp
		try {
			String aplyStartDt = classInsertDTO.getAplyStartDt(); // "2023-09-07T12:06"
			String aplyEndDt = classInsertDTO.getAplyEndDt();

			if (aplyStartDt.equals("null") && aplyEndDt.equals("null")) {
				classVO.setAplyStartDt(null);
				classVO.setAplyEndDt(null);
			} else {
				try {
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
					java.util.Date parsedStartDate = dateFormat.parse(aplyStartDt);
					java.util.Date parsedEndDate = dateFormat.parse(aplyEndDt);
					Timestamp startTimestamp = new Timestamp(parsedStartDate.getTime());
					Timestamp endTimestamp = new Timestamp(parsedEndDate.getTime());
					classVO.setAplyStartDt(startTimestamp);
					classVO.setAplyEndDt(endTimestamp);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		//Time(String) to Timestamp
		try {
			String setInTm = classInsertDTO.getSetInTm();
			String setOutTm = classInsertDTO.getSetOutTm();
			SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
			java.util.Date parsedSetInTm = dateFormat.parse(setInTm);
			java.util.Date parsedSetOutTm = dateFormat.parse(setOutTm);
			// Date 객체를 Timestamp로 변환합니다.
			Timestamp setInTimestamp = new Timestamp(parsedSetInTm.getTime());
			Timestamp sertOutTimestamp = new Timestamp(parsedSetOutTm.getTime());
			classVO.setSetInTm(setInTimestamp);
			classVO.setSetOutTm(sertOutTimestamp);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//String to Date
		try {
			String clssStartDd = (String)classInsertDTO.getClssStartDd();
			String clssEndDd = (String)classInsertDTO.getClssEndDd();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			java.util.Date startDate = dateFormat.parse(clssStartDd);
			java.util.Date endDate = dateFormat.parse(clssEndDd);

			// java.util.Date를 java.sql.Date로 변환
			java.sql.Date sqlStartDate = new java.sql.Date(startDate.getTime());
			java.sql.Date sqlEndDate = new java.sql.Date(endDate.getTime());

			classVO.setClssStartDd(sqlStartDate);
			classVO.setClssEndDd(sqlEndDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}


		classVO.setClssCd(clssCd);
		classVO.setClssAdr(classInsertDTO.getClssAdr());
		classVO.setClssTotalTm(classInsertDTO.getClssTotalTm());
		classVO.setFileId(maxFileId);
		classVO.setClssEtc(classInsertDTO.getClssEtc());
		classVO.setRgsterId((String)session.getAttribute("mngrId"));

		//커리큘럼을 지정하지 않았다면 null전달
		List<CurriculumVO> curriculumList = null;
		if(lctrIds != null) {
			curriculumList = new ArrayList<CurriculumVO>();
			for(String lctrId : lctrIds) {
				CurriculumVO curriculumVO = new CurriculumVO();
				curriculumVO.setClssId(classVO.getClssId());
				curriculumVO.setLctrId(lctrId);
				curriculumVO.setRgsterId((String)session.getAttribute("mngrId"));
				curriculumList.add(curriculumVO);
			}
		}

		adminService.createClass(fileList, classVO, curriculumList);

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
