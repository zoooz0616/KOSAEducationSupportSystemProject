package com.finalprj.kess.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.Console;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
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
import com.finalprj.kess.model.RegistrationVO;
import com.finalprj.kess.model.StudentVO;
import com.finalprj.kess.model.WorklogVO;
import com.finalprj.kess.service.IStudentService;
import com.finalprj.kess.service.IUploadFileService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/student")
public class StudentController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

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

		int aplyClassCnt = studentService.getAplyClass((String) session.getAttribute("stdtId"));
		int cmptClassCnt = studentService.getCmptClass((String) session.getAttribute("stdtId"));
		ClassVO ingClassVO = studentService.getIngClass((String) session.getAttribute("stdtId"));

		model.addAttribute("aplyClassCnt", aplyClassCnt);
		model.addAttribute("cmptClassCnt", cmptClassCnt);
		model.addAttribute("ingClassVO", ingClassVO);

		return "student/main";
	}

	// 출퇴근 토글
	/**
	 * @author : dabin
	 * @date : 2023. 9. 16.
	 * @parameter : model
	 * @return :
	 * @throws ParseException
	 */
	@SuppressWarnings("null")
	@GetMapping("/wlog/inlog")
	public WorklogVO insertWlog(@RequestParam("indtlog") String inlog, @RequestParam("classVO") ClassVO classVO,
			HttpSession session) throws ParseException {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss");
		Date inlogDt = (Date) sdf.parse(inlog);
		Timestamp inlogTs = new Timestamp(inlogDt.getTime());

		logger.warn(inlog);

		SimpleDateFormat sdfDd = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdfTm = new SimpleDateFormat("HH:mm:ss");

		Date inlogDd = (Date) sdfDd.parse(inlog); // 날짜 부분 파싱
		Date inlogTm = (Date) sdfTm.parse(inlog); // 시간 부분 파싱

		Date classinTm = (Date) sdfTm.parse(classVO.getSetInTime());

		String stdtId = (String) session.getAttribute("stdtId");
		String clssId = classVO.getClssId();
		int wlogIdCnt = studentService.getWlogIdCnt(stdtId, clssId);
		String wlogCd = null;

		WorklogVO newInWlogVO = null;

		if (wlogIdCnt == 0) { // 처음 버튼을 누른 경우
			Date startDd = classVO.getClssStartDd();

			long diffSec = (startDd.getTime() - inlogDd.getTime()) / 1000;
			long diffDays = diffSec / (24 * 60 * 60); // 일자수 차이

			if (diffDays > 1) {
				// 시작일과 1일 이상 차이나는 경우
				Calendar cal1 = Calendar.getInstance();
				Calendar cal2 = Calendar.getInstance();

				/* Calendar 타입으로 변경 add()메소드로 1일씩 추가해 주기위해 변경 */
				cal1.setTime(startDd);
				cal2.setTime(inlogDd);

				/* 시작날짜와 끝 날짜를 비교해, 시작날짜가 작은경우 출력 */

				while (cal1.compareTo(cal2) != 1) {

					/* 출력 */
					Timestamp pastDd = new Timestamp(cal1.getTimeInMillis());
					String maxWlogId = studentService.getMaxWlogId();
					diffSec = (classinTm.getTime() - inlogTm.getTime()) / 1000;
					wlogCd = "WOK0000004";
					inlogTs = pastDd;

					newInWlogVO.setWlogId(maxWlogId);
					newInWlogVO.setClssId(clssId);
					newInWlogVO.setStdtId(stdtId);
					newInWlogVO.setInTm(inlogTs);
					newInWlogVO.setWlogCd(wlogCd);

					newInWlogVO = studentService.insertNewWlog();

					/* 시작날짜 + 1 일 */
					cal1.add(Calendar.DATE, 1);
				}
			} else {
				String maxWlogId = studentService.getMaxWlogId();
				diffSec = (classinTm.getTime() - inlogTm.getTime()) / 1000;
				if (diffSec > 1)
					wlogCd = "WOK0000002";

				else
					wlogCd = "WOK0000001";

				newInWlogVO.setWlogId(maxWlogId);
				newInWlogVO.setClssId(clssId);
				newInWlogVO.setStdtId(stdtId);
				newInWlogVO.setInTm(inlogTs);
				newInWlogVO.setWlogCd(wlogCd);
				newInWlogVO = studentService.insertNewWlog();
			}

		} else { // 이전 출석 로그가 있는 경우
			String lastWlogId = studentService.getLastWlogId(stdtId, clssId);
			WorklogVO lastWlogVO = studentService.getLastWlogVO(lastWlogId);
			Date lastDd = lastWlogVO.getInTmDd();

			long diffSec = (lastDd.getTime() - inlogDd.getTime()) / 1000;
			long diffDays = diffSec / (24 * 60 * 60); // 일자수 차이

			if (diffDays > 2) {
				// 이전 출석 로그와 2일 이상 차이나는 경우
				Calendar cal1 = Calendar.getInstance();
				Calendar cal2 = Calendar.getInstance();

				/* Calendar 타입으로 변경 add()메소드로 1일씩 추가해 주기위해 변경 */
				cal1.setTime(lastDd);
				cal2.setTime(inlogDd);

				/* 시작날짜와 끝 날짜를 비교해, 시작날짜가 작은경우 출력 */

				while (!cal1.equals(cal2)) {

					/* 출력 */
					Timestamp pastDd = new Timestamp(cal1.getTimeInMillis());
					String maxWlogId = studentService.getMaxWlogId();
					diffSec = (classinTm.getTime() - inlogTm.getTime()) / 1000;
					wlogCd = "WOK0000004";
					inlogTs = pastDd;

					newInWlogVO.setWlogId(maxWlogId);
					newInWlogVO.setClssId(clssId);
					newInWlogVO.setStdtId(stdtId);
					newInWlogVO.setInTm(inlogTs);
					newInWlogVO.setWlogCd(wlogCd);
					newInWlogVO = studentService.insertNewWlog();

					/* 시작날짜 + 1 일 */
					cal1.add(Calendar.DATE, 1);
				}
			} else {
				String maxWlogId = studentService.getMaxWlogId();
				diffSec = (classinTm.getTime() - inlogTm.getTime()) / 1000;
				if (diffSec > 1)
					wlogCd = "WOK0000002";

				else
					wlogCd = "WOK0000001";

				newInWlogVO.setWlogId(maxWlogId);
				newInWlogVO.setClssId(clssId);
				newInWlogVO.setStdtId(stdtId);
				newInWlogVO.setInTm(inlogTs);
				newInWlogVO.setWlogCd(wlogCd);
				newInWlogVO = studentService.insertNewWlog();
			}
		}
		return newInWlogVO;
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
		List<FileVO> classFileList = studentService.selectAllClassFile(clssId);
		model.addAttribute("classFileList", classFileList);

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

	@GetMapping("file/{fileId}/{fileSubId}")
	public ResponseEntity<byte[]> getFile(@PathVariable String fileId, @PathVariable String fileSubId,
			HttpSession session) {
		FileVO file = uploadFileService.getFile(fileId, fileSubId);

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

		List<RegistrationVO> rgstList = studentService.searchRgstList(stdtId);
		model.addAttribute("rgstList", rgstList);

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
	public void updateAply(@RequestParam("aplyCd") String aplyCd, @RequestParam("aplyId") String aplyId) {
		studentService.updateaply(aplyCd, aplyId);
	}

	// 마이페이지 지원내역 확정
	/**
	 * @author : dabin
	 * @return
	 * @date : 2023. 9 .13
	 * @parameter : session, model
	 * @return :
	 */

	@PostMapping("/mypage/aplyList/confirm")
	@ResponseBody
	public boolean confirmAply(@RequestParam("aplyCd") String aplyCd, @RequestParam("aplyId") String aplyId,
			HttpSession session) {
		String stdtId = (String) session.getAttribute("stdtId");
		int rgstIngCnt = studentService.getRgstIngCnt(stdtId);
		if (rgstIngCnt == 0) {
			studentService.updateaply(aplyCd, aplyId);
			String maxRgstId = studentService.getMaxRegistrationId();
			studentService.insertRgst(aplyId, maxRgstId, stdtId);
			return true;
		} else {
			return false;
		}
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
	public String updateAplyFile(@PathVariable String aplyId, @RequestParam("formData") MultipartFile file,
			HttpSession session) throws IOException {
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
