package com.finalprj.kess.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Date;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
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

import com.finalprj.kess.dto.CurriculumDetailDTO;
import com.finalprj.kess.dto.ReasonDTO;
import com.finalprj.kess.dto.StudentInfoDTO;
import com.finalprj.kess.dto.SubsidyDTO;
import com.finalprj.kess.dto.WorklogDTO;
import com.finalprj.kess.model.ClassVO;
import com.finalprj.kess.model.CommonCodeVO;
import com.finalprj.kess.model.CurriculumVO;
import com.finalprj.kess.model.FileVO;
import com.finalprj.kess.model.ManagerVO;
import com.finalprj.kess.model.ReasonVO;
import com.finalprj.kess.model.SubsidyVO;
import com.finalprj.kess.model.WorklogVO;
import com.finalprj.kess.service.IAdminService;
import com.finalprj.kess.service.IManagerService;
import com.finalprj.kess.service.IStudentService;
import com.finalprj.kess.service.UploadFileService;

import jakarta.mail.Session;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/manager")
public class ManagerController {
	LocalDate today = LocalDate.now();
	LocalDate firstDay = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth());
//	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	IManagerService managerService;
	@Autowired
	IStudentService studentService;
	@Autowired
	IAdminService adminService;
	@Autowired
	UploadFileService uploadFileService;

	//	메인 페이지
	@GetMapping("")
	public String managerMain(Model model, HttpSession session) {
		//유저 필터링
		if(session.getAttribute("roleCd")== null) {
			return "redirect:/login";
		}else if(((String)session.getAttribute("roleCd")).equals("ROL0000001")){
			return "redirect:/student";
		}else if(((String)session.getAttribute("roleCd")).equals("ROL0000002")){
			return "redirect:/admin";
		}
		
		List<ClassVO> classList = managerService.getClassListByMngrId((String) session.getAttribute("mngrId"), LocalDate.now().getYear());
		
		model.addAttribute("title","메인");
		model.addAttribute("classList",classList);
		
		return "manager/manager_main";
	}

	//	담당 교육 목록 조회
	@GetMapping("/class")
	public String getClassList(Model model, HttpSession session) {
		//로그인 필터링
		if(session.getAttribute("roleCd")== null) {
			return "redirect:/login";
		}else if(((String)session.getAttribute("roleCd")).equals("ROL0000001")){
			return "redirect:/student";
		}else if(((String)session.getAttribute("roleCd")).equals("ROL0000002")){
			return "redirect:/admin";
		}
		//end
		String mngrId = (String)session.getAttribute("mngrId");
		model.addAttribute("title", "교육과정 관리");
		List<ClassVO> classList = managerService.getClassListByMngrId(mngrId, null);
		// session의 key-value를 설정 할 때 value가 object로 업캐스팅 된다. get 할 때 다운캐스팅 할 것
		for (ClassVO vo : classList) {
			vo.setRgstCnt(managerService.getRgstCountByClssId(vo.getClssId()));
		}
		model.addAttribute("classList", classList);
		model.addAttribute("resultCount", classList.size());
		List<CommonCodeVO> classCodeNameList = managerService.getCodeNameList("CLS");
		List<CommonCodeVO> cmptCodeNameList = managerService.getCodeNameList("CLS");
		model.addAttribute("classCodeNameList", classCodeNameList);
		model.addAttribute("cmptCodeNameList", cmptCodeNameList);
		//교육과정이 존재하는 연도 목록 추가
		List<Integer> yearList = managerService.getYearList(mngrId);
		model.addAttribute("yearList", yearList);
		return "manager/class_list";

	}

	//	교육 상세 조회
	/*
	@GetMapping("/class/{classId}")
	public String getClassDetail(Model model, @PathVariable String classId, HttpSession session) {
		//유저 필터링
		if(session.getAttribute("roleCd")== null) {
			return "redirect:/login";
		}else if(((String)session.getAttribute("roleCd")).equals("ROL0000001")){
			return "redirect:/student";
		}else if(((String)session.getAttribute("roleCd")).equals("ROL0000002")){
			return "redirect:/admin";
		}
		
		// 학생 정보를 세션에서 가져와서 student 객체에 저장
		String student = (String) session.getAttribute("student");
		model.addAttribute("student", student);

		// classDetail 객체를 가져와서 모델에 추가
		ClassVO classDetail = studentService.selectClass(classId);
		classDetail.setRgstCnt(managerService.getRgstCountByClssId(classId));
		model.addAttribute("classDetail", classDetail);

		List<CurriculumDetailDTO> curriculumlist = studentService.getCurriculumList(classId);
		model.addAttribute("curriculumlist", curriculumlist);

		// classDetailList 객체를 가져와서 모델에 추가
		List<FileVO> classFileList = studentService.selectAllClassFile(classId);
		model.addAttribute("classFileList", classFileList);
		
		model.addAttribute("title", "교육 과정 상세");
		
		return "manager/class_detail";
	}
	*/
	@RequestMapping("/class/{classId}")
	public String classDetail(@PathVariable String classId, HttpSession session, Model model) {
		//로그인 필터링
		if(session.getAttribute("roleCd")== null) {
			return "redirect:/login";
		}else if(((String)session.getAttribute("roleCd")).equals("ROL0000001")){
			return "redirect:/student";
		}else if(((String)session.getAttribute("roleCd")).equals("ROL0000002")){
			return "redirect:/admin";
		}
		//end
		
		// title
		String title = "교육과정 상세";
		model.addAttribute("title", title);

		// 해당 교육과정 객체 가져오기
		ClassVO classVO = adminService.getClass(classId);
		DecimalFormat formatter = new DecimalFormat("##,###,###");
		classVO.setSSubsidy(formatter.format(classVO.getClssSubsidy()));
		model.addAttribute("classVO", classVO);

		// 파일 가져오기
		List<FileVO> fileList = null;
		if (classVO.getFileId() != null) {
			fileList = uploadFileService.getFileList(classVO.getFileId());
		}
		model.addAttribute("fileList", fileList);

		// 강의 가져오기
		List<CurriculumVO> curriculumList = adminService.getCurriculumList(classVO.getClssId());
		List<CurriculumDetailDTO> curriculumDetailList = null;

		if (curriculumList != null) {
			curriculumDetailList = new ArrayList<CurriculumDetailDTO>();
			for (CurriculumVO curriculumVO : curriculumList) {
				CurriculumDetailDTO curriculumDetailDTO = adminService.getCurriculumDetail(curriculumVO.getLctrId());
				curriculumDetailList.add(curriculumDetailDTO);
			}
		}
		model.addAttribute("curriculumDetailList", curriculumDetailList);

		return "manager/class_detail";
	}

	// 교육 상세 정보 > 파일 요청
	@GetMapping("/{fileId}/{fileSubId}")
	public ResponseEntity<byte[]> getFile(@PathVariable String fileId, @PathVariable int fileSubId, HttpSession session) {
		//유저 필터링
		if(session.getAttribute("roleCd")== null) {
			return null;
		}else if(((String)session.getAttribute("roleCd")).equals("ROL0000001")){
			return null;
		}else if(((String)session.getAttribute("roleCd")).equals("ROL0000002")){
			return null;
		}
				
		FileVO file = managerService.getFileByIds(fileId, fileSubId);
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
	@GetMapping("/student")
	public String getStudentList(
			Model model, HttpSession session, HttpServletRequest httpServletRequest,
			@RequestParam(required = false) String classId,
			@RequestParam(required = false) String startDate,
			@RequestParam(required = false) String endDate) {
		
		//로그인 필터링
		if(session.getAttribute("roleCd")== null) {
			return "redirect:/login";
		}else if(((String)session.getAttribute("roleCd")).equals("ROL0000001")){
			return "redirect:/student";
		}else if(((String)session.getAttribute("roleCd")).equals("ROL0000002")){
			return "redirect:/admin";
		}
		//end
		
		List<ClassVO> classList = new ArrayList<ClassVO>();
		
		ClassVO emptyClassVO = new ClassVO();
		emptyClassVO.setClssNm("교육과정명을 선택하세요");
		classList.add(emptyClassVO);
		
		//교육과정이 존재하는 연도 목록 추가
		List<Integer> yearList = managerService.getYearList((String)session.getAttribute("mngrId"));
		model.addAttribute("yearList", yearList);
		
		if(classId!=null) {
//			//Param으로 시작/종료 날짜가 null일 경우 시작/종료 날짜에 교육과정 시작/종료 일자를 대입
			if (startDate == null) {
				startDate = String.valueOf(managerService.getClassDetailByClssId(classId).getClssStartDd());
			}
			if (endDate == null) {
				endDate = String.valueOf(managerService.getClassDetailByClssId(classId).getClssEndDd());
			}
//			//end
			model.addAttribute("thisClass", managerService.getClassDetailByClssId(classId));//requestParam에 해당하는 수업 정보 전달
		}else {
			model.addAttribute("thisClass", emptyClassVO);
		}

		List<StudentInfoDTO> stdtList = managerService.getStudentListByOnlyClssId(classId);//학생 이름 목록
		classList = managerService.getClassListByMngrId((String) session.getAttribute("mngrId"), yearList.get(0));
		
		List<CommonCodeVO> classCodeNameList = managerService.getCodeNameList("CLS");//
		List<CommonCodeVO> stdtCodeNameList = managerService.getCodeNameList("RST");
		List<CommonCodeVO> cmptCodeNameList = managerService.getCodeNameList("CMP");
		List<CommonCodeVO> wlogCodeNameList = managerService.getCodeNameList("WOK");

//		double totalTm = managerService.getTotalTmByClssId(classId);
		double stdtTmSum;
		for (StudentInfoDTO stdt : stdtList) {
			// 출결 가져오기
			stdt.setWlogCnt("");
			for (CommonCodeVO cmcd : wlogCodeNameList) {
				stdt.appendWlogCnt(String.valueOf(cmcd.getCmcdId() + managerService.getCountByClssIdWlogCdStdtId(classId, cmcd.getCmcdId(), stdt.getStdtId(), startDate, endDate)));
				stdt.appendWlogCnt(",");
			}

			// 이수율 입력하기
			stdtTmSum = managerService.getStudentTmSumByIds(classId, stdt.getStdtId());
			if(Double.isNaN(stdtTmSum)) {
				stdtTmSum = 0;
			}
			if(managerService.getClassDetailByClssId(classId).getClssTotalTm()==0) {
				stdt.setCmptRate(0);
			} else if(stdtTmSum/managerService.getClassDetailByClssId(classId).getClssTotalTm()>1) {
				stdt.setCmptRate(100.0);
			} else if(stdtTmSum/managerService.getClassDetailByClssId(classId).getClssTotalTm()<0) {
				stdt.setCmptRate(0);
			} else {
				stdt.setCmptRate(100.0 * stdtTmSum/managerService.getClassDetailByClssId(classId).getClssTotalTm());
			}
		}
		
		model.addAttribute("title", "교육생 관리");
		model.addAttribute("classCodeNameList", classCodeNameList);//교육과정 상태 넘김
		model.addAttribute("stdtCodeNameList", stdtCodeNameList);//학생 등록 상태 넘김
		model.addAttribute("cmptCodeNameList", cmptCodeNameList);//이수 여부 관련 상태 넘김
		model.addAttribute("wlogCodeNameList", wlogCodeNameList);//출퇴근 상태 넘김
		model.addAttribute("classList", classList);
		model.addAttribute("stdtList", stdtList);
		model.addAttribute("stdtCnt", stdtList.size());
		return "manager/student_list";
	}

	//	개인정보 조회 : 비밀번호 확인창(GET)
	@GetMapping("/mypage")
	public String getMngrInfo(Model model, HttpSession session) {
		//유저 필터링
		if(session.getAttribute("roleCd")== null) {
			return "redirect:/login";
		}else if(((String)session.getAttribute("roleCd")).equals("ROL0000001")){
			return "redirect:/student";
		}else if(((String)session.getAttribute("roleCd")).equals("ROL0000002")){
			return "redirect:/admin";
		}
		
		if( session.getAttribute("isConfirmed") == null || !(boolean) session.getAttribute("isConfirmed")) {
			model.addAttribute("title", "비밀번호 확인");
			return "manager/mypage_confirm_pw";
		}else {
			String mngrId = (String) session.getAttribute("mngrId");
			ManagerVO thisManager = adminService.getManager(mngrId);
			model.addAttribute("title", "내 정보 관리");
			model.addAttribute("thisManager", thisManager);
			return "redirect:/manager/mypage/manager_info";
		}
	}
	
	//	개인정보 조회 : 비밀번호 확인 후 리다이렉팅
	@PostMapping("/mypage/confirm_pw")
	public String confirmPassword(Model model, HttpSession session
			, @RequestParam String password) {
		//유저 필터링
		if(session.getAttribute("roleCd")== null) {
			return "redirect:/login";
		}else if(((String)session.getAttribute("roleCd")).equals("ROL0000001")){
			return "redirect:/student";
		}else if(((String)session.getAttribute("roleCd")).equals("ROL0000002")){
			return "redirect:/admin";
		}
		
		String mngrId = (String) session.getAttribute("mngrId");
		ManagerVO thisManager = adminService.getManager(mngrId);
		
		if(thisManager.getUserPwd().equals(password)) {
			session.setAttribute("isConfirmed", Boolean.TRUE);
			return "redirect:/manager/mypage/manager_info";
		}else {
			return "redirect:/manager/mypage";
		}
	}

	//	개인정보 조회 : 개인정보 수정 화면
	@GetMapping("/mypage/manager_info")
	public String mypageUpdateInfo(Model model, HttpSession session) {
		//유저 필터링
		if(session.getAttribute("roleCd")== null) {
			return "redirect:/login";
		}else if(((String)session.getAttribute("roleCd")).equals("ROL0000001")){
			return "redirect:/student";
		}else if(((String)session.getAttribute("roleCd")).equals("ROL0000002")){
			return "redirect:/admin";
		}
		//주소를 통한 접근 차단
		if(!(boolean) session.getAttribute("isConfirmed")) {
			return "redirect:/manager/mypage";
		}
		String mngrId = (String) session.getAttribute("mngrId");
		ManagerVO thisManager = adminService.getManager(mngrId);
		
		model.addAttribute("title", "내 정보 관리");
		model.addAttribute("thisManager", thisManager);
		
		return "manager/mypage_updateInfo";
	}
	
	@GetMapping("/worklog")
	public String getWlogList(Model model, HttpSession session, HttpServletRequest httpServletRequest
			,@RequestParam(required = false) String clssId
			,@RequestParam(required = false) String startDate
			,@RequestParam(required = false) String endDate
			,@RequestParam(required = false) String wlogCd
			,@RequestParam(required = false) String keyword
			,@RequestParam(required = false) String isDelete
			,@RequestParam(required = false) String resnOnly
			,@RequestParam(required = false, value = "filterString[]") List<String> filterString
			) {
		//유저 필터링
		if(session.getAttribute("roleCd")== null) {
			return "redirect:/login";
		}else if(((String)session.getAttribute("roleCd")).equals("ROL0000001")){
			return "redirect:/student";
		}else if(((String)session.getAttribute("roleCd")).equals("ROL0000002")){
			return "redirect:/admin";
		}
		//End : 유저 필터링
		
		String mngrId = (String) session.getAttribute("mngrId");
		String title = "출퇴근 관리";
		
		//교육과정이 존재하는 연도 목록 추가
		List<Integer> yearList = managerService.getYearList((String)session.getAttribute("mngrId"));
		model.addAttribute("yearList", yearList);
		
		//------------------------------------------------------------------------------------
		List<CommonCodeVO> wlogCdList = managerService.getCodeNameList("WOK");
		model.addAttribute("wlogCdList", wlogCdList);
		
		List<ClassVO> classList = managerService.getClassListByMngrId(mngrId, LocalDate.now().getYear());
		
		if(clssId != null) {
			List<String> isManagerIdList = new ArrayList<String>();
			for (ClassVO vo : classList) {
				isManagerIdList.add(vo.getClssId());
			}
			if(!isManagerIdList.contains(clssId)) {
				//탈출
				return null;//<<<이 부분 에러 페이지 이동으로 수정하기
			}
		}
		
		ClassVO thisClass;
		
		if(clssId ==null) {
			clssId ="";
			thisClass = new ClassVO();
		}else {
			thisClass = managerService.getClassDetailByClssId(clssId);
		}
		if(startDate ==null) {
			startDate ="";
		}
		if(endDate ==null) {
			endDate ="";
		}
		if(keyword ==null) {
			keyword="";
		}
		
		List<WorklogDTO> wlogList = managerService.getWlogListByClssIdDate(mngrId, clssId, startDate, endDate, keyword, isDelete, resnOnly, filterString);
		
		model.addAttribute("thisClass",thisClass);
		model.addAttribute("wlogCnt", wlogList.size());
		model.addAttribute("classList", classList);
		model.addAttribute("wlogList", wlogList);
		model.addAttribute("title", title);
		session.setAttribute("searchWorklogList", wlogList);
		return "manager/wlog_list";
	}
	
	@GetMapping("/subsidy")
	public String getMoneyList(Model model, HttpSession session, HttpServletRequest httpServletRequest
			,@RequestParam(required = false) String clssId
			,@RequestParam(required = false) String startDate
			,@RequestParam(required = false) String endDate
			,@RequestParam(required = false) String keyword
			,@RequestParam(required = false, value = "filterString[]") List<String> filterString
			) {
		//유저 필터링
		if(session.getAttribute("roleCd")== null) {
			return "redirect:/login";
		}else if(((String)session.getAttribute("roleCd")).equals("ROL0000001")){
			return "redirect:/student";
		}else if(((String)session.getAttribute("roleCd")).equals("ROL0000002")){
			return "redirect:/admin";
		}
		//End : 유저 필터링
		//파라미터가 null이면 ""으로 변환
		if(clssId==null) {clssId="";}
		if(startDate==null) {startDate="";}
		if(endDate==null) {endDate="";}
		if(keyword==null) {keyword="";}
		//End : 파라미터가 null이면 ""으로 변환
		
		String mngrId = (String) session.getAttribute("mngrId");
		String title = "지원금 관리";
		List<SubsidyDTO> subsidyList = managerService.getSubsidyList(mngrId, clssId, startDate, endDate, keyword, filterString);
		List<ClassVO> classList = managerService.getClassListByMngrId(mngrId, null);
		List<CommonCodeVO> sbsdCodeNameList = managerService.getCodeNameList("SSD");
		List<CommonCodeVO> wlogCodeNameList = managerService.getCodeNameList("WOK");
		
		//교육과정이 존재하는 연도 목록 추가
		List<Integer> yearList = managerService.getYearList((String)session.getAttribute("mngrId"));
		model.addAttribute("yearList", yearList);
		
		for (SubsidyDTO dto : subsidyList) {
			dto.setWlog("");
			for (CommonCodeVO cmcd : wlogCodeNameList) {
				dto.appendWlog(String.valueOf(cmcd.getCmcdId() + managerService.getCountByClssIdWlogCdStdtId(clssId, cmcd.getCmcdId(), dto.getStdtId(), startDate, endDate)));
				dto.appendWlog(",");
			}
		}
		
		model.addAttribute("title", title);
		model.addAttribute("subsidyList", subsidyList);
		model.addAttribute("resultCount", subsidyList.size());
		model.addAttribute("classList", classList);
		model.addAttribute("sbsdCodeNameList", sbsdCodeNameList);
		model.addAttribute("wlogCodeNameList", wlogCodeNameList);
		
		return "manager/subsidy_view";
	}
	
	@GetMapping("/subsidy_insert")
	public String insertSubsidyView(Model model, HttpSession session, HttpServletRequest httpServletRequest) {
		//유저 필터링
		if(session.getAttribute("roleCd")== null) {
			return "redirect:/login";
		}else if(((String)session.getAttribute("roleCd")).equals("ROL0000001")){
			return "redirect:/student";
		}else if(((String)session.getAttribute("roleCd")).equals("ROL0000002")){
			return "redirect:/admin";
		}
		
		// mngrId
		String mngrId = (String) session.getAttribute("mngrId");
		
		// 페이지 제목 추가
		String title = "지원금 관리(등록)";
		model.addAttribute("title", title);
		
		//오늘 기준 연/월 및 월 길이 전달
		LocalDate now = LocalDate.now();
		int nowYear = now.getYear();
		int nowMonth = now.getMonthValue();
		int monthLength = new GregorianCalendar ( nowYear, nowMonth - 1,1).getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
		model.addAttribute("monthLength", monthLength);
		model.addAttribute("nowYear", nowYear);
		model.addAttribute("nowMonth", nowMonth);
		
		//교육과정이 존재하는 연도 목록 추가
		List<Integer> yearList = managerService.getYearList(mngrId);
		model.addAttribute("yearList", yearList);
		
		//교육과정 목록 추가
		List<ClassVO> classList = managerService.getClassListByMngrId(mngrId, yearList.get(0));
//		List<String> classIdList = new ArrayList<>();
//		List<String> classNameList = new ArrayList<>();
//		List<Integer> classSubsidyList = new ArrayList<>();
//		for (ClassVO clss : classList) {
//			classIdList.add(clss.getClssId());
//			classNameList.add(clss.getClssNm());
//			classSubsidyList.add(clss.getClssSubsidy());
//		}
//		model.addAttribute("classIdList", classIdList);
//		model.addAttribute("classNameList", classNameList);
//		model.addAttribute("classSubsidyList", classSubsidyList);
		model.addAttribute("classList", classList);
		
		//출석 종류 추가
		List<CommonCodeVO> wlogCodeNameList = managerService.getCodeNameList("WOK");
		model.addAttribute("wlogCodeNameList", wlogCodeNameList);
		
		return "manager/subsidy_insert";
	}
	
	/*
	@PostMapping("/subsidy/insert")
	public String insertSubsidyView(Model model, HttpSession session, HttpServletRequest httpServletRequest) {
		//유저 필터링
		if(session.getAttribute("roleCd")== null) {
			return "redirect:/login";
		}else if(((String)session.getAttribute("roleCd")).equals("ROL0000001")){
			return "redirect:/student";
		}else if(((String)session.getAttribute("roleCd")).equals("ROL0000002")){
			return "redirect:/admin";
		}
		//End : 유저 필터링
		
		String mngrId = (String) session.getAttribute("mngrId");
		String title = "지원금 관리(등록)";
		
		List<ClassVO> classList = managerService.getClassListByMngrId(mngrId, "", "");
		List<CommonCodeVO> monyCodeNameList = managerService.getCodeNameList("MNY");
		List<CommonCodeVO> wlogCodeNameList = managerService.getCodeNameList("WOK");
		
		model.addAttribute("title", title);
		model.addAttribute("classList", classList);
		model.addAttribute("monyCodeNameList", monyCodeNameList);
		model.addAttribute("wlogCodeNameList", wlogCodeNameList);
		
		return "manager/subsidy_insert";
	}
	*/
// AJAX 메서드---------------------------------------------------------------------------------------------------------------
	@GetMapping("/student/search")
	@ResponseBody
	public Map<String, Object> fetchStudentList(
			@RequestParam("classId") String classId, 
			@RequestParam("startDate") String startDate, 
			@RequestParam("endDate") String endDate,
			@RequestParam("keyword") String keyword,
			@RequestParam("cmptList[]") List<String> cmptList
			) {

		List<StudentInfoDTO> stdtList = managerService.getStudentListByClssId(classId, keyword, cmptList);
		List<CommonCodeVO> wlogCodeNameList = managerService.getCodeNameList("WOK");

		for (StudentInfoDTO stdt : stdtList) {
			stdt.setWlogCnt("");
			for (CommonCodeVO cmcd : wlogCodeNameList) {
				stdt.appendWlogCnt(String.valueOf(cmcd.getCmcdId() + managerService.getCountByClssIdWlogCdStdtId(classId, cmcd.getCmcdId(), stdt.getStdtId(), startDate, endDate)));
				stdt.appendWlogCnt(",");
			}
			double stdtTmSum = managerService.getStudentTmSumByIds(classId, stdt.getStdtId());
			stdtTmSum = managerService.getStudentTmSumByIds(classId, stdt.getStdtId());
			if(Double.isNaN(stdtTmSum)) {
				stdtTmSum = 0;
			}
			if(managerService.getClassDetailByClssId(classId).getClssTotalTm()==0) {
				stdt.setCmptRate(0);
			} else if(stdtTmSum/managerService.getClassDetailByClssId(classId).getClssTotalTm()>1) {
				stdt.setCmptRate(100.0);
			} else if(stdtTmSum/managerService.getClassDetailByClssId(classId).getClssTotalTm()<0) {
				stdt.setCmptRate(0);
			} else {
				stdt.setCmptRate(100.0 * stdtTmSum/managerService.getClassDetailByClssId(classId).getClssTotalTm());
			}
		}

		ClassVO thisClassVO = managerService.getClassDetailByClssId(classId);
		
		Map<String, Object> stdtListResponse = new HashMap<>();
		stdtListResponse.put("stdtList", stdtList);
		stdtListResponse.put("thisClassVO", thisClassVO);
		return stdtListResponse;
	}

	@GetMapping("/student/search/codename")
	@ResponseBody
	public Map<String, Object> fetchCodeNameList() {
		List<CommonCodeVO> wlogCodeList = managerService.getCodeNameList("WOK");
		Map<String, Object> wlogResponse = new HashMap<>();
		wlogResponse.put("wlogCodeList", wlogCodeList);
		return wlogResponse;
	}
	
	@GetMapping("/class/search")
	@ResponseBody
	public Map<String, Object> fetchClassList(
			HttpSession session
			,@RequestParam(name="searchKeyword") String searchKeyword
			,@RequestParam(value = "filterString[]") List<String> filterString
			,@RequestParam(value = "year") String year
			) {
		
		System.out.println("year : "+year);
		for (String string : filterString) {
			System.out.println("filterString : " + string);
		}
		List<ClassVO> classList;
		String mngrId = (String) session.getAttribute("mngrId");
		if(!year.equals("")) {
			classList = managerService.getFilteredClassListByMngrId(mngrId, filterString, searchKeyword, Integer.parseInt(year));
		}else {
			classList = managerService.getFilteredClassListByMngrId(mngrId, filterString, searchKeyword, null);
		}
		for (ClassVO vo : classList) {
			vo.setRgstCnt(managerService.getRgstCountByClssId(vo.getClssId()));
		}
		Map<String, Object> classListResponse = new HashMap<>();
		classListResponse.put("classList", classList);
		return classListResponse;
	}
	
	@PostMapping("/student/updateCmpt")
	@ResponseBody
	public String updateCmptCd(
			HttpSession session,
			@RequestParam("targetList[]") List<String> targetList,
			@RequestParam String targetCmptId,
			@RequestParam String clssId
			) {
		//업데이트
		for (String stdtId : targetList) {
			managerService.updateStdtCmptCd((String)session.getAttribute("mngrId"),stdtId, clssId, targetCmptId);
		}
		// End : 업데이트
		return "OK!";
		// End : 업데이트 결과 전송
	}
	
	@GetMapping("/worklog/search")
	@ResponseBody
	public Map<String, Object> getWlogSearch(HttpSession session, HttpServletRequest httpServletRequest
			,@RequestParam(required = false) String clssId
			,@RequestParam(required = false) String startDate
			,@RequestParam(required = false) String endDate
			,@RequestParam(required = false) String keyword
			,@RequestParam(required = false) String isDelete
			,@RequestParam(required = false) String resnOnly
			,@RequestParam("filterString[]") List<String> filterString
			) {
		
		//유저 필터링
		if(session.getAttribute("roleCd")== null) {
			return null;
		}else if(!((String)session.getAttribute("roleCd")).equals("ROL0000003")){
			return null;
		}
		//End : 유저 필터링
		
		/*
		//classId != null 일 경우, 자기가 담당한 교육에 대한 요청인지 확인
		if(clssId != null) {
			List<ClassVO> isManagerChkList = managerService.getClassListByMngrId((String) session.getAttribute("mngrId"),"", "");
			List<String> isManagerIdList = new ArrayList<String>();
			for (ClassVO vo : isManagerChkList) {
				isManagerIdList.add(vo.getClssId());
			}
			if(!isManagerIdList.contains(clssId)) {
				//탈출
				return null;//<<<이 부분 에러 페이지 이동으로 수정하기
			}
		}
		*/
		
		List<WorklogDTO> wlogList = managerService.getWlogListByClssIdDate((String)session.getAttribute("mngrId"), clssId, startDate, endDate, keyword, isDelete, resnOnly, filterString);
		
		for (WorklogDTO dto : wlogList) {
			dto.setStrInTmDd(dto.getInTmAsString());
			dto.setStrOutTmDd(dto.getOutTmAsString());
		}
		session.setAttribute("searchWorklogList", wlogList);
		Map<String, Object> wlogListResponse = new HashMap<>();
		wlogListResponse.put("wlogList", wlogList);
		return wlogListResponse;
	}
	@GetMapping("/worklog/getPeriod")
	@ResponseBody
	public Map<String, Object> getClassPeriod(HttpSession session
			, HttpServletRequest httpServletRequest
			,@RequestParam String clssId
			) {
		
		//유저 필터링
		if(session.getAttribute("roleCd")== null) {
			return null;
		}else if(!((String)session.getAttribute("roleCd")).equals("ROL0000003")){
			return null;
		}
		//End : 유저 필터링
		
		ClassVO targetClass = managerService.getClassDetailByClssId(clssId);
		
		Map<String, Object> response = new HashMap<>();
		response.put("classDetail", targetClass);
		return response;
	}
	
	//사유서 내용 출력
	@GetMapping("/worklog/resnContent")
	@ResponseBody
	public Map<String, Object> getResnContent(HttpSession session
			, HttpServletRequest httpServletRequest
			,@RequestParam String resnId
			) {
		
		//유저 필터링
		if(session.getAttribute("roleCd")== null) {
			return null;
		}else if(!((String)session.getAttribute("roleCd")).equals("ROL0000003")){
			return null;
		}
		//End : 유저 필터링
		
		ReasonDTO thisResn = managerService.getResnDetailByResnId(resnId);
		thisResn.setStrInTmDd(thisResn.getInTmAsString());
		thisResn.setStrOutTmDd(thisResn.getOutTmAsString());
		List<CommonCodeVO> resnCdList = managerService.getCodeNameList("RES");
		resnCdList.remove(0);
		Map<String, Object> response = new HashMap<>();
		response.put("resnContent", thisResn);
		response.put("resnCdList", resnCdList);
		return response;
	}
	
	//사유서 파일 목록 반환
	@GetMapping("/worklog/resn_file_list")
	@ResponseBody
	public Map<String, Object> getResnFile(HttpSession session
			, HttpServletRequest httpServletRequest
			,@RequestParam String resnId
			) {
		
		//유저 필터링
		if(session.getAttribute("roleCd")== null) {
			return null;
		}else if(!((String)session.getAttribute("roleCd")).equals("ROL0000003")){
			return null;
		}
		//End : 유저 필터링
		
		ReasonDTO thisResn = managerService.getResnDetailByResnId(resnId);
		String thisFileId = thisResn.getFileId();
		List<Integer> resnFileSubIdList = managerService.getFileSubIdListByFileId(thisResn.getFileId());
		List<FileVO> resnFileList = new ArrayList<FileVO>();
		for (Integer i : resnFileSubIdList) {
			resnFileList.add(managerService.getFileInfoByIds(thisFileId, i));
		}
		Map<String, Object> response = new HashMap<>();
		response.put("resnFileList", resnFileList);
		response.put("resnFileList", resnFileList);
		return response;
	}
	
	//사유서 내용 출력
	@GetMapping("/resn/{fileId}/{fileSubId}")
	@ResponseBody
	public ResponseEntity<byte[]> getFile(HttpSession session
			, HttpServletRequest httpServletRequest
			,@PathVariable("fileId") String fileId
			,@PathVariable("fileSubId") int fileSubId
			) {
		
		//유저 필터링
		if(session.getAttribute("roleCd")== null) {
			return null;
		}else if(!((String)session.getAttribute("roleCd")).equals("ROL0000003")){
			return null;
		}
		//End : 유저 필터링
		
		FileVO file = managerService.getFileByIds(fileId, fileSubId);
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
	
	@PostMapping("/worklog/update_resn_code")
	@ResponseBody
	public Map<String, Object> updateResnCd(
			HttpSession session,
			@RequestParam String resnId,
			@RequestParam String resnCd
			) {
		
		//유저 필터링
		if(session.getAttribute("roleCd")== null) {
			return null;
		}else if(!((String)session.getAttribute("roleCd")).equals("ROL0000003")){
			return null;
		}
		//End : 유저 필터링
		
		//업데이트
		managerService.updateResnCd(resnId, resnCd, (String)session.getAttribute("mngrId"));
		// End : 업데이트
		Map<String, Object> response = new HashMap<>();
		response.put("responseName", managerService.getCodeVO(resnCd).getCmcdNm());
		response.put("responseValue", resnCd);
		return  response;
		// End : 업데이트 결과 전송
	}

	//출퇴근 기록의 출석 상태 변경
	@PostMapping("/worklog/update_wlog_code")
	@ResponseBody
	public Map<String, Object> updateWlogCd(
			HttpSession session,
			@RequestParam (value="wlogList[]") List<String> wlogList,
			@RequestParam String wlogCd
			) {
		
		//유저 필터링
		if(session.getAttribute("roleCd")== null || (!((String)session.getAttribute("roleCd")).equals("ROL0000003"))) {
			return null;
		}
		//End : 유저 필터링
		
		List<String> result = new ArrayList<String>();
		
		//업데이트
		for (String wlogId : wlogList) {
			managerService.updateWlogCd(wlogId, wlogCd, (String)session.getAttribute("mngrId"));
			result.add(wlogCd+" : OK");
		}
		// End : 업데이트
		Map<String, Object> response = new HashMap<>();
		response.put("result", result);
		return response;
	}
	
	//출퇴근 기록 삭제
	@PostMapping("/worklog/delete_wlog")
	@ResponseBody
	public Map<String, Object> deleteWlog(
			HttpSession session,
			@RequestParam (value="wlogList[]") List<String> wlogList
			) {
		//유저 필터링
		if(session.getAttribute("roleCd")== null || (!((String)session.getAttribute("roleCd")).equals("ROL0000003"))) {
			return null;
		}
		//End : 유저 필터링
		
		//업데이트
		for (String wlogId : wlogList) {
			managerService.deleteWlog(wlogId);
		}
		
		Map<String, Object> response = new HashMap<>();
		return response;
	}
	
	//	개인정보 조회 : 업데이트 후 리다이렉팅
	@PostMapping("/mypage/update_info")
	@ResponseBody
	public Map<String, Object> mypageUpdateInfo(
			HttpSession session
			, @RequestParam(required = false) String inputPassword
			, @RequestParam(required = false) String confirmPassword
			, @RequestParam String inputTel
			, @RequestParam String inputName
			) {
		//유저 필터링
		if(session.getAttribute("roleCd")== null || (!((String)session.getAttribute("roleCd")).equals("ROL0000003"))) {
			return null;
		}
		//End : 유저 필터링
		
		Map<String, Object> response = new HashMap<>();
		
		//주소를 통한 접근 차단
		if(!(boolean) session.getAttribute("isConfirmed")) {
			response.put("message","유효하지 않은 접근입니다.");
			return response;
		}
		
		String mngrId = (String) session.getAttribute("mngrId");
		ManagerVO updateManager = adminService.getManager(mngrId);
		
		if(inputPassword!=null && !inputPassword.equals("") && inputPassword.equals(confirmPassword)) {
			updateManager.setUserPwd(inputPassword);
		}
		
		if(inputTel != null && !inputTel.equals("") ) {
			updateManager.setMngrTel(inputTel);
		}
		
		if(inputName != null && !inputName.equals("") ) {
			updateManager.setMngrNm(inputName);
		}
		
		managerService.updateManagerInfo(updateManager);
		
		response.put("thisManager", adminService.getManager(mngrId));
		response.put("message","변경되었습니다.");
		return response;
	}
	
	//출퇴근 기록의 출석 상태 코드 반환
	@GetMapping("/main/wlog_cd")
	@ResponseBody
	public Map<String, Object> getWlogCd(
			HttpSession session
			) {
		
		//유저 필터링
		if(session.getAttribute("roleCd")== null || (!((String)session.getAttribute("roleCd")).equals("ROL0000003"))) {
			return null;
		}
		//End : 유저 필터링
		
		List<CommonCodeVO> wlogCdList= managerService.getCodeNameList("WOK");
		
		// End : 업데이트
		Map<String, Object> response = new HashMap<>();
		response.put("wlogCdList", wlogCdList);
		return response;
	}
	
	//연도에 해당하는 교육과정 목록 반환
	@GetMapping("/get_class_list")
	@ResponseBody
	public Map<String, Object> getClassList(
			HttpSession session
			, @RequestParam(required = false) Integer year
			) {
		//유저 필터링
		if(session.getAttribute("roleCd")== null || (!((String)session.getAttribute("roleCd")).equals("ROL0000003"))) {
			return null;
		}
		//End : 유저 필터링
		String mngrId = (String)(session.getAttribute("mngrId"));
		
		//교육과정 목록 반환
		List<ClassVO> classList= managerService.getClassListByMngrId(mngrId, year);
		
		//response 생성 및 목록 추가
		Map<String, Object> response = new HashMap<>();
		response.put("classList", classList);
		return response;
	}
	//지원금 관리(입력)의 학생 목록 반환
	@GetMapping("/subsidy/get_student_list")
	@ResponseBody
	public Map<String, Object> getStudentList(
			HttpSession session
			, @RequestParam String classId
			, @RequestParam String startDate
			, @RequestParam String endDate
			) {
		//유저 필터링
		if(session.getAttribute("roleCd")== null || (!((String)session.getAttribute("roleCd")).equals("ROL0000003"))) {
			return null;
		}
		
		//End : 유저 필터링
		List<StudentInfoDTO> stdtList = managerService.getStudentListByOnlyClssId(classId);
		List<CommonCodeVO> wlogCodeNameList = managerService.getCodeNameList("WOK");
		List<CommonCodeVO> sbsdCdList = managerService.getCodeNameList("SSD");
		for (StudentInfoDTO dto : stdtList) {
			// 출결 가져오기
			dto.setWlogCnt("");
			for (CommonCodeVO cmcd : wlogCodeNameList) {
				dto.appendWlogCnt(String.valueOf(cmcd.getCmcdId() + managerService.getCountByClssIdWlogCdStdtId(classId, cmcd.getCmcdId(), dto.getStdtId(), startDate, endDate)));
				dto.appendWlogCnt(",");
			}	
		}
		//response 생성 및 목록 추가
		Map<String, Object> response = new HashMap<>();
		response.put("stdtList", stdtList);
		response.put("sbsdCdList", sbsdCdList);
		return response;
	}
}