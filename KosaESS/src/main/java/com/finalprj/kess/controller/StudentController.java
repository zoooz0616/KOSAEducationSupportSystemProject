package com.finalprj.kess.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.finalprj.kess.dto.ApplyDetailDTO;
import com.finalprj.kess.dto.CurriculumDetailDTO;
import com.finalprj.kess.model.ApplyVO;
import com.finalprj.kess.model.ClassVO;
import com.finalprj.kess.model.FileVO;
import com.finalprj.kess.model.LoginVO;
import com.finalprj.kess.model.PostVO;
import com.finalprj.kess.model.ReasonVO;
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
		String stdtId = (String) session.getAttribute("stdtId");
		model.addAttribute("student", student);

		List<PostVO> postList = new ArrayList<PostVO>();
		postList = studentService.selectNoticeMain();
		model.addAttribute("postList", postList);

		List<ClassVO> classList = new ArrayList<ClassVO>();
		classList = studentService.selectClassMain();
		model.addAttribute("classList", classList);

		int aplyClassCnt = studentService.getAplyClass(stdtId);
		int cmptClassCnt = studentService.getCmptClass(stdtId);
		ClassVO ingClassVO = studentService.getIngClass(stdtId);
		Timestamp lastLogTime = studentService.getlastLogTime(stdtId);

		if (ingClassVO != null) {
			String clssId = ingClassVO.getClssId();
			String lastWlogId = studentService.getLastWlogId(stdtId, clssId);
			WorklogVO lastWlogVO = studentService.getLastWlogVO(lastWlogId);

			model.addAttribute("lastWlogVO", lastWlogVO);
		}
		model.addAttribute("aplyClassCnt", aplyClassCnt);
		model.addAttribute("cmptClassCnt", cmptClassCnt);
		model.addAttribute("ingClassVO", ingClassVO);
		model.addAttribute("lastLogTime", lastLogTime);

		return "student/main";
	}

	@GetMapping("/events")
	@ResponseBody
	public Map<String, Object> getEvents() {
		// 이벤트 데이터를 DB에서 가져오는 서비스 메서드 호출
		List<ClassVO> events = new ArrayList<ClassVO>();
		events = studentService.getAllEvents();
		List<ClassVO> clss1Events = new ArrayList<>();
		List<ClassVO> clss2Events = new ArrayList<>();

		// clssCd 별로 이벤트 분류
		for (ClassVO event : events) {
			if ("CLS0000001".equals(event.getClssCd())) {
				clss1Events.add(event);
			} else if ("CLS0000002".equals(event.getClssCd())) {
				clss2Events.add(event);
			}
		}

		Map<String, Object> eventsData = new HashMap<String, Object>();
		eventsData.put("exp", clss1Events);
		eventsData.put("Ing", clss2Events);
		return eventsData;
	}

	@GetMapping("/stdtevents")
	@ResponseBody
	public Map<String, Object> getstdtEvents(HttpSession session) {
		// 이벤트 데이터를 DB에서 가져오는 서비스 메서드 호출
		String stdtId = (String) session.getAttribute("stdtId");

		Map<String, Object> eventsData = new HashMap<String, Object>();
		eventsData.put("rgst", studentService.getStdtRgstEvents(stdtId));
		eventsData.put("aply", studentService.getStdtAplyEvents(stdtId));
		return eventsData;
	}

	// 출퇴근 토글 출근!!
	/**
	 * @author : dabin
	 * @date : 2023. 9. 16.
	 * @parameter : model
	 * @return :
	 * @throws ParseException
	 */
	@PostMapping("/wlog/inlog")
	@ResponseBody
	public WorklogVO insertWlog(@RequestParam("clssId") String clssId, HttpSession session) throws ParseException {
		ClassVO clssVO = studentService.getWlogClass(clssId);
		String stdtId = (String) session.getAttribute("stdtId");
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

		Timestamp newInTm = new Timestamp(System.currentTimeMillis()); // 출근시간
		Date newInTmDd = new Date(newInTm.getTime()); // 출근시간
		String newInTime = sdf.format(newInTm); //

		// System.out.println("수업 시작시간TM" + newInTm);
		// System.out.println("수업 시작시간STR" + newInTime);
		// System.out.println("수업 날짜 DATE" + newInTmDd);

		Timestamp clssInTm = clssVO.getSetInTm(); // 시작시간
		Date clssStatrDd = clssVO.getClssStartDd(); // 시작날짜
		String clssInTime = sdf.format(clssInTm);
		String wlogCd = null;

		// System.out.println("수업 시작시간TM" + clssInTm);
		// System.out.println("수업 시작시간STR" + clssInTime);
		// System.out.println("수업 날짜 DATE" + clssStatrDd);

		WorklogVO InWlogVO = new WorklogVO();
		String maxWlogId = studentService.getMaxWlogId();
		int wlogIdCnt = studentService.getWlogIdCnt(stdtId, clssId); // 그 전 출퇴근기록
		if (wlogIdCnt == 0) {
			long diffSec = (newInTmDd.getTime() - clssStatrDd.getTime()) / 1000;
			long diffDays = diffSec / (24 * 60 * 60); // 일자수 차이

			// System.out.println("차이 초" + diffSec);
			// System.out.println("차이 일수" + diffDays);

			if (diffDays > 0) { // 시작일과 1일 이상 차이나는 경우
				Calendar cal1 = Calendar.getInstance();
				Calendar cal2 = Calendar.getInstance();

				// Calendar 타입으로 변경 add()메소드로 1일씩 추가해 주기위해 변경
				cal1.setTime(clssStatrDd);
				cal2.setTime(newInTmDd);
				cal2.add(Calendar.DATE, -1);
				if (!cal1.equals(cal2)) {
					// 시작날짜와 끝 날짜를 비교해, 시작날짜가 작은경우 출력
					while (cal1.compareTo(cal2) != 1) {

						Timestamp pastDd = new Timestamp(cal1.getTimeInMillis());
						maxWlogId = studentService.getMaxWlogId();
						WorklogVO pastwlogVO = new WorklogVO();
						pastwlogVO.setWlogId(maxWlogId);
						pastwlogVO.setClssId(clssId);
						pastwlogVO.setStdtId(stdtId);
						pastwlogVO.setInTm(pastDd);
						pastwlogVO.setWlogCd("WOK0000004");
						pastwlogVO.setRgsterId(stdtId);
						pastwlogVO.setWlogTotalTm(0);
						studentService.insertPastWlog(pastwlogVO);

						// 시작날짜 + 1 일
						cal1.add(Calendar.DATE, 1);

						if (cal1.compareTo(cal2) == 0)
							break;
					}
				}
				if (clssInTime.compareTo(newInTime) < 0) {
					wlogCd = "WOK0000002";
				} else {
					wlogCd = "WOK0000001";
				}
				maxWlogId = studentService.getMaxWlogId();
				InWlogVO = new WorklogVO();
				InWlogVO.setWlogId(maxWlogId);
				InWlogVO.setClssId(clssId);
				InWlogVO.setStdtId(stdtId);
				InWlogVO.setInTm(newInTm);
				InWlogVO.setWlogCd(wlogCd);
				InWlogVO.setRgsterId(stdtId);
				studentService.insertNewWlog(InWlogVO);
			} else {
				if (clssInTime.compareTo(newInTime) < 0) {
					wlogCd = "WOK0000002";
				} else {
					wlogCd = "WOK0000001";
				}
				maxWlogId = studentService.getMaxWlogId();
				InWlogVO = new WorklogVO();
				InWlogVO.setWlogId(maxWlogId);
				InWlogVO.setClssId(clssId);
				InWlogVO.setStdtId(stdtId);
				InWlogVO.setInTm(newInTm);
				InWlogVO.setWlogCd(wlogCd);
				InWlogVO.setRgsterId(stdtId);
				studentService.insertNewWlog(InWlogVO);
			}
		} else { // 이전 출석 로그가 있는 경우
			String lastWlogId = studentService.getLastWlogId(stdtId, clssId);
			WorklogVO lastWlogVO = studentService.getLastWlogVO(lastWlogId);
			Timestamp lastDd = lastWlogVO.getInTm();
			Date lastDate = new Date(lastDd.getTime());
			System.out.println("이전 날짜TM" + lastDd);
			System.out.println("이전 날짜 DATE" + lastDate);

			long diffSec = (newInTmDd.getTime() - lastDate.getTime()) / 1000;
			long diffDays = diffSec / (24 * 60 * 60); // 일자수 차이

			// System.out.println("차이 초" + diffSec);
			System.out.println("차이 일수" + diffDays);
			if (diffDays > 1) { // 이전 출석 로그와 2일 이상 차이나는 경우
				Calendar cal1 = Calendar.getInstance();
				Calendar cal2 = Calendar.getInstance();

				cal1.setTime(lastDate);
				cal2.setTime(newInTmDd);
				cal2.add(Calendar.DATE, -1);
				if (!cal1.equals(cal2)) {
					// 시작날짜와 끝 날짜를 비교해, 시작날짜가 작은경우 출력
					cal1.add(Calendar.DATE, 1);
					while (cal1.compareTo(cal2) != 1) {
						Timestamp pastDd = new Timestamp(cal1.getTimeInMillis());
						maxWlogId = studentService.getMaxWlogId();
						WorklogVO pastwlogVO = new WorklogVO();
						pastwlogVO.setWlogId(maxWlogId);
						pastwlogVO.setClssId(clssId);
						pastwlogVO.setStdtId(stdtId);
						pastwlogVO.setInTm(pastDd);
						pastwlogVO.setWlogCd("WOK0000004");
						pastwlogVO.setRgsterId(stdtId);
						pastwlogVO.setWlogTotalTm(0);
						studentService.insertPastWlog(pastwlogVO);

						// 시작날짜 + 1 일
						cal1.add(Calendar.DATE, 1);
					}
				}
				if (clssInTime.compareTo(newInTime) < 0) {
					wlogCd = "WOK0000002";
				} else {
					wlogCd = "WOK0000001";
				}
				maxWlogId = studentService.getMaxWlogId();
				InWlogVO = new WorklogVO();
				InWlogVO.setWlogId(maxWlogId);
				InWlogVO.setClssId(clssId);
				InWlogVO.setStdtId(stdtId);
				InWlogVO.setInTm(newInTm);
				InWlogVO.setWlogCd(wlogCd);
				InWlogVO.setRgsterId(stdtId);
				studentService.insertNewWlog(InWlogVO);

			} else {
				if (clssInTime.compareTo(newInTime) < 0) {
					wlogCd = "WOK0000002";
				} else {
					wlogCd = "WOK0000001";
				}
				maxWlogId = studentService.getMaxWlogId();
				InWlogVO = new WorklogVO();
				InWlogVO.setWlogId(maxWlogId);
				InWlogVO.setClssId(clssId);
				InWlogVO.setStdtId(stdtId);
				InWlogVO.setInTm(newInTm);
				InWlogVO.setWlogCd(wlogCd);
				InWlogVO.setRgsterId(stdtId);
				studentService.insertNewWlog(InWlogVO);
			}
		}
		return InWlogVO;
	}

	// 출퇴근 토글 퇴근!!
	/**
	 * @author : dabin
	 * @date : 2023. 9. 16.
	 * @parameter : model
	 * @return :
	 * @throws ParseException
	 */
	@PostMapping("/wlog/outlog")
	@ResponseBody
	public WorklogVO updateNewWlog(@RequestParam("clssId") String clssId, HttpSession session) throws ParseException {
		ClassVO clssVO = studentService.getWlogClass(clssId);
		String stdtId = (String) session.getAttribute("stdtId");
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		SimpleDateFormat sdfd = new SimpleDateFormat("yyyy-MM-dd");

		Timestamp clssInTm = clssVO.getSetInTm(); // 수업시작시간
		String clssInTime = sdf.format(clssInTm);
		java.util.Date clssInTimeDd = sdf.parse(clssInTime);

		Timestamp clssOutTm = clssVO.getSetOutTm(); // 수업종료시간
		String clssOutTime = sdf.format(clssOutTm);
		java.util.Date clssOutTimeDd = sdf.parse(clssOutTime);

		// System.out.println("수업 종료시간TM" + clssOutTm);
		// System.out.println("수업 종료시간STR" + clssOutTime);

		Timestamp newOutTm = new Timestamp(System.currentTimeMillis()); // 새로운퇴근시간
		Date newOutTmDd = new Date(newOutTm.getTime());
		// System.out.println(newOutTmDd);
		String newOutTime = sdf.format(newOutTm); //
		java.util.Date newOutTimeDd = sdf.parse(newOutTime);

		String lastWlogId = studentService.getLastWlogId(stdtId, clssId);
		WorklogVO lastWlogVO = studentService.getLastWlogVO(lastWlogId); // 그전 출근VO
		String outlogCd = null;
		String inlogCd = lastWlogVO.getWlogCd();
		Timestamp inlogTm = lastWlogVO.getInTm();
		Date inlogTmDd = new Date(inlogTm.getTime());
		String inlogTime = sdf.format(inlogTm); //
		java.util.Date inlogTimeDd = sdf.parse(inlogTime);

		Double totalTm = 0.0;
		Double standardTm = (double) ((clssOutTimeDd.getTime() - clssInTimeDd.getTime()) / 2000 / (60 * 60));
		System.out.println(inlogTmDd);
		System.out.println(newOutTmDd);
		boolean areDatesEqual = sdfd.format(inlogTmDd).equals(sdfd.format(newOutTmDd));
		if (areDatesEqual) {
			if (clssOutTime.compareTo(newOutTime) < 0) {
				outlogCd = ("WOK0000001");
				if (inlogCd.equals("WOK0000001")) { // 수업마무리시간 - 수업시작시간
					Double diffSec = (double) ((clssOutTimeDd.getTime() - clssInTimeDd.getTime()) / 1000);
					Double diffHours = diffSec / (60 * 60); // 시간 차이
					System.out.println("차이 초" + diffSec);
					if (diffHours < 0)
						totalTm = 0.0;
					else
						totalTm = diffHours;
					if (totalTm < standardTm)
						outlogCd = ("WOK0000004");

				} else if (inlogCd.equals("WOK0000002")) { // 수업마무리시간 - 출근시간
					Double diffSec = (double) ((clssOutTimeDd.getTime() - inlogTimeDd.getTime()) / 1000);
					Double diffHours = diffSec / (60 * 60); // 시간 차이
					System.out.println("차이 초" + diffSec);
					if (diffHours < 0)
						totalTm = 0.0;
					else
						totalTm = diffHours;
					if (totalTm < standardTm)
						outlogCd = ("WOK0000004");
					else
						outlogCd = ("WOK0000002");
				}

			} else {
				outlogCd = ("WOK0000003");
				if (inlogCd.equals("WOK0000001")) { // 퇴근시간 - 수업시작시간
					Double diffSec = (double) ((newOutTimeDd.getTime() - clssInTimeDd.getTime()) / 1000);
					Double diffHours = diffSec / (60 * 60); // 시간 차이
					System.out.println("차이 초" + diffSec);
					if (diffHours < 0)
						totalTm = 0.0;
					else
						totalTm = diffHours;
					if (totalTm < standardTm)
						outlogCd = "WOK0000004";

				} else if (inlogCd.equals("WOK0000002")) { // 퇴근시간 - 출근시간
					Double diffSec = (double) ((newOutTimeDd.getTime() - inlogTimeDd.getTime()) / 1000);
					Double diffHours = diffSec / (60 * 60); // 시간 차이
					System.out.println("차이 초" + diffSec);
					if (diffHours < 0)
						totalTm = 0.0;
					else
						totalTm = diffHours;
					if (totalTm < standardTm)
						outlogCd = ("WOK0000004");
					else
						outlogCd = ("WOK0000002");
				}

			}
		} else {
			outlogCd = ("WOK0000004");
		}
		studentService.getUpdateOutlog(newOutTm, outlogCd, lastWlogId, totalTm);
		WorklogVO OutWlogVO = studentService.getLastWlogVO(lastWlogId);
		return OutWlogVO;
	}

	// 공지사항 리스트확인

	/**
	 * @author : dabin
	 * @date : 2023. 8. 28.
	 * @parameter : model
	 * @return :
	 */

	@GetMapping("/notice/list")
	public String noticeList(Model model) {
		List<PostVO> postList = new ArrayList<PostVO>();
		postList = studentService.selectAllNotice();
		model.addAttribute("postList", postList);

		return "student/notice_list";
	}

	// 공지사항 검색 버튼
	/**
	 * @author : dabin
	 * @date : 2023. 9 .8
	 * @parameter : session, model
	 * @return :
	 */

	@GetMapping("/notice/search")
	@ResponseBody
	public List<PostVO> searchNotices(@RequestParam("keyword") String keyword, Model model) {
		// 검색어(keyword)를 사용하여 교육 과정을 검색하고 결과를 모델에 담습니다.
		List<PostVO> searchResults = studentService.searchNotices(keyword);
		model.addAttribute("noticesList", searchResults);
		return searchResults;
	}

	// 공지사항 조회수 상승

	/**
	 * @author : dabin
	 * @date : 2023. 9.20.
	 * @parameter : model
	 * @return :
	 */

	@PostMapping("/incrementHit")
	@ResponseBody
	public void incrementHit(@RequestParam("postId") String postId) {
		studentService.incrementHit(postId);
	}

	// 공지사항 상세화면

	/**
	 * @author : dabin
	 * @date : 2023. 9.20.
	 * @parameter : model
	 * @return :
	 */

	@GetMapping("/notice/view/{postId}")
	public String noticeDetail(@PathVariable String postId, Model model) {
		PostVO noticeDetail = studentService.selectNotice(postId);
		model.addAttribute("noticeDetail", noticeDetail);

		List<FileVO> noticeFileList = studentService.selectAllNoticeFile(postId);
		model.addAttribute("noticeFileList", noticeFileList);

		return "student/notice_detail";
	}

	// 문의사항 리스트확인
	/**
	 * @author : dabin
	 * @date : 2023. 8. 28.
	 * @parameter : model
	 * @return :
	 */
	@GetMapping("/inquiry/list")
	public String inquiryList(Model model) {
		List<PostVO> postList = new ArrayList<PostVO>();
		postList = studentService.selectAllInquiry();
		model.addAttribute("postList", postList);

		return "student/inquiry_list";
	}

	// 문의사항 검색 버튼
	/**
	 * @author : dabin
	 * @date : 2023. 9 .8
	 * @parameter : session, model
	 * @return :
	 */

	@GetMapping("/inquiry/search")
	@ResponseBody
	public List<PostVO> searchInquiries(@RequestParam("keyword") String keyword, Model model) {
		// 검색어(keyword)를 사용하여 교육 과정을 검색하고 결과를 모델에 담습니다.
		List<PostVO> searchResults = studentService.searchInquiries(keyword);
		model.addAttribute("inquiriesList", searchResults);
		return searchResults;
	}

	/**
	 * @author : dabin
	 * @return
	 * @date : 2023. 9.20.
	 * @parameter : model
	 * @return :
	 */

	@PostMapping("/inquiry/writeform")
	@ResponseBody
	public String InquiryWrite(HttpSession session) {
		String stdtId = (String) session.getAttribute("stdtId");
		return stdtId;
	}

	@GetMapping("/inquiry/writegoform")
	public String goInquiryWrite() {
		return "student/inquiry_write";
	}

	// 문의사항 글쓰기

	/**
	 * @author : dabin
	 * @date : 2023. 9.20.
	 * @parameter : model
	 * @return :
	 */

	@PostMapping("/inquiry/write")
	@ResponseBody
	public void write(@RequestParam("file") MultipartFile file, @RequestParam("title") String title,
			@RequestParam("content") String content, HttpSession session, Model model) throws IOException {

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

		String maxPostId = studentService.getMaxPostId();
		PostVO post = new PostVO();
		post.setPostId(maxPostId);
		post.setPostTitle(title);
		post.setPostContent(content);
		post.setPostCd("PST0000003");
		post.setFileId(maxFileId);
		post.setRgsterId(stdtId);
		studentService.uploadInquiry(post);

	}

	// 문의사항 상세화면

	/**
	 * @author : dabin
	 * @date : 2023. 9.20.
	 * @parameter : model
	 * @return :
	 */

	@GetMapping("/inquiry/view/{postId}")
	public String inquiryDetail(@PathVariable String postId, Model model, HttpSession session) {
		PostVO inquiryDetail = studentService.selectInquiry(postId);
		model.addAttribute("inquiryDetail", inquiryDetail);

		List<FileVO> inquiryFileList = studentService.selectAllInquiryFile(postId);
		model.addAttribute("inquiryFileList", inquiryFileList);

		List<PostVO> replyDetail = studentService.selectReply(postId);
		model.addAttribute("replyDetail", replyDetail);

		String stdtId = (String) session.getAttribute("stdtId");
		model.addAttribute("stdtId", stdtId);

		return "student/inquiry_detail";
	}

	// 문의사항 업데이트 폼 이동
	/**
	 * @author : dabin
	 * @return
	 * @date : 2023. 9 .13
	 * @parameter : session, model
	 * @return :
	 */

	@GetMapping("/inquiry/updateform/{postId}")
	public String updateform(@PathVariable String postId, Model model, HttpSession session) {

		PostVO inquiryDetail = studentService.selectInquiry(postId);
		model.addAttribute("inquiryDetail", inquiryDetail);

		String stdtId = (String) session.getAttribute("stdtId");
		model.addAttribute("stdtId", stdtId);

		List<FileVO> inquiryFileList = studentService.selectAllInquiryFile(postId);
		model.addAttribute("inquiryFileList", inquiryFileList);

		return "student/inquiry_write";
	}

	// 문의사항 업데이트
	@PostMapping("/inquiry/update/{postId}")
	@ResponseBody
	public String updateInquiry(@PathVariable String postId, @RequestParam("updatedTitle") String updatedTitle,
			@RequestParam("updatedContent") String updatedContent,
			@RequestParam(name = "files", required = false) MultipartFile[] files,
			@RequestParam(name = "deleteFiles", required = false) List<String> deleteFiles, HttpSession session,
			RedirectAttributes redirectAttrs) {

		String stdtId = (String) session.getAttribute("stdtId");

		PostVO postVO = studentService.getPostVO(postId);
		postVO.setPostTitle(updatedTitle);
		postVO.setPostContent(updatedContent);
		postVO.setUpdterId(stdtId);

		// 삭제한 파일이 있다면
		if (deleteFiles != null) {
			// 파일삭제
			studentService.deleteFile(postVO.getFileId(), deleteFiles);
			System.out.println("+++++++++++++++++++++" + deleteFiles);
		}

		// fileId가 널이 아닐 때 file개수가 0 이면 fileId null로 변경
		if (postVO.getFileId() != null) {
			int fileCnt = studentService.getFileCnt(postVO.getFileId());
			if (fileCnt == 0) {
				postVO.setFileId(null);
			}
		}

		// 파일을 첨부하지 않았다면 maxFileId는 그대로 fileList는 null.
		String maxFileId = postVO.getFileId();
		List<FileVO> fileList = null;

		// 파일을 첨부했다면 List생성해서 전달
		if (files[0] != null && !files[0].isEmpty()) {
			// 원래 파일이 없다면
			if (maxFileId == null) {
				maxFileId = uploadFileService.getMaxFileId();
				int subFileId = 1;
				fileList = new ArrayList<FileVO>();
				try {
					for (MultipartFile file : files) {
						if (file != null && !file.isEmpty()) {
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
				}
			} else {
				// 원래 파일이 있었다면
				int subFileId = studentService.getmaxSubId(maxFileId);
				fileList = new ArrayList<FileVO>();
				try {
					for (MultipartFile file : files) {
						if (file != null && !file.isEmpty()) {
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
		}
		postVO.setFileId(maxFileId);

		studentService.updatePostVO(fileList, postVO);

		return "student/inquiry/view/{postId}";
	}

	// 문의사항 삭제
	/**
	 * @author : dabin
	 * @date : 2023. 9 .13
	 * @parameter : session, model
	 * @return :
	 */

	@GetMapping("/inquiry/delete/{postId}")
	@ResponseBody
	public void deleteInquiry(@PathVariable String postId, HttpSession session) {
		studentService.deleteInquiry(postId);
	}

	// 교육 리스트확인
	/**
	 * @author : dabin
	 * @date : 2023. 8. 28.
	 * @parameter :model
	 * @return :
	 */

	@GetMapping("/class/list")
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

	@GetMapping("/class/view/{clssId}")
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
	public String mypageMain() {
		return "student/mypage";
	}

	// 마이페이지 개인정보 조회
	/**
	 * @author : dabin
	 * @date : 2023. 8. 31.
	 * @parameter :model
	 * @return :
	 * @throws IOException
	 */
	@PostMapping("/check/password")
	@ResponseBody
	public Map<String, Object> checkPassword(@RequestBody Map<String, String> requestBody, HttpSession session) {
		Map<String, Object> response = new HashMap<>();
		String stdtId = (String) session.getAttribute("stdtId");
		String stdtPwd = (String) studentService.getPassword(stdtId);

		// 데이터베이스에서 사용자 정보 조회
		StudentVO stdtVO = studentService.getstdtInfo(stdtId);

		if (stdtVO != null) {
			String clientPassword = requestBody.get("password"); // 클라이언트로부터 받은 비밀번호

			// 클라이언트로부터 받은 비밀번호와 사용자의 비밀번호를 비교
			if (passwordsMatch(clientPassword, stdtPwd)) {
				// 비밀번호가 일치하는 경우
				response.put("success", true);

				Date birth = stdtVO.getBirthDd();
				String birthDd = new SimpleDateFormat("yyyy-MM-dd").format(birth);
				Timestamp lastLogin = studentService.getlastLogTime(stdtId);
				Timestamp rgstDt = stdtVO.getRgstDt();
				String lastLog = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(lastLogin);
				String rgstDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(rgstDt);

				// 사용자 정보를 응답에 추가
				Map<String, String> user = new HashMap<>();
				user.put("name", stdtVO.getStdtNm());
				user.put("email", stdtVO.getUserEmail());
				user.put("gender", stdtVO.getGenderNm());
				user.put("birth", birthDd);
				user.put("tel", stdtVO.getStdtTel());
				user.put("job", stdtVO.getJobNm());
				user.put("rgstDt", rgstDate);
				user.put("LastLogin", lastLog);

				response.put("user", user);
			} else {
				// 비밀번호가 일치하지 않는 경우
				response.put("success", false);
			}
		} else {
			// 사용자 정보를 찾을 수 없는 경우
			response.put("success", false);
		}

		return response;
	}

	// 비밀번호 비교 함수
	private boolean passwordsMatch(String clientPassword, String stdtPwd) {
		return clientPassword.equals(stdtPwd);
	}

	// 마이페이지 개인정보 수정
	/**
	 * @author : dabin
	 * @date : 2023. 8. 31.
	 * @parameter :model
	 * @return :
	 * @throws IOException
	 */
	@PostMapping("/updateUserInfo")
	@ResponseBody
	public Map<String, Object> updateUserInfo(@RequestBody Map<String, String> updatedUserInfo) {
		Map<String, Object> response = new HashMap<>();

		response.put("success", true);

		return response;
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

	// 마이페이지 출퇴근 내역 조회
	/**
	 * @author : dabin
	 * @date : 2023. 9 .13
	 * @parameter : session, model
	 * @return :
	 */

	@PostMapping("/mypage/wlogList")
	@ResponseBody
	public List<WorklogVO> searchWlogList(HttpSession session, Model model) {
		String stdtId = (String) session.getAttribute("stdtId");
		List<WorklogVO> wlogList = studentService.searchWlogList(stdtId);
		model.addAttribute("wlogList", wlogList);
		return wlogList;
	}

	// 마이페이지 이수 내역 조회
	/**
	 * @author : dabin
	 * @date : 2023. 9 .13
	 * @parameter : session, model
	 * @return :
	 */

	@PostMapping("/mypage/rgstList")
	@ResponseBody
	public List<RegistrationVO> searchRgstList(HttpSession session, Model model) {
		String stdtId = (String) session.getAttribute("stdtId");
		List<RegistrationVO> rgstList = studentService.searchRgstList(stdtId);
		model.addAttribute("rgstList", rgstList);
		return rgstList;
	}

	// 마이페이지 지원내역 조회
	/**
	 * @author : dabin
	 * @date : 2023. 9 .13
	 * @parameter : session, model
	 * @return :
	 */

	@PostMapping("/mypage/aplyList")
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

	// 마이페이지 사유서 제출
	/**
	 * @author : dabin
	 * @date : 2023. 8. 31.
	 * @parameter :model
	 * @return :
	 * @throws IOException
	 */

	@PostMapping("/mypage/submitResn/{wlogId}")
	public String insertResnFile(@PathVariable String wlogId, @RequestParam("file") MultipartFile file,
			@RequestParam("resnText") String resnText, HttpSession session, Model model) throws IOException {
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
		String maxResnId = studentService.getMaxResnId();
		ReasonVO resn = new ReasonVO();
		resn.setResnId(maxResnId);
		resn.setWlogId(wlogId);
		resn.setResnContent(resnText);
		resn.setResnCd("RES0000001");
		resn.setFileId(maxFileId);
		resn.setRgsterId(stdtId);
		studentService.uploadResnFile(resn);

		return "redirect:/student/mypage";
	}

	// 마이페이지 사유서 내역 업데이트
	@PostMapping("/mypage/uploadResn/{resnId}")
	public String updateResnFile(@PathVariable String resnId, @RequestParam("file") MultipartFile file,
			@RequestParam("resnText") String resnText, HttpSession session) throws IOException {
		String stdtId = (String) session.getAttribute("stdtId");

		FileVO fileVO = new FileVO();
		fileVO.setFileNm(file.getOriginalFilename());
		fileVO.setFileSize(file.getSize());
		fileVO.setFileType(file.getContentType());
		fileVO.setFileContent(file.getBytes());
		studentService.updateResnFile(resnId, fileVO);
		studentService.updateResndt(resnId, stdtId, resnText);

		return "redirect:/student/mypage";
	}

	@PostMapping("/show/reply")
	@ResponseBody
	public List<Map<String, Object>> getReply(@RequestParam("postId") String postId) {
		List<Map<String, Object>> responseList = new ArrayList<>(); // 리스트 생성

		// postId를 사용하여 답변 정보 가져오기 (studentService.getReply(postId) 호출)
		List<PostVO> replyList = studentService.getReply(postId);
		String content = studentService.getContent(postId);

		for (PostVO replyVO : replyList) {
			Map<String, Object> response = new HashMap<>(); // 각각의 답변 정보를 담을 Map 생성
			Date rgstDate = replyVO.getRgstDd();
			String rgstDd = new SimpleDateFormat("yyyy-MM-dd").format(rgstDate);

			Map<String, String> inquiry = new HashMap<>();
			inquiry.put("Postcontent", content);
			inquiry.put("id", replyVO.getPostId());
			inquiry.put("title", replyVO.getPostTitle());
			inquiry.put("replyContent", replyVO.getPostContent());
			inquiry.put("name", replyVO.getMngrNm());
			inquiry.put("date", rgstDd);

			response.put("inquiry", inquiry);
			responseList.add(response); // 답변 정보를 리스트에 추가
		}

		return responseList;
	}

	// 마이페이지 문의 내역 조회
	/**
	 * @author : dabin
	 * @date : 2023. 9 .13
	 * @parameter : session, model
	 * @return :
	 */

	@PostMapping("/mypage/postList")
	@ResponseBody
	public List<PostVO> searchPostList(HttpSession session, Model model) {
		String stdtId = (String) session.getAttribute("stdtId");
		List<PostVO> postList = studentService.searchPostList(stdtId);
		model.addAttribute("postList", postList);
		return postList;
	}

}
