package com.finalprj.kess.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.finalprj.kess.dto.ApplyDetailDTO;
import com.finalprj.kess.dto.ClassInsertDTO;
import com.finalprj.kess.dto.CurriculumDetailDTO;
import com.finalprj.kess.dto.LectureListDTO;
import com.finalprj.kess.dto.RegistrationDTO;
import com.finalprj.kess.dto.SubsidyDTO;
import com.finalprj.kess.model.ApplyVO;
import com.finalprj.kess.model.ClassVO;
import com.finalprj.kess.model.CommonCodeVO;
import com.finalprj.kess.model.CompanyVO;
import com.finalprj.kess.model.CurriculumVO;
import com.finalprj.kess.model.FileVO;
import com.finalprj.kess.model.LectureVO;
import com.finalprj.kess.model.ManagerVO;
import com.finalprj.kess.model.PostVO;
import com.finalprj.kess.model.ProfessorVO;
import com.finalprj.kess.model.RegistrationVO;
import com.finalprj.kess.model.StudentVO;
import com.finalprj.kess.model.SubjectVO;
import com.finalprj.kess.model.SubsidyVO;
import com.finalprj.kess.service.IAdminService;
import com.finalprj.kess.service.IMailService;
import com.finalprj.kess.service.IManagerService;
import com.finalprj.kess.service.IUploadFileService;
import com.itextpdf.text.pdf.PdfStructTreeController.returnType;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.websocket.Session;

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

	@Autowired
	IMailService mailService;

	/**
	 * 관리자 대시보드
	 * 
	 * @author : eunji
	 * @date : 2023. 8. 22.
	 * @parameter : session, model
	 * @return : String
	 * @throws Exception
	 */
	@RequestMapping("")
	public String main(HttpSession session, Model model, HttpServletRequest request) throws Exception {
		if (session.getAttribute("mngrId") == null) {
			return "redirect:/login";
		} else {
			if (!(((String) session.getAttribute("roleCd")).equals("ROL0000002"))) {
				// 이동하고자 하는 기본 URL 설정
				String defaultRedirectUrl = "/manager"; // 기본적으로 홈 페이지로 이동

				// 이전 페이지의 URL을 가져오기 시도
				String referer = request.getHeader("Referer");

				// 이전 페이지의 URL이 유효한 경우에만 사용
				if (referer != null && !referer.isEmpty()) {
					return "redirect:" + referer;
				} else {
					return "redirect:" + defaultRedirectUrl;
				}
			}
		}

		// 기업 개수
		List<CompanyVO> companyList = adminService.getCompanyListAll();
		model.addAttribute("companyCnt", companyList.size());

		// 강의 개수
		List<LectureVO> lectureList = adminService.getLectureListAll();
		model.addAttribute("lectureCnt", lectureList.size());

		// 교육생 개수
		List<StudentVO> studentList = adminService.getStudentListAll();
		model.addAttribute("studentCnt", studentList.size());

		// 교육과정 개수
		List<ClassVO> classList = adminService.getClassListAll();
		model.addAttribute("classCnt", classList.size());
		
		// 답변대기 문의사항 10개 가져오기
		List<PostVO> inquiryList = adminService.getWaitInquiryList();
		model.addAttribute("inquiryList", inquiryList);

		// 지급 대기 지원금 10개 가져오기
		List<SubsidyDTO> subsidyList = adminService.getWaitSubsidyList();
		model.addAttribute("subsidyList", subsidyList);
		
		//이수완료
		int completeCnt =  adminService.getCmptCnt("CMP0000002");
		model.addAttribute("completeCnt", completeCnt);
		//미이수
		int uncompleteCnt =adminService.getCmptCnt("CMP0000001");
		model.addAttribute("uncompleteCnt", uncompleteCnt);
		//중도포기
		int giveupCnt = adminService.getCmptCnt("CMP0000003");
		model.addAttribute("giveupCnt", giveupCnt);
		
		//출근해야하는 교육생 수
		int wlogCntTotal = adminService.getWlogCntTotal();
		//출근등록한 교육생 수
		int setInTmCnt = adminService.getWlogCntSetInTm();
		//출근 미등록한 교육생 수
		int unSetInTmCnt = wlogCntTotal - setInTmCnt;
		model.addAttribute("setInTmCnt", setInTmCnt);
		model.addAttribute("unSetInTmCnt", unSetInTmCnt);
		
		
		return "admin/dashboard";
	}

	/**
	 * 공지사항 목록조회
	 * 
	 * @author : eunji
	 * @date : 2023. 9. 14.
	 * @parameter : session, model
	 * @return : String
	 */
	@GetMapping("/notice/list/{page}")
	public String noticeList(@PathVariable int page, HttpSession session, Model model) {
		// 공지사항 상태 리스트 전달
		List<CommonCodeVO> noticeCommonCodeList = adminService.getNoticeCommonCodeList("GRP0000001");
		model.addAttribute("noticeCommonCodeList", noticeCommonCodeList);

		// 공지사항 페이징 리스트 전달
		List<PostVO> noticeList = adminService.getNoticeList(page);
		model.addAttribute("noticeList", noticeList);

		// 공지사항 모든 리스트 전달
		List<PostVO> noticeListAll = adminService.getNoticeListAll();
		session.setAttribute("searchNoticeList", noticeListAll);

		session.setAttribute("page", page);

		int bbsCount = adminService.getNoticeCnt();
		model.addAttribute("noticeCnt", bbsCount);
		int totalPage = 0;
		if (bbsCount > 0) {
			totalPage = (int) Math.ceil(bbsCount / 20.0);
		}

		int totalPageBlock = (int) (Math.ceil(totalPage / 10.0));
		int nowPageBlock = (int) Math.ceil(page / 10.0);
		int startPage = (nowPageBlock - 1) * 10 + 1;
		int endPage = 0;
		if (totalPage > nowPageBlock * 10) {
			endPage = nowPageBlock * 10;
		} else {
			endPage = totalPage;
		}
		model.addAttribute("totalPageCount", totalPage);
		model.addAttribute("nowPage", page);
		model.addAttribute("totalPageBlock", totalPageBlock);
		model.addAttribute("nowPageBlock", nowPageBlock);
		model.addAttribute("startPage", startPage);
		model.addAttribute("endPage", endPage);

		return "admin/notice_list";
	}

	/**
	 * 공지사항 상세조회
	 * 
	 * @author : eunji
	 * @date : 2023. 9. 17.
	 * @parameter :noticeId, model
	 * @return : String
	 */
	@GetMapping("/notice/view/{noticeId}")
	public String noticeDetail(@PathVariable String noticeId, Model model) {
		String act = "select";
		model.addAttribute("act", act);

		PostVO postVO = adminService.getPostVO(noticeId);
		model.addAttribute("postVO", postVO);

		if (postVO.getFileId() != null) {
			List<FileVO> fileList = uploadFileService.getFileList(postVO.getFileId());
			model.addAttribute("fileList", fileList);
		}

		return "admin/notice_form";
	}

	/**
	 * 공지사항 등록 GET
	 * 
	 * @author : eunji
	 * @date : 2023. 9. 17.
	 * @parameter :model
	 * @return : String
	 */
	@GetMapping("/notice/insert")
	public String noticeInsert(Model model) {
		String act = "insert";
		model.addAttribute("act", act);

		List<FileVO> fileList = new ArrayList<FileVO>();
		model.addAttribute("fileList", fileList);

		return "admin/notice_form";
	}

	/**
	 * 공지사항 등록 POST
	 * 
	 * @author : eunji
	 * @date : 2023. 9. 17.
	 * @parameter : files, redirectAttrs, noticeTitle, editorTxt
	 * @return : String
	 */
	@PostMapping("/notice/insert")
	public String noticeCreate(@RequestParam("files") MultipartFile[] files, RedirectAttributes redirectAttrs,
			@RequestParam String noticeTitle, @RequestParam String editorTxt) {

		// 파일을 첨부하지 않았다면 maxFileId와 fileList는 null.
		String maxFileId = null;
		List<FileVO> fileList = null;

		// 파일을 첨부했다면 List생성해서 전달
		if (files[0] != null && !files[0].isEmpty()) {
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
						fileVO.setRgsterId("MNGR000001");
						fileList.add(fileVO);
						subFileId++;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				redirectAttrs.addFlashAttribute("message", e.getMessage());
			}
		}

		// notice 객체 생성

		PostVO postVO = new PostVO();
		postVO.setPostId(adminService.getMaxNoticeId());
		postVO.setPostTitle(noticeTitle);
		postVO.setPostContent(editorTxt);
		postVO.setFileId(maxFileId);
		postVO.setRgsterId("MNGR000001");

		adminService.insertNoticeVO(fileList, postVO);

		return "redirect:/admin/notice/list/1";
	}

	/**
	 * 공지사항 수정 GET
	 * 
	 * @author : eunji
	 * @date : 2023. 9. 17.
	 * @parameter :postId, model
	 * @return : String
	 */
	@GetMapping("/notice/update/{noticeId}")
	public String noticeUpdate(@PathVariable String noticeId, Model model) {
		String act = "update";
		model.addAttribute("act", act);

		PostVO postVO = adminService.getPostVO(noticeId);
		model.addAttribute("postVO", postVO);

		// 공지사항 상태 리스트 전달
		List<CommonCodeVO> noticeCommonCodeList = adminService.getNoticeCommonCodeListByInsert();
		model.addAttribute("noticeCommonCodeList", noticeCommonCodeList);

		List<FileVO> fileList = new ArrayList<FileVO>();

		if (postVO.getFileId() != null) {
			fileList = uploadFileService.getFileList(postVO.getFileId());
		}

		model.addAttribute("fileList", fileList);

		return "admin/notice_form";
	}

	/**
	 * 공지사항 수정 POST
	 * 
	 * @author : eunji
	 * @date : 2023. 9. 18.
	 * @parameter :postId, model
	 * @return : String
	 */
	@PostMapping("/notice/update")
	public String noticeUpdate(@RequestParam(name = "files", required = false) MultipartFile[] files,
			RedirectAttributes redirectAttrs, @RequestParam String noticeTitle, @RequestParam String noticeStatus,
			@RequestParam String editorTxt, @RequestParam String noticeId,
			@RequestParam(name = "deleteFiles", required = false) List<String> deleteFiles) {

		PostVO postVO = adminService.getPostVO(noticeId);
		postVO.setPostTitle(noticeTitle);
		postVO.setPostContent(editorTxt);
		postVO.setPostCd(noticeStatus);

		// 삭제한 파일이 있다면
		if (deleteFiles != null) {
			// 파일삭제
			adminService.deleteFile(postVO.getFileId(), deleteFiles);
			System.out.println("+++++++++++++++++++++" + deleteFiles);
		}

		// fileId가 널이 아닐 때 file개수가 0 이면 fileId null로 변경
		if (postVO.getFileId() != null) {
			int fileCnt = adminService.getFileCnt(postVO.getFileId());
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
							fileVO.setRgsterId("MNGR000001");
							fileList.add(fileVO);
							subFileId++;
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					redirectAttrs.addFlashAttribute("message", e.getMessage());
				}
			} else {
				// 원래 파일이 있었다면
				int subFileId = adminService.getMaxFileSubId(maxFileId);
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
							fileVO.setRgsterId("MNGR000001");
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

		adminService.updateNoticeVO(fileList, postVO);

		return "redirect:/admin/notice/view/" + postVO.getPostId();
	}

	/**
	 * 공지사항 삭제
	 * 
	 * @author : eunji
	 * @date : 2023. 9. 15.
	 * @parameter : selectedNoticeIds
	 * @return : String
	 */
	@PostMapping("/notice/delete")
	@ResponseBody
	public String noticeDeleteAll(@RequestParam("selectedNoticeIds[]") List<String> selectedNoticeIds) {
		// 공지사항 deleteYn='Y'로 업데이트
		System.out.println("qssssssssss");
		adminService.deleteAllNotice(selectedNoticeIds);

		return "success";
	}

	/**
	 * 공지사항 검색
	 * 
	 * @author : eunji
	 * @date : 2023. 9. 18.
	 * @parameter : session, model
	 * @return : String
	 */
	@PostMapping("/notice/search")
	@ResponseBody
	public Map<String, Object> noticeSearch(HttpSession session, @RequestParam String searchInputCategory,
			@RequestParam String searchInput, @RequestParam("postStatusList[]") List<String> postStatusList) {

		Map<String, Object> response = new HashMap<String, Object>();

		// 공지사항 리스트 전달
		List<PostVO> noticeList = adminService.getSearchPostList(searchInputCategory, searchInput.trim(),
				postStatusList);
		session.setAttribute("searchNoticeList", noticeList);

		response.put("noticeList", noticeList);
		return response;
	}

	/**
	 * 문의사항 목록조회
	 * 
	 * @author : eunji
	 * @date : 2023. 9. 17.
	 * @parameter :session, model
	 * @return : String
	 */
	@RequestMapping("/inquiry/list/{page}")
	public String inquiry(@PathVariable int page, HttpSession session, Model model) {
		// 문의사항 상태 리스트 전달
		List<CommonCodeVO> classCommonCodeList = adminService.getInquriyCommonCodeList("GRP0000001");
		model.addAttribute("classCommonCodeList", classCommonCodeList);

		// 문의사항 페이지 리스트 전달
		List<PostVO> inquiryList = adminService.getInquiryList(page);
		model.addAttribute("inquiryList", inquiryList);

		// 문의사항 전체 리스트 전달
		List<PostVO> inquiryListAll = adminService.getInquiryListAll();
		session.setAttribute("searchInquiryList", inquiryListAll);

		int bbsCount = adminService.getInquiryCnt();
		model.addAttribute("inquiryCnt", bbsCount);
		int totalPage = 0;
		if (bbsCount > 0) {
			totalPage = (int) Math.ceil(bbsCount / 20.0);
		}

		int totalPageBlock = (int) (Math.ceil(totalPage / 10.0));
		int nowPageBlock = (int) Math.ceil(page / 10.0);
		int startPage = (nowPageBlock - 1) * 10 + 1;
		int endPage = 0;
		if (totalPage > nowPageBlock * 10) {
			endPage = nowPageBlock * 10;
		} else {
			endPage = totalPage;
		}
		model.addAttribute("totalPageCount", totalPage);
		model.addAttribute("nowPage", page);
		model.addAttribute("totalPageBlock", totalPageBlock);
		model.addAttribute("nowPageBlock", nowPageBlock);
		model.addAttribute("startPage", startPage);
		model.addAttribute("endPage", endPage);

		return "admin/inquiry_list";
	}

	/**
	 * 문의사항 상세조회
	 * 
	 * @author : eunji
	 * @date : 2023. 9. 17.
	 * @parameter :noticeId, model
	 * @return : String
	 */
	@GetMapping("/inquiry/view/{inquiryId}")
	public String inquiryDetail(@PathVariable String inquiryId, Model model) {
		String act = "select";
		model.addAttribute("act", act);

		PostVO postVO = adminService.getPostVO(inquiryId);
		model.addAttribute("postVO", postVO);
		if (postVO.getFileId() != null) {
			List<FileVO> fileList = uploadFileService.getFileList(postVO.getFileId());
			model.addAttribute("fileList", fileList);
		}

		// 답변
		List<PostVO> replyList = adminService.getReplyList(postVO.getPostId());
		model.addAttribute("replyList", replyList);

		return "admin/inquiry_form";
	}

	/**
	 * 문의사항 답변등록
	 * 
	 * @author : eunji
	 * @date : 2023. 9. 18.
	 * @parameter : session, model
	 * @return : String
	 */
	@PostMapping("/inquiry/reply/{postId}")
	public String insertReply(@PathVariable String postId, @RequestParam String replyTitle,
			@RequestParam String replyContent) {
		PostVO postVO = new PostVO();
		postVO.setPostId(adminService.getMaxReplyId());
		postVO.setMasterId(postId);
		postVO.setPostTitle(replyTitle);
		postVO.setPostContent(replyContent);
		postVO.setRgsterId("MNGR000001");

		adminService.insertReplyVO(postVO);
		adminService.updateInquiryStatus(postId);

		return "redirect:/admin/inquiry/view/" + postId;
	}

	/**
	 * 문의사항 선택 삭제
	 * 
	 * @author : eunji
	 * @date : 2023. 9. 15.
	 * @parameter : session, model
	 * @return : String
	 */
	@PostMapping("/inquiry/delete")
	@ResponseBody
	public String inquiryDeleteAll(@RequestParam("selectedInquiryIds[]") List<String> selectedInquiryIds) {
		// 문의사항 deleteYn='Y'로 업데이트
		adminService.deleteAllInquiry(selectedInquiryIds);
		return "success";
	}

	/**
	 * 문의사항 답변 삭제
	 * 
	 * @author : eunji
	 * @date : 2023. 9. 22.
	 * @parameter :
	 * @return : String
	 */
	@PostMapping("/inquiry/reply/delete/{replyId}")
	@ResponseBody
	public String inquiryReplyDelete(@PathVariable String replyId) {
		// 답변 deleteYn='Y'로 업데이트
		adminService.deleteInquiryReply(replyId);
		return "success";
	}

	/**
	 * 문의사항 검색
	 * 
	 * @author : eunji
	 * @date : 2023. 9. 18.
	 * @parameter : session, model
	 * @return : String
	 */
	@PostMapping("/inquiry/search")
	@ResponseBody
	public Map<String, Object> inquirySearch(HttpSession session, @RequestParam String searchInputCategory,
			@RequestParam String searchInput, @RequestParam("postStatusList[]") List<String> postStatusList) {

		Map<String, Object> response = new HashMap<String, Object>();

		// 문의사항 리스트 전달
		List<PostVO> inquiryList = adminService.getSearchPostList(searchInputCategory, searchInput.trim(),
				postStatusList);
		session.setAttribute("searchInquiryList", inquiryList);

		response.put("inquiryList", inquiryList);
		return response;
	}

	/**
	 * 교육과정 목록조회
	 * 
	 * @author : eunji
	 * @date : 2023. 9. 4.
	 * @parameter : session, model
	 * @return : String
	 */
	@RequestMapping("/class/list/{page}")
	public String classList(@PathVariable int page, HttpSession session, Model model) {
		List<CompanyVO> companyList = adminService.getCompanyListAll();
		model.addAttribute("companyList", companyList);

		// 교육상태 리스트
		List<CommonCodeVO> classCommonCodeList = adminService.getCommonCodeList("GRP0000002");
		model.addAttribute("classCommonCodeList", classCommonCodeList);

		// 교육과정 페이지 리스트
		List<ClassVO> classList = adminService.getClassList(page);
		model.addAttribute("classList", classList);

		// 교육과정 전체 리스트
		List<ClassVO> classListAll = adminService.getClassListAll();
		// 엑셀다운로드를 위해 저장해놓기
		session.setAttribute("searchClassList", classListAll);

		session.setAttribute("page", page);

		int bbsCount = adminService.getClassCnt();
		model.addAttribute("classCnt", bbsCount);
		int totalPage = 0;
		if (bbsCount > 0) {
			totalPage = (int) Math.ceil(bbsCount / 20.0);
		}

		int totalPageBlock = (int) (Math.ceil(totalPage / 10.0));
		int nowPageBlock = (int) Math.ceil(page / 10.0);
		int startPage = (nowPageBlock - 1) * 10 + 1;
		int endPage = 0;
		if (totalPage > nowPageBlock * 10) {
			endPage = nowPageBlock * 10;
		} else {
			endPage = totalPage;
		}
		model.addAttribute("totalPageCount", totalPage);
		model.addAttribute("nowPage", page);
		model.addAttribute("totalPageBlock", totalPageBlock);
		model.addAttribute("nowPageBlock", nowPageBlock);
		model.addAttribute("startPage", startPage);
		model.addAttribute("endPage", endPage);

		return "admin/class_list";
	}
	
	/**
	 * 교육과정 조회 팝업
	 * 
	 * @author : eunji
	 * @date : 2023. 9. 6.
	 * @parameter : clssId, session, model
	 * @return : String
	 */
	@RequestMapping("/select/class")
	public String selectClass(Model model) {
		List<ClassVO> classList = adminService.getClassListAll();
		model.addAttribute("classList", classList);
		
		return "admin/select_class_popup";
	}
	
	/**
	 * 교육과정 팝업에서 검색
	 * 
	 * @author : eunji
	 * @date : 2023. 9. 6.
	 * @parameter : clssId, session, model
	 * @return : String
	 */
	@GetMapping("/select/class/search")
	@ResponseBody
	public Map<String, Object> selectSearchClass(@RequestParam String keyword) {
		Map<String, Object> response = new HashMap<String, Object>();
		
		List<ClassVO> classList = adminService.getSearchClassPopup(keyword.trim());
		response.put("classList", classList);
		
		return response;
	}

	/**
	 * 교육과정 상세조회
	 * 
	 * @author : eunji
	 * @date : 2023. 9. 6.
	 * @parameter : clssId, session, model
	 * @return : String
	 */
	@RequestMapping("/class/view/{clssId}")
	public String classDetail(@PathVariable String clssId, HttpSession session, Model model) {
		// title
		String title = "교육과정 상세";
		model.addAttribute("title", title);

		// insert or select or update
		String act = "select";
		model.addAttribute("act", act);

		// 해당 교육과정 객체 가져오기
		ClassVO classVO = adminService.getClass(clssId);
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

		System.out.println("##############################");
		System.out.println(curriculumDetailList);

		model.addAttribute("curriculumDetailList", curriculumDetailList);

		return "admin/class_form";
	}

	/**
	 * 교육과정 생성 GET
	 * 
	 * @author : eunji
	 * @date : 2023. 9. 10.
	 * @parameter : session, model
	 * @return : String
	 */
	@GetMapping("/class/insert")
	public String insertClass(HttpSession session, Model model) {

		// title
		String title = "교육과정 등록";
		model.addAttribute("title", title);

		// insert or select or update
		String act = "insert";
		model.addAttribute("act", act);

		List<FileVO> fileList = new ArrayList<FileVO>();
		model.addAttribute("fileList", fileList);

		// 클래스 하나 생성하고 classId값 지정해서 넘기기
		ClassVO classVO = new ClassVO();
		classVO.setClssId(adminService.getMaxClassId());

		model.addAttribute("classVO", classVO);

		// 교육상태 리스트
		List<CommonCodeVO> classCommonCodeList = adminService.getCommonCodeList("GRP0000002");
		model.addAttribute("classCommonCodeList", classCommonCodeList);

		// 업체 리스트
		List<CompanyVO> companyList = adminService.getCompanyListAll();
		model.addAttribute("companyList", companyList);

		// 업무담당자 리스트
		List<ManagerVO> managerList = adminService.getManagerListAll();
		model.addAttribute("managerList", managerList);

		// 강의 리스트
		List<LectureVO> lectureList = adminService.getLectureListAll();
		model.addAttribute("lectureList", lectureList);

		return "admin/class_form";
	}

	/**
	 * 교육과정 생성 시 기업 선택 팝업 띄우기
	 * 
	 * @author : eunji
	 * @date : 2023. 10. 21.
	 * @parameter : session, model
	 * @return : String
	 */
	@RequestMapping("/class/select/company")
	public String selectCompanyByInsertClass(Model model) {
		// insert때 사용가능한 기업리스트만 보내기
		List<CompanyVO> companyList = adminService.getCompanyListAll();
		model.addAttribute("companyList", companyList);

		return "admin/select_company_popup";
	}

	/**
	 * 교육과정 생성 시 업무담당자 선택 팝업 띄우기
	 * 
	 * @author : eunji
	 * @date : 2023. 10. 22.
	 * @parameter : session, model
	 * @return : String
	 */
	@RequestMapping("/class/select/manager")
	public String selectManagerByInsertClass(Model model) {
		// insert때 사용가능한 기업리스트만 보내기
		List<ManagerVO> managerList = adminService.getManagerListAllByInsert();
		model.addAttribute("managerList", managerList);

		return "admin/select_manager_popup";
	}

	/**
	 * 교육과정 생성 시 강의 선택 팝업 띄우기
	 * 
	 * @author : eunji
	 * @date : 2023. 10. 23.
	 * @parameter : session, model
	 * @return : String
	 */
	@RequestMapping("/class/select/lecture")
	public String selectLectureByInsertClass(Model model) {
		// insert때 사용가능한 강의만 보내기
		// 강의 리스트
		List<LectureVO> lectureList = adminService.getLectureListAll();
		model.addAttribute("lectureList", lectureList);

		return "admin/select_lecture_popup";
	}

	/**
	 * 교육과정 생성 POST
	 * 
	 * @author : eunji
	 * @date : 2023. 9. 11.
	 * @parameter : session, files, redirectAttrs, classInsertDTO, clssCd, cmpyId,
	 *            mngrId, lctrIds
	 * @return : String
	 */
	@PostMapping("/class/insert")
	public String insertClass(HttpSession session, @RequestParam("files") MultipartFile[] files,
			RedirectAttributes redirectAttrs, ClassInsertDTO classInsertDTO, @RequestParam String clssCd,
			@RequestParam(name = "lctrId", required = false) List<String> lctrIds) {

		if (session.getAttribute("mngrId") == null) {
			return "redirect:/login";
		}

		// 파일을 첨부하지 않았다면 maxFileId와 fileList는 null.
		String maxFileId = null;
		List<FileVO> fileList = null;

		// 파일을 첨부했다면 List생성해서 전달
		if (files[0] != null && !files[0].isEmpty()) {
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
						fileVO.setRgsterId("MNGR000001");
						fileList.add(fileVO);
						subFileId++;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				redirectAttrs.addFlashAttribute("message", e.getMessage());
			}
		}

		// 교육과정 생성하기
		ClassVO classVO = new ClassVO();
		classVO.setClssId(classInsertDTO.getClssId());
		classVO.setMngrId(classInsertDTO.getMngrId());
		classVO.setCmpyId(classInsertDTO.getCmpyId());
		classVO.setClssNm(classInsertDTO.getClssNm());
		classVO.setClssContent(classInsertDTO.getClssContent());

		String clssSubsidy = classInsertDTO.getClssSubsidy().replaceAll(",", "");

		classVO.setClssSubsidy(Integer.parseInt(clssSubsidy));
		classVO.setLimitCnt(classInsertDTO.getLimitCnt());

		// Date-time(String) to Timestamp
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

		// Time(String) to Timestamp
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
		// String to Date
		try {
			String clssStartDd = (String) classInsertDTO.getClssStartDd();
			String clssEndDd = (String) classInsertDTO.getClssEndDd();
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
		classVO.setClssEtc((String) classInsertDTO.getClssEtc());
		classVO.setRgsterId((String) session.getAttribute("mngrId"));

		// 커리큘럼을 지정하지 않았다면 null전달
		List<CurriculumVO> curriculumList = null;
		if (lctrIds != null) {
			curriculumList = new ArrayList<CurriculumVO>();
			for (String lctrId : lctrIds) {
				CurriculumVO curriculumVO = new CurriculumVO();
				curriculumVO.setClssId(classVO.getClssId());
				curriculumVO.setLctrId(lctrId);
				curriculumVO.setRgsterId((String) session.getAttribute("mngrId"));
				curriculumList.add(curriculumVO);
			}
		}

		adminService.createClass(fileList, classVO, curriculumList);

		return "redirect:/admin/class/list/1";
	}

	/**
	 * 교육과정 수정 GET
	 * 
	 * @author : eunji
	 * @date : 2023. 9. 12.
	 * @parameter : clssId,session, model
	 * @return : String
	 */
	@GetMapping("/class/update/{clssId}")
	public String updateClass(@PathVariable String clssId, Model model, HttpSession session) {
		// title
		String title = "교육과정 수정";
		model.addAttribute("title", title);

		// insert or select or update
		String act = "update";
		model.addAttribute("act", act);

		// 해당 교육과정 객체 가져오기
		ClassVO classVO = adminService.getClass(clssId);
		DecimalFormat formatter = new DecimalFormat("##,###,###");
		classVO.setSSubsidy(formatter.format(classVO.getClssSubsidy()));
		model.addAttribute("classVO", classVO);

		// 파일 가져오기
		List<FileVO> fileList = new ArrayList<FileVO>();
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

		/*
		 * 선택에 필요한 리스트 넘기기
		 */

		// 교육상태 리스트
		List<CommonCodeVO> classCommonCodeList = adminService.getCommonCodeList("GRP0000002");
		model.addAttribute("classCommonCodeList", classCommonCodeList);

		// 업체 리스트
		List<CompanyVO> companyList = adminService.getCompanyListAll();
		model.addAttribute("companyList", companyList);

		// 업무담당자
		List<ManagerVO> managerList = adminService.getManagerListAll();
		model.addAttribute("managerList", managerList);

		// 강의 리스트
		List<LectureVO> lectureList = adminService.getLectureListAll();
		model.addAttribute("lectureList", lectureList);

		return "admin/class_form";
	}

	/**
	 * 교육과정 수정 POST
	 * 
	 * @author : eunji
	 * @date : 2023. 9. 12.
	 * @parameter : session, files, redirectAttrs, classInsertDTO, clssId, cmpyId,
	 *            mngrId, lctrIds, fileSubIds
	 * @return : String
	 */
	@PostMapping("/class/update")
	public String updateClass(HttpSession session, @RequestParam("files") MultipartFile[] files,
			RedirectAttributes redirectAttrs, ClassInsertDTO classInsertDTO, @RequestParam String clssCd,
			@RequestParam(required = false) String cmpyId, @RequestParam(required = false) String mngrId,
			@RequestParam(name = "lctrId", required = false) List<String> lctrIds,
			@RequestParam(name = "fileSubId", required = false) List<String> fileSubIds) {

		// 원래의 교육과정 가져오기
		ClassVO classVO = adminService.getClass(classInsertDTO.getClssId());

		// 현 교육과정의 fileId값 가져오기
		String fileId = classVO.getFileId();

		// 삭제되거나 변경전 파일 아이디들(deleteFileIds) 있다면 update deleteYn='Y'
		if (fileSubIds != null) {
			adminService.deleteFile(fileId, fileSubIds);
		}

		// 만약 null이라면 파일이 없었음. fileId 생성해줌.
		if (fileId == null) {
			fileId = uploadFileService.getMaxFileId();
		}

		// 추가한 파일 insert하기.파일 변경이 없거나 모두 삭제시 fileList는 null.
		List<FileVO> fileList = null;

		if (files[0] != null && !files[0].isEmpty()) {
			Integer fileSubId = adminService.getMaxFileSubId(fileId);

			if (fileSubId == null) {
				fileSubId = 1;
			}

			fileList = new ArrayList<FileVO>();
			try {
				for (MultipartFile file : files) {
					if (file != null && !file.isEmpty()) {
						FileVO fileVO = new FileVO();
						fileVO.setFileId(fileId);
						fileVO.setFileSubId(fileSubId);
						fileVO.setFileNm(file.getOriginalFilename());
						fileVO.setFileSize(file.getSize());
						fileVO.setFileType(file.getContentType());
						fileVO.setFileContent(file.getBytes());
						fileVO.setRgsterId("MNGR000001");
						fileList.add(fileVO);
						fileSubId++;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				redirectAttrs.addFlashAttribute("message", e.getMessage());
			}
		}

		// dto연결하고 select문들 다 가져와서 교육과정 update하기
		classVO.setMngrId(mngrId);
		classVO.setCmpyId(cmpyId);
		classVO.setClssNm(classInsertDTO.getClssNm());
		classVO.setClssContent(classInsertDTO.getClssContent());
		String clssSubsidy = classInsertDTO.getClssSubsidy().replaceAll(",", "");

		classVO.setClssSubsidy(Integer.parseInt(clssSubsidy));

		classVO.setLimitCnt(classInsertDTO.getLimitCnt());

		// Date-time(String) to Timestamp

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

		// Time(String) to Timestamp
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

		// String to Date
		try {
			String clssStartDd = (String) classInsertDTO.getClssStartDd();
			String clssEndDd = (String) classInsertDTO.getClssEndDd();
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
		classVO.setClssEtc(classInsertDTO.getClssEtc());
		classVO.setUpdterId((String) session.getAttribute("mngrId"));

		// 커리큘럼 다 가져와서 원래꺼 delete하고 새로 insert하기
		// 커리큘럼을 변경하지 않았거나 모두 삭제했을 경우 null전달
		List<CurriculumVO> curriculumList = null;
		if (lctrIds != null) {
			curriculumList = new ArrayList<CurriculumVO>();
			for (String lctrId : lctrIds) {
				CurriculumVO curriculumVO = new CurriculumVO();
				curriculumVO.setClssId(classVO.getClssId());
				curriculumVO.setLctrId(lctrId);
				curriculumVO.setRgsterId((String) session.getAttribute("mngrId"));
				curriculumList.add(curriculumVO);
			}
		}

		adminService.updateClass(fileList, classVO, curriculumList);
		System.out.println("===============================================");
		System.out.println("클래스: " + classVO);
		System.out.println("===============================================");

		// 교육과정 상세페이지로 이동하기
		return "redirect:/admin/class/view/" + classVO.getClssId();
	}

	/**
	 * 교육과정 선택 삭제
	 * 
	 * @author : eunji
	 * @date : 2023. 9. 12.
	 * @parameter : clssId
	 * @return : String
	 */
	@PostMapping("/cls/delete")
	public String classDeleteAll(@RequestParam(name = "select") List<String> clssIds) {
		// 선택한 교육과정 모두 update deleteYn='Y', updt
		adminService.deleteClass(clssIds);

		return "redirect:/admin/class/list/1";
	}

	/**
	 * 교육과정 개별 삭제
	 * 
	 * @author : eunji
	 * @date : 2023. 9. 12.
	 * @parameter : clssId
	 * @return : String
	 */
	@PostMapping("/class/delete/{clssId}")
	public @ResponseBody String classDelete(@PathVariable String clssId) {
		List<String> clssIds = new ArrayList<String>();
		clssIds.add(clssId);
		adminService.deleteClass(clssIds);
		return clssId;
	}

	/**
	 * 교육과정명 자동완성
	 * 
	 * @author : eunji
	 * @date : 2023. 10. 02.
	 * @parameter : term
	 * @return : String
	 */
	@GetMapping("/class/autocomplete")
	@ResponseBody
	public String classAutocomplete(@RequestParam String term) {
		// 자동완성 검색어 목록 생성
		List<String> autocompleteResults = new ArrayList<>();
		// 결과 생성
		autocompleteResults = adminService.getClassSearch(term);

		// 결과를 HTML 형태로 변환
		StringBuilder resultBuilder = new StringBuilder();
		for (String result : autocompleteResults) {
			resultBuilder.append("<option value=\"").append(result).append("\">").append(result).append("</option>");
		}

		return resultBuilder.toString();
	}

	/**
	 * 교육과정 목록 검색
	 * 
	 * @author : eunji
	 * @date : 2023. 10. 02.
	 * @parameter : className, status, aplyStartDt, aplyEndDt, classStartDd,
	 *            classEndDd, cmpyId
	 * @return : String
	 */
	@GetMapping("/class/search")
	@ResponseBody
	public List<ClassVO> classSearch(HttpSession session,
			@RequestParam(name = "clssNm", required = false) String clssNm,
			@RequestParam(name = "clssCd", required = false) String clssCd,
			@RequestParam(name = "aplyStartDt", required = false) String aplyStartDt,
			@RequestParam(name = "aplyEndDt", required = false) String aplyEndDt,
			@RequestParam(name = "clssStartDd", required = false) String clssStartDd,
			@RequestParam(name = "clssEndDd", required = false) String clssEndDd,
			@RequestParam(name = "cmpyId", required = false) String cmpyId) {

		System.out.println("##########################");
		System.out.println(clssNm);
		System.out.println(clssCd);
		System.out.println(aplyStartDt);
		System.out.println(aplyEndDt);
		System.out.println(clssStartDd);
		System.out.println(clssEndDd);
		System.out.println(cmpyId);
		System.out.println("##########################");

		// 검색 결과통해서 교육과정 리스트 객체 생성
		List<ClassVO> classList = adminService.getSearchClassList(clssNm, clssCd, aplyStartDt, aplyEndDt, clssStartDd,
				clssEndDd, cmpyId);
		// 엑셀다운로드를 위해 저장해놓기
		session.setAttribute("searchClassList", classList);

		return classList;
	}

	/**
	 * 교육과정 지원자 목록조회
	 * 
	 * @author : eunji
	 * @date : 2023. 9. 13.
	 * @parameter : clssId, model, session
	 * @return : String
	 */
	@GetMapping("/class/{clssId}/applicant")
	public String applicant(@PathVariable String clssId, Model model, HttpSession session) {
		// 해당 교육과정VO 생성
		ClassVO classVO = adminService.getClass(clssId);
		model.addAttribute("classVO", classVO);

		// 지원자 목록 생성
		List<ApplyDetailDTO> applyDetailList = adminService.getApplyDetailDTOList(clssId);
		model.addAttribute("applyDetailList", applyDetailList);
		
		session.setAttribute("searchApplyList", applyDetailList);

		return "admin/applicant_list";
	}
	
	/**
	 * 교육과정 수강자 목록조회
	 * 
	 * @author : eunji
	 * @date : 2023. 9. 13.
	 * @parameter : clssId, model, session
	 * @return : String
	 */
	@GetMapping("/class/{clssId}/registant")
	public String registant(@PathVariable String clssId, Model model, HttpSession session) {
		// 해당 교육과정VO 생성
		ClassVO classVO = adminService.getClass(clssId);
		model.addAttribute("classVO", classVO);

		// 수강자 목록 생성
		List<RegistrationDTO> registList = adminService.getRegistDTOList(clssId);
		model.addAttribute("registList", registList);
		
		session.setAttribute("searchRegistList", registList);

		return "admin/registant_list";
	}

	/**
	 * 교육과정 지원자 합불처리
	 * 
	 * @author : eunji
	 * @date : 2023. 9. 13.
	 * @parameter : clssId, session, action, aplyIds
	 * @return : String
	 * @throws Exception
	 */
	@PostMapping("/class/{clssId}/applicant")
	public String applicant(@PathVariable String clssId, HttpSession session, @RequestParam("action") String action,
			@RequestParam(name = "select", required = false) List<String> aplyIds) throws Exception {
		if ("합격".equals(action)) {
			// 선택한 개수와 현재 합격된 교육생 수가 수강가능 인원 이하인지 확인(나중에 구현)
			// "pass" 버튼을 클릭한 경우 실행할 코드
			adminService.updateAplyPass(aplyIds);

			for (String aplyId : aplyIds) {
				String recipient = adminService.getStudentEmailByAplyId(aplyId);
				String clssNm = adminService.getClssNmByAplyId(aplyId);
				mailService.sendMail(recipient, clssNm);
			}

		} else if ("불합격".equals(action)) {
			// "fail" 버튼을 클릭한 경우 실행할 코드
			adminService.updateAplyFail(aplyIds);
		}

		// 원하는 뷰 페이지로 이동
		return "redirect:/admin/class/" + clssId + "/applicant";
	}

	/**
	 * 강의 목록조회
	 * 
	 * @author : eunji
	 * @date : 2023. 9. 13.
	 * @parameter : model, session
	 * @return : String
	 */
	@RequestMapping("/lecture/list/{page}")
	public String lecture(@PathVariable int page, HttpSession session, Model model) {
		// 강의 리스트 페이지 생성
		List<LectureVO> lectureList = adminService.getLectureList(page);
		model.addAttribute("lectureList", lectureList);

		// 강의 all 페이지 생성
		List<LectureVO> lectureListAll = adminService.getLectureListAll();
		model.addAttribute("lectureListAll", lectureListAll);
		session.setAttribute("searchLectureList", lectureListAll);

		// 과목 리스트 생성
		List<SubjectVO> subjectList = adminService.getSubjectList();
		model.addAttribute("subjectList", subjectList);

		// 강사 리스트 생성
		List<ProfessorVO> professorList = adminService.getProfessorList();
		model.addAttribute("professorList", professorList);

		session.setAttribute("page", page);

		int bbsCount = lectureListAll.size();
		model.addAttribute("noticeCnt", bbsCount);
		int totalPage = 0;
		if (bbsCount > 0) {
			totalPage = (int) Math.ceil(bbsCount / 20.0);
		}

		int totalPageBlock = (int) (Math.ceil(totalPage / 10.0));
		int nowPageBlock = (int) Math.ceil(page / 10.0);
		int startPage = (nowPageBlock - 1) * 10 + 1;
		int endPage = 0;
		if (totalPage > nowPageBlock * 10) {
			endPage = nowPageBlock * 10;
		} else {
			endPage = totalPage;
		}
		model.addAttribute("totalPageCount", totalPage);
		model.addAttribute("nowPage", page);
		model.addAttribute("totalPageBlock", totalPageBlock);
		model.addAttribute("nowPageBlock", nowPageBlock);
		model.addAttribute("startPage", startPage);
		model.addAttribute("endPage", endPage);

		return "admin/lecture_list";
	}

	/**
	 * 강의 생성 팝업페이지
	 * 
	 * @author : eunji
	 * @date : 2023. 10. 22.
	 * @parameter : model
	 * @return : String
	 */
	@RequestMapping("/lecture/insert")
	public String lectureInsert() {

		return "admin/insert_lecture_popup";
	}

	/**
	 * 강의 생성 - 과목 선택 팝업 페이지
	 * 
	 * @author : eunji
	 * @date : 2023. 10. 22.
	 * @parameter : model
	 * @return : String
	 */
	@RequestMapping("/lecture/select/subject")
	public String selectSubject(Model model) {
		List<SubjectVO> subjectList = adminService.getSubjectList();
		model.addAttribute("subjectList", subjectList);

		return "admin/select_subject_popup";
	}

	/**
	 * 과목 생성 팝업 페이지
	 * 
	 * @author : eunji
	 * @date : 2023. 10. 25.
	 * @parameter :
	 * @return : String
	 */
	@RequestMapping("/subject/insert")
	public String insertSubject() {

		return "admin/insert_subject_popup";
	}

	/**
	 * 과목 생성 POST
	 * 
	 * @author : eunji
	 * @date : 2023. 9. 14.
	 * @parameter : sbjtNm
	 * @return : String
	 */
	@PostMapping("/subject/insert")
	@ResponseBody
	public String insertSubject(HttpSession session, @RequestParam("subjectInput") String sbjtNm) {
		// 과목명이 중복되는지 확인 후 중복되면 return 실패보내기
		// sql에서 대문자로 변경 후 공백 제거한 것으로 비교.
		Integer sbjtNmCnt = adminService.getSubjectNmCnt(sbjtNm);
		if (sbjtNmCnt != 0) {
			return "fail";
		} else {
			SubjectVO subjectVO = new SubjectVO();
			subjectVO.setSbjtId(adminService.getMaxSubjectId());
			subjectVO.setSbjtNm(sbjtNm);
			subjectVO.setRgsterId((String) session.getAttribute("mngrId"));
			adminService.insertSubjectVO(subjectVO);

			// 없는 이름이면 객체 생성해서 insert하기
			return "success";
		}
	}

	/**
	 * 강의 생성 - 강사 선택 팝업 페이지
	 * 
	 * @author : eunji
	 * @date : 2023. 10. 22.
	 * @parameter : model
	 * @return : String
	 */
	@RequestMapping("/lecture/select/professor")
	public String selectProfessor(Model model) {
		List<ProfessorVO> professorList = adminService.getProfessorList();
		model.addAttribute("professorList", professorList);

		return "admin/select_professor_popup";
	}

	/**
	 * 강사 생성 팝업 페이지
	 * 
	 * @author : eunji
	 * @date : 2023. 10. 25.
	 * @parameter :
	 * @return : String
	 */
	@RequestMapping("/professor/insert")
	public String insertProfessor() {
		return "admin/insert_professor_popup";
	}

	/**
	 * 강사 생성
	 * 
	 * @author : eunji
	 * @date : 2023. 9. 14.
	 * @parameter : profNm, profTel, profEmail
	 * @return : String
	 */
	@PostMapping("/professor/insert")
	@ResponseBody
	public String insertProfessor(HttpSession session, @RequestParam("profNm") String profNm,
			@RequestParam("profTel") String profTel, @RequestParam("profEmail") String profEmail) {
		// 전화번호가 중복되는지 확인 후 중복되면 return 실패보내기
		// sql에서 대문자로 변경 후 공백 제거한 것으로 비교.
		Integer profTelCnt = adminService.getProfTelCnt(profTel, profEmail);

		if (profTelCnt != 0) {
			return "fail";
		} else {
			ProfessorVO professorVO = new ProfessorVO();
			professorVO.setProfId(adminService.getMaxProfId());
			professorVO.setProfNm(profNm);
			professorVO.setProfTel(profTel);
			professorVO.setProfEmail(profEmail);
			professorVO.setRgsterId((String) session.getAttribute("mngrId"));
			adminService.insertProfessorVO(professorVO);

			// 없는 이름이면 객체 생성해서 insert하기
			return "success";
		}
	}

	/**
	 * 강의 생성
	 * 
	 * @author : eunji
	 * @date : 2023. 9. 13.
	 * @parameter : model, session
	 * @return : String
	 */
	@PostMapping("/lecture/insert")
	@ResponseBody
	public String insertLecture(@RequestParam("lectureNm") String lectureNm,
			@RequestParam("subjectId") String subjectId, @RequestParam("professorId") String professorId,
			@RequestParam("lectureTm") int lectureTm, @RequestParam("lectureEtc") String lectureEtc) {

		// 강의명이 중복되는지 확인 후 중복 시 fail 리턴
		Integer lctrNmCnt = adminService.getLctrNmCnt(lectureNm);

		if (lctrNmCnt != 0) {
			return "fail";
		} else {
			LectureVO lectureVO = new LectureVO();
			lectureVO.setLctrId(adminService.getMaxLectureId());
			lectureVO.setLctrNm(lectureNm);
			lectureVO.setSbjtId(subjectId);
			lectureVO.setProfId(professorId);
			lectureVO.setLctrTm(lectureTm);
			lectureVO.setLctrEtc(lectureEtc);

			adminService.insertLectureVO(lectureVO);

			return "success";
		}
	}

	/**
	 * 강의 일괄 수정
	 * 
	 * @author : eunji
	 * @date : 2023. 10. 5.
	 * @parameter : updatedlectureList
	 * @return : Map<String, Object>
	 */
	@PostMapping("/lecture/update")
	@ResponseBody
	public Map<String, Object> updateLecture(@RequestBody LectureVO[] updateLectureList) {
		adminService.updateLecture(updateLectureList);

		Map<String, Object> data = new HashMap<>();
		data.put("message", "success");

		return data;
	}

	/**
	 * 과목 일괄 수정
	 * 
	 * @author : eunji
	 * @date : 2023. 10. 5.
	 * @parameter : lectureId, lectureNm, lectureTm
	 * @return : String
	 */
	@PostMapping("/lecture/subject/update")
	@ResponseBody
	public Map<String, Object> updateSubject(@RequestBody SubjectVO[] updateSubjectList) {
		adminService.updateSubject(updateSubjectList);

		Map<String, Object> data = new HashMap<>();
		data.put("message", "success");

		return data;
	}

	/**
	 * 강사 일괄 수정
	 * 
	 * @author : eunji
	 * @date : 2023. 10. 5
	 * @parameter : professorId, professorNm, professorTel, professorEmail
	 * @return : String
	 */
	@PostMapping("/lecture/professor/update")
	@ResponseBody
	public Map<String, Object> updateProfessor(@RequestBody ProfessorVO[] updateProfessorList) {
		adminService.updateProfessor(updateProfessorList);

		Map<String, Object> data = new HashMap<>();
		data.put("message", "success");

		return data;
	}

	/**
	 * 강의 검색
	 * 
	 * @author : eunji
	 * @date : 2023. 10. 5.
	 * @parameter :
	 * @return : LectureListDTO
	 */
	@GetMapping("/lecture/search")
	@ResponseBody
	public Map<String, Object> searchLecture(@RequestParam(name = "lctrNm", required = false) String lctrNm,
			@RequestParam(name = "sbjtId", required = false) String sbjtId,
			@RequestParam(name = "profId", required = false) String profId) {
		Map<String, Object> response = new HashMap<String, Object>();

		List<LectureVO> lectureList = adminService.getSearchLectureList(lctrNm.trim(), sbjtId, profId);
		response.put("lectureList", lectureList);

		List<SubjectVO> subjectList = adminService.getSubjectList();
		response.put("subjectList", subjectList);

		List<ProfessorVO> professorList = adminService.getProfessorList();
		response.put("professorList", professorList);

		return response;
	}

	/**
	 * 과목 검색
	 * 
	 * @author : eunji
	 * @date : 2023. 10. 25.
	 * @parameter :
	 * @return : Map<String, Object>
	 */
	@GetMapping("/subject/search")
	@ResponseBody
	public Map<String, Object> searchSubject(@RequestParam(name = "sbjtNm", required = false) String sbjtNm) {
		Map<String, Object> response = new HashMap<String, Object>();

		List<SubjectVO> subjectList = adminService.getSearchSubjectList(sbjtNm.trim());
		response.put("subjectList", subjectList);

		return response;
	}

	/**
	 * 강사 검색
	 * 
	 * @author : eunji
	 * @date : 2023. 10. 25.
	 * @parameter :
	 * @return : Map<String, Object>
	 */
	@GetMapping("/professor/search")
	@ResponseBody
	public Map<String, Object> searchProfessor(@RequestParam(name = "keyword", required = false) String keyword) {
		Map<String, Object> response = new HashMap<String, Object>();

		List<ProfessorVO> professorList = adminService.getSearchProfessorList(keyword.trim());
		response.put("professorList", professorList);

		return response;
	}

	/**
	 * 강의 선택삭제
	 * 
	 * @author : eunji
	 * @date : 2023. 9. 14.
	 * @parameter : profNm, profTel, profEmail
	 * @return : String
	 */
	@PostMapping("/lecture/lecture/deleteall")
	@ResponseBody
	public String deleteAllLectures(@RequestParam("selectedLectureIds[]") List<String> selectedLectureIds) {
		// 선택된 lectureIds 를 가지고 모두 update deletYn='Y'로 변경해줌
		adminService.deleteLecture(selectedLectureIds);

		return "success";

	}

	/**
	 * 과목 선택삭제
	 * 
	 * @author : eunji
	 * @date : 2023. 9. 14.
	 * @parameter : profNm, profTel, profEmail
	 * @return : String
	 */
	@PostMapping("/lecture/subject/deleteall")
	@ResponseBody
	public String deleteAllSubjects(@RequestParam("selectedSubjectIds[]") List<String> selectedSubjectIds) {
		// 선택된 lectureIds 를 가지고 모두 update deletYn='Y'로 변경해줌
		adminService.deleteSubject(selectedSubjectIds);

		return "success";

	}

	/**
	 * 강사 선택삭제
	 * 
	 * @author : eunji
	 * @date : 2023. 9. 14.
	 * @parameter : profNm, profTel, profEmail
	 * @return : String
	 */
	@PostMapping("/lecture/professor/deleteall")
	@ResponseBody
	public String deleteAllProfessors(@RequestParam("selectedProfessorIds[]") List<String> selectedProfessorIds) {
		// 선택된 lectureIds 를 가지고 모두 update deletYn='Y'로 변경해줌
		adminService.deleteProfessor(selectedProfessorIds);

		return "success";

	}

	/**
	 * 기업 목록조회
	 * 
	 * @author : eunji
	 * @date : 2023. 9. 15.
	 * @parameter : model
	 * @return : String
	 */
	@RequestMapping("/company/list/{page}")
	public String companyList(HttpSession session, @PathVariable int page, Model model) {
		// 기업 페이지 리스트 전달
		List<CompanyVO> companyList = adminService.getCompanyList(page);
		model.addAttribute("companyList", companyList);

		// 기업 전체 리스트 전달
		List<CompanyVO> companyListAll = adminService.getCompanyListAll();
		session.setAttribute("searchCompanyList", companyListAll);

		session.setAttribute("page", page);

		int bbsCount = adminService.getCompanyCnt();
		model.addAttribute("companyCnt", bbsCount);
		int totalPage = 0;
		if (bbsCount > 0) {
			totalPage = (int) Math.ceil(bbsCount / 20.0);
		}

		int totalPageBlock = (int) (Math.ceil(totalPage / 10.0));
		int nowPageBlock = (int) Math.ceil(page / 10.0);
		int startPage = (nowPageBlock - 1) * 10 + 1;
		int endPage = 0;
		if (totalPage > nowPageBlock * 10) {
			endPage = nowPageBlock * 10;
		} else {
			endPage = totalPage;
		}
		model.addAttribute("totalPageCount", totalPage);
		model.addAttribute("nowPage", page);
		model.addAttribute("totalPageBlock", totalPageBlock);
		model.addAttribute("nowPageBlock", nowPageBlock);
		model.addAttribute("startPage", startPage);
		model.addAttribute("endPage", endPage);

		return "admin/company_list";
	}

	/**
	 * 기업 상세조회
	 * 
	 * @author : eunji
	 * @date : 2023. 9. 19.
	 * @parameter : cmpyId
	 * @return : String
	 */
	@PostMapping("/company/view")
	@ResponseBody
	public Map<String, Object> companySelect(@RequestParam String cmpyId) {
		// 기업 객체 생성
		CompanyVO companyVO = adminService.getCompanyVO(cmpyId);

		Map<String, Object> response = new HashMap<String, Object>();
		response.put("companyVO", companyVO);

		return response;
	}

	@GetMapping("/file/{fileId}/1")
	public ResponseEntity<byte[]> getFile(@PathVariable String fileId) {
		FileVO file = uploadFileService.getFile(fileId, "1");

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

	/**
	 * 기업 검색
	 * 
	 * @author : eunji
	 * @date : 2023. 10. 20.
	 * @parameter : tpcdId, cmcdNm
	 * @return : String
	 */
	@PostMapping("/company/search")
	@ResponseBody
	public Map<String, Object> searchCompany(HttpSession session,
			@RequestParam(name = "cmpyNm", required = false) String cmpyNm) {
		Map<String, Object> response = new HashMap<String, Object>();

		List<CompanyVO> companyList = adminService.getSearchCompanyList(cmpyNm.trim());
		session.setAttribute("searchCompanyList", companyList);

		response.put("companyList", companyList);

		return response;
	}

	/**
	 * 기업 생성
	 * 
	 * @author : eunji
	 * @date : 2023. 9. 20.
	 * @parameter : tpcdId, cmcdNm
	 * @return : String
	 */
	@RequestMapping("/company/insert")
	public String insertCompany() {
		return "admin/insert_company_popup";
	}

	/**
	 * 기업 생성
	 * 
	 * @author : eunji
	 * @date : 2023. 9. 19.
	 * @parameter : file,redirectAttrs,cmpyNm,cmpyTel,cmpyAdr
	 * @return : String
	 * @throws IOException
	 */
	@PostMapping("/company/insert")
	@ResponseBody
	public String companyInsert(@RequestParam(name = "file", required = false) MultipartFile file,
			RedirectAttributes redirectAttrs, @RequestParam("cmpyNm") String cmpyNm,
			@RequestParam("cmpyTel") String cmpyTel, @RequestParam("cmpyAdr") String cmpyAdr,
			@RequestParam(name = "cmpyAdrDetail", required = false) String cmpyAdrDetail) throws IOException {

		String fileId = null;
		FileVO fileVO = null;
		// 파일 처리

		if (file != null && !file.isEmpty()) {
			fileVO = new FileVO();
			fileId = uploadFileService.getMaxFileId();
			fileVO.setFileId(fileId);
			fileVO.setFileSubId(1);
			fileVO.setFileNm(file.getOriginalFilename());
			fileVO.setFileSize(file.getSize());
			fileVO.setFileType(file.getContentType());
			fileVO.setFileContent(file.getBytes());
			fileVO.setRgsterId("MNGR000001");
		}

		CompanyVO companyVO = new CompanyVO();
		companyVO.setCmpyId(adminService.getMaxCompanyId());
		companyVO.setCmpyNm(cmpyNm);
		companyVO.setCmpyTel(cmpyTel);
		companyVO.setCmpyAdr(cmpyAdr);
		companyVO.setCmpyAdrDetail(cmpyAdrDetail);
		companyVO.setFileId(fileId);

		adminService.insertCompanyVO(fileVO, companyVO);

		return "success";
	}

	/**
	 * 기업 수정
	 * 
	 * @author : eunji
	 * @date : 2023. 9. 26.
	 * @parameter : cmpyId, file, redirectAttrs, cmpyNm, cmpyTel, cmpyAdr
	 * @return : String
	 * @throws IOException
	 */
	@PostMapping("/company/update/{cmpyId}")
	@ResponseBody
	public List<CompanyVO> updateCompany(@PathVariable String cmpyId,
			@RequestParam(name = "file", required = false) MultipartFile file, RedirectAttributes redirectAttrs,
			@RequestParam("cmpyNm") String cmpyNm, @RequestParam("cmpyTel") String cmpyTel,
			@RequestParam("cmpyAdr") String cmpyAdr, @RequestParam(name = "cmpyAdrDetail") String cmpyAdrDetail)
			throws IOException {

		CompanyVO companyVO = adminService.getCompanyVO(cmpyId);
		companyVO.setCmpyNm(cmpyNm);
		companyVO.setCmpyTel(cmpyTel);
		companyVO.setCmpyAdr(cmpyAdr);
		companyVO.setCmpyAdrDetail(cmpyAdrDetail);

		String fileId = companyVO.getFileId();
		FileVO fileVO = null;

		if (file != null && !file.isEmpty()) {
			fileVO = new FileVO();
			fileId = uploadFileService.getMaxFileId();
			fileVO.setFileId(fileId);
			fileVO.setFileSubId(1);
			fileVO.setFileNm(file.getOriginalFilename());
			fileVO.setFileSize(file.getSize());
			fileVO.setFileType(file.getContentType());
			fileVO.setFileContent(file.getBytes());
		}

		companyVO.setFileId(fileId);

		adminService.updateCompany(fileVO, companyVO);

		List<CompanyVO> companyList = adminService.getCompanyListAll();

		return companyList;
	}

	/**
	 * 기업 선택삭제
	 * 
	 * @author : eunji
	 * @date : 2023. 9. 19.
	 * @parameter : selectedCompanyIds
	 * @return : String
	 */
	@PostMapping("/company/delete")
	@ResponseBody
	public String companyDelete(@RequestParam("selectedCompanyIds[]") List<String> selectedCompanyIds) {
		adminService.deleteCompany(selectedCompanyIds);

		return "success";
	}

	/**
	 * 업무담당자 목록조회
	 * 
	 * @author : eunji
	 * @date : 2023. 9. 15.
	 * @parameter : profNm, profTel, profEmail
	 * @return : String
	 */
	@RequestMapping("/manager/list/{page}")
	public String manager(@PathVariable int page, HttpSession session, Model model) {
		// 업무담당자 계정 기준정보 리스트
		List<CommonCodeVO> mngrCommonCodeList = adminService.getCommonCodeList("GRP0000008");
		model.addAttribute("mngrCommonCodeList", mngrCommonCodeList);

		// 교육과정 리스트
		List<ClassVO> classList = adminService.getClassListAll();
		model.addAttribute("classList", classList);

		// 업무담당자 페이지 리스트
		List<ManagerVO> managerList = adminService.getManagerList(page);
		model.addAttribute("managerList", managerList);

		// 업무담당자 전체 리스트
		List<ManagerVO> managerListAll = adminService.getManagerListAll();
		session.setAttribute("searchManagerList", managerListAll);

		int bbsCount = adminService.getManagerCnt();
		model.addAttribute("managerCnt", bbsCount);
		int totalPage = 0;
		if (bbsCount > 0) {
			totalPage = (int) Math.ceil(bbsCount / 20.0);
		}

		int totalPageBlock = (int) (Math.ceil(totalPage / 10.0));
		int nowPageBlock = (int) Math.ceil(page / 10.0);
		int startPage = (nowPageBlock - 1) * 10 + 1;
		int endPage = 0;
		if (totalPage > nowPageBlock * 10) {
			endPage = nowPageBlock * 10;
		} else {
			endPage = totalPage;
		}
		model.addAttribute("totalPageCount", totalPage);
		model.addAttribute("nowPage", page);
		model.addAttribute("totalPageBlock", totalPageBlock);
		model.addAttribute("nowPageBlock", nowPageBlock);
		model.addAttribute("startPage", startPage);
		model.addAttribute("endPage", endPage);

		return "admin/manager_list";
	}

	/**
	 * 업무담당자 등록시 이메일 유효성 검사
	 * 
	 * @author : eunji
	 * @date : 2023. 9. 26.
	 * @parameter : managerEmail
	 * @return : String
	 */
	@PostMapping("/manager/check_email")
	@ResponseBody
	public String managerEmailCheck(@RequestParam String managerEmail) {
		int managerCnt = adminService.getManagerEmailCnt(managerEmail);
		if (managerCnt == 0) {
			return "success";

		} else {
			return "fail";
		}
	}

	/**
	 * 업무담당자 등록
	 * 
	 * @author : eunji
	 * @date : 2023. 10. 18.
	 * @parameter : tpcdId, cmcdNm
	 * @return : String
	 */
	@RequestMapping("/manager/insert")
	public String insertManager() {
		return "admin/insert_manager_popup";
	}

	/**
	 * 업무담당자 등록
	 * 
	 * @author : eunji
	 * @date : 2023. 9. 18.
	 * @parameter : managerNm, managerTel, managerEmail,managerPwd
	 * @return : String
	 */
	@PostMapping("/manager/insert")
	@ResponseBody
	public String managerInsert(@RequestParam String managerNm, @RequestParam String managerTel,
			@RequestParam String managerEmail, @RequestParam String managerPwd) {
		ManagerVO managerVO = new ManagerVO();
		managerVO.setMngrId(adminService.getMaxManagerId());
		managerVO.setMngrNm(managerNm);
		managerVO.setMngrTel(managerTel);
		managerVO.setUserEmail(managerEmail);
		managerVO.setUserPwd(managerPwd);

		adminService.insertManagerVO(managerVO);

		return "success";
	}

	/**
	 * 업무담당자 검색
	 * 
	 * @author : eunji
	 * @date : 2023. 9. 18.
	 * @parameter : profNm, profTel, profEmail
	 * @return : String
	 */
	@PostMapping("/manager/search")
	@ResponseBody
	public Map<String, Object> managerSearch(HttpSession session, @RequestParam String searchInputCategory,
			@RequestParam String searchInput, @RequestParam String searchMngrStatus,
			@RequestParam String searchClassId) {
		Map<String, Object> response = new HashMap<String, Object>();

		List<ManagerVO> managerList = adminService.getSearchManagerList(searchInputCategory, searchInput.trim(),
				searchMngrStatus, searchClassId);
		response.put("managerList", managerList);
		session.setAttribute("searchManagerList", managerList);

		// 업무담당자 계정 기준정보 리스트
		List<CommonCodeVO> mngrCommonCodeList = adminService.getCommonCodeList("GRP0000008");
		response.put("mngrCommonCodeList", mngrCommonCodeList);

		return response;
	}

	/**
	 * 업무담당자 수정
	 * 
	 * @author : eunji
	 * @date : 2023. 9. 19.
	 * @parameter : selectedManagerIds
	 * @return : String
	 */
	@PostMapping("/manager/update")
	@ResponseBody
	public String managerUpdate(@RequestParam String mngrId, @RequestParam String mngrNm, @RequestParam String mngrTel,
			@RequestParam String mngrStatus) {

		ManagerVO managerVO = adminService.getManager(mngrId);
		managerVO.setMngrNm(mngrNm);
		managerVO.setMngrTel(mngrTel);
		managerVO.setUserCd(mngrStatus);
		adminService.updateManager(managerVO);
		return "success";
	}

	/**
	 * 업무담당자 선택삭제
	 * 
	 * @author : eunji
	 * @date : 2023. 9. 19.
	 * @parameter : selectedManagerIds
	 * @return : String
	 */
	@PostMapping("/manager/delete")
	@ResponseBody
	public String managerDelete(@RequestParam("selectedManagerIds[]") List<String> selectedManagerIds) {
		adminService.deleteManagerList(selectedManagerIds);
		return "success";
	}

	/**
	 * 교육생 목록조회
	 * 
	 * @author : eunji
	 * @date : 2023. 9. 25.
	 * @parameter : model
	 * @return : String
	 */
	@RequestMapping("/student/list/{page}")
	public String studentList(@PathVariable int page, HttpSession session, Model model) {
		// 검색부분
		// 교육과정 리스트
		List<ClassVO> classList = adminService.getClassListAll();
		model.addAttribute("classList", classList);

		// 성별 commoncode
		List<CommonCodeVO> genderList = adminService.getCommonCodeList("GRP0000006");
		model.addAttribute("genderList", genderList);

		// 직업 commoncode
		List<CommonCodeVO> jobList = adminService.getCommonCodeList("GRP0000007");
		model.addAttribute("jobList", jobList);

		// 계정상태 commoncode
		List<CommonCodeVO> statusList = adminService.getCommonCodeList("GRP0000008");
		model.addAttribute("statusList", statusList);

		// 교육생 페이지 리스트
		List<StudentVO> studentList = adminService.getStudentList(page);
		model.addAttribute("studentList", studentList);

		// 교육생 전체 리스트
		List<StudentVO> studentListAll = adminService.getStudentListAll();
		session.setAttribute("searchStudentList", studentListAll);

		session.setAttribute("page", page);

		int bbsCount = adminService.getStudentCnt();
		model.addAttribute("studentCnt", bbsCount);
		int totalPage = 0;
		if (bbsCount > 0) {
			totalPage = (int) Math.ceil(bbsCount / 20.0);
		}

		int totalPageBlock = (int) (Math.ceil(totalPage / 10.0));
		int nowPageBlock = (int) Math.ceil(page / 10.0);
		int startPage = (nowPageBlock - 1) * 10 + 1;
		int endPage = 0;
		if (totalPage > nowPageBlock * 10) {
			endPage = nowPageBlock * 10;
		} else {
			endPage = totalPage;
		}
		model.addAttribute("totalPageCount", totalPage);
		model.addAttribute("nowPage", page);
		model.addAttribute("totalPageBlock", totalPageBlock);
		model.addAttribute("nowPageBlock", nowPageBlock);
		model.addAttribute("startPage", startPage);
		model.addAttribute("endPage", endPage);

		return "admin/student_list";
	}

	/**
	 * 교육생 상세보기
	 * 
	 * @author : eunji
	 * @date : 2023. 9. 28.
	 * @parameter : stdtId, model
	 * @return : String
	 */
	@GetMapping("/student/view/{stdtId}")
	public String studenctDetail(@PathVariable String stdtId, Model model) {
		// 교육생 정보
		StudentVO studentVO = adminService.getStudent(stdtId);
		model.addAttribute("student", studentVO);

		// 교육생 지원정보
		List<ApplyVO> applyList = adminService.getApplyListByStudent(stdtId);
		model.addAttribute("applyList", applyList);

		// 교육생 수강정보
		List<RegistrationVO> registList = adminService.getRegistListByStudent(stdtId);
		model.addAttribute("registList", registList);

		// 성별 리스트
		List<CommonCodeVO> genderList = adminService.getCommonCodeList("GRP0000006");
		model.addAttribute("genderList", genderList);

		// 직업 리스트
		List<CommonCodeVO> jobList = adminService.getCommonCodeList("GRP0000007");
		model.addAttribute("jobList", jobList);

		// 계정상태 리스트
		List<CommonCodeVO> statusList = adminService.getCommonCodeList("GRP0000008");
		model.addAttribute("statusList", statusList);

		return "admin/student_form";
	}

	/**
	 * 교육생 수정
	 * 
	 * @author : eunji
	 * @date : 2023. 9. 29.
	 * @parameter : model
	 * @return : String
	 */
	@PostMapping("/student/update/{stdtId}")
	@ResponseBody
	public String updateStudent(@PathVariable String stdtId, @RequestParam String stdtNm, @RequestParam String stdtTel,
			@RequestParam String genderCd, @RequestParam String birthDd, @RequestParam String jobCd,
			@RequestParam String userCd) {

		StudentVO studentVO = adminService.getStudent(stdtId);
		studentVO.setStdtNm(stdtNm);
		studentVO.setStdtTel(stdtTel);
		studentVO.setGenderCd(genderCd);
		studentVO.setBirthDd(Date.valueOf(birthDd));
		studentVO.setJobCd(jobCd);
		studentVO.setUserCd(userCd);

		// adminService.updateStudent(studentVO);

		return "success";
	}

	/**
	 * 교육생 검색
	 * 
	 * @author : eunji
	 * @date : 2023. 9. 27.
	 * @parameter : stdtNm,clssId,genderCd,jobCd,userCd
	 * @return : String
	 */
	@GetMapping("/student/search")
	@ResponseBody
	public List<StudentVO> searchStudent(HttpSession session,
			@RequestParam(name = "stdtNm", required = false) String stdtNm, @RequestParam String clssId,
			@RequestParam String genderCd, @RequestParam String jobCd, @RequestParam String userCd) {

		List<StudentVO> studentList = adminService.getSearchStudentList(stdtNm, clssId, genderCd, jobCd, userCd);
		session.setAttribute("searchStudentList", studentList);

		return studentList;
	}

	/**
	 * 교육생 선택삭제
	 * 
	 * @author : eunji
	 * @date : 2023. 9. 19.
	 * @parameter : selectedStudentIds
	 * @return : String
	 */
	@PostMapping("/student/delete")
	@ResponseBody
	public String deleteStudent(@RequestParam("selectedStudentIds[]") List<String> selectedStudentIds) {
		adminService.deleteStudentList(selectedStudentIds);
		return "success";
	}

	/**
	 * 기준정보 그룹코드 목록조회
	 * 
	 * @author : eunji
	 * @date : 2023. 9. 15.
	 * @parameter : model
	 * @return : String
	 */
	@RequestMapping("/commoncode/list/{page}")
	public String commoncode(@PathVariable int page, HttpSession session, Model model) {
		// 그룹코드 페이지 리스트 전달
		List<CommonCodeVO> groupCodeList = adminService.getGroupCodeList(page);
		model.addAttribute("groupCodeList", groupCodeList);

		// 상세코드 생성 후 전달
		List<CommonCodeVO> detailCodeList = new ArrayList<CommonCodeVO>();
		model.addAttribute("detailCodeList", detailCodeList);

		// 그룹코드 전체 리스트 전달
		List<CommonCodeVO> groupCodeListAll = adminService.getGroupCodeListAll();
		session.setAttribute("searchGroupCodeList", groupCodeListAll);

		session.setAttribute("page", page);

		int bbsCount = adminService.getGroupCodeCnt();
		model.addAttribute("groupCodeCnt", bbsCount);
		int totalPage = 0;
		if (bbsCount > 0) {
			totalPage = (int) Math.ceil(bbsCount / 20.0);
		}

		int totalPageBlock = (int) (Math.ceil(totalPage / 10.0));
		int nowPageBlock = (int) Math.ceil(page / 10.0);
		int startPage = (nowPageBlock - 1) * 10 + 1;
		int endPage = 0;
		if (totalPage > nowPageBlock * 10) {
			endPage = nowPageBlock * 10;
		} else {
			endPage = totalPage;
		}
		model.addAttribute("totalPageCount", totalPage);
		model.addAttribute("nowPage", page);
		model.addAttribute("totalPageBlock", totalPageBlock);
		model.addAttribute("nowPageBlock", nowPageBlock);
		model.addAttribute("startPage", startPage);
		model.addAttribute("endPage", endPage);

		return "admin/commoncode_list";
	}

	/**
	 * 기준정보 그룹코드 리스트 전달
	 * 
	 * @author : eunji
	 * @date : 2023. 10. 6
	 * @parameter : model
	 * @return : String
	 */
	@GetMapping("/commoncode/getgroupcodelist")
	@ResponseBody
	public List<CommonCodeVO> getGroupCodeList() {

		// 그룹코드 리스트 전달
		List<CommonCodeVO> groupCodeList = adminService.getGroupCodeListAll();

		return groupCodeList;
	}

	/**
	 * 기준정보 그룹코드 생성
	 * 
	 * @author : eunji
	 * @date : 2023. 9. 20.
	 * @parameter : tpcdId, cmcdNm
	 * @return : String
	 */
	@RequestMapping("/commoncode/insert/groupcode")
	public String insertCMCD() {
		return "admin/insert_group_code_popup";
	}

	/**
	 * 기준정보 그룹코드 생성
	 * 
	 * @author : eunji
	 * @date : 2023. 9. 20.
	 * @parameter : tpcdId, cmcdNm
	 * @return : String
	 */
	@PostMapping("/commoncode/insert/groupcode")
	@ResponseBody
	public String insertGroupCode(@RequestParam String tpcdId, @RequestParam String cmcdNm) {
		// tpcd중복 확인
		int tpcdIdCnt = adminService.getTpcdIdCnt(tpcdId);

		if (tpcdIdCnt == 0) {
			// 그룹 코드 생성하기
			CommonCodeVO commonCodeVO = new CommonCodeVO();
			commonCodeVO.setCmcdId(adminService.getMaxGroupCodeId());
			commonCodeVO.setTpcdId(tpcdId);
			commonCodeVO.setCmcdNm(cmcdNm);

			adminService.insertGroupCode(commonCodeVO);
			return "success";
		} else {
			return "fail";
		}
	}

	/**
	 * 기준정보 그룹코드 수정
	 * 
	 * @author : eunji
	 * @date : 2023. 9. 20.
	 * @parameter : model
	 * @return : String
	 */
	@PostMapping("/commoncode/update/groupcode")
	@ResponseBody
	public Map<String, Object> updateGroupCode(@RequestBody CommonCodeVO[] updateGroupList) {
		adminService.updateGroupCode(updateGroupList);

		Map<String, Object> data = new HashMap<>();
		data.put("message", "success");

		return data;
	}

	/**
	 * 기준정보 그룹코드 삭제
	 * 
	 * @author : eunji
	 * @date : 2023. 9. 20.
	 * @parameter : selectedGroupCodeIds
	 * @return : String
	 */
	@PostMapping("/commoncode/delete/groupcode")
	@ResponseBody
	public String deleteGroupCode(@RequestParam(name = "selectedGroupCodeIds[]") List<String> selectedGroupCodeIds) {
		adminService.deleteGroupCode(selectedGroupCodeIds);

		return "success";

	}

	/**
	 * 기준정보 상세코드 목록조회
	 * 
	 * @author : eunji
	 * @date : 2023. 9. 20.
	 * @parameter : cmcdId, model
	 * @return : String
	 */
	@GetMapping("/commoncode/view/{cmcdId}")
	@ResponseBody
	public Map<String, Object> detailCode(@PathVariable String cmcdId) {
		Map<String, Object> response = new HashMap<String, Object>();

		// 상세코드 리스트 전달
		List<CommonCodeVO> detailCodeList = adminService.getDetailCodeList(cmcdId);
		response.put("detailCodeList", detailCodeList);

		return response;
	}

	/**
	 * 기준정보 상세코드 등록
	 * 
	 * @author : eunji
	 * @date : 2023. 10.16
	 * @parameter : selectedCode, model
	 * @return : String
	 */
	@GetMapping("/commoncode/insert/detailcode/popup/{selectedCode}")
	public String insertDetail(Model model, @PathVariable String selectedCode) {
		model.addAttribute("cmcdId", selectedCode);
		List<CommonCodeVO> groupCodeList = adminService.getGroupCodeListAll();
		model.addAttribute("groupCodeList", groupCodeList);

		return "admin/insert_detail_code_popup";
	}

	/**
	 * 기준정보 상세코드 등록
	 * 
	 * @author : eunji
	 * @date : 2023. 9. 20.
	 * @parameter : cmcdId,cmcdNm
	 * @return : String
	 */
	@PostMapping("/commoncode/insert/detailcode")
	@ResponseBody
	public String insertDetailCode(@RequestParam String cmcdId, @RequestParam String cmcdNm,
			@RequestParam int cmcdOrder) {
		// 이미 그 그룹코드에 사용여부가 Y중에 입력한 상세코드명이 있는지 확인
		int detailCodeCnt = adminService.getDetailCodeNmCnt(cmcdId, cmcdNm);
		// 있으면 return fail
		if (detailCodeCnt > 0) {
			return "fail";
		} else {
			CommonCodeVO commonCodeVO = new CommonCodeVO();
			commonCodeVO.setCmcdId(adminService.getMaxDetailCodeId(cmcdId));
			commonCodeVO.setTpcdId(cmcdId);
			commonCodeVO.setCmcdNm(cmcdNm);
			commonCodeVO.setCmcdOrder(cmcdOrder);

			adminService.insertDetailCode(commonCodeVO);

			return cmcdId;
		}
	}

	/**
	 * 기준정보 상세코드 수정
	 * 
	 * @author : eunji
	 * @date : 2023. 10. 6.
	 * @parameter : updateDetailList
	 * @return : Map<String, Object>
	 */
	@PostMapping("/commoncode/update/detailcode")
	@ResponseBody
	public Map<String, Object> updateDetailCode(@RequestBody CommonCodeVO[] updateDetailList) {
		System.out.println("오키");

		adminService.updateDetailCode(updateDetailList);

		Map<String, Object> data = new HashMap<>();
		data.put("message", "success");

		return data;
	}

	/**
	 * 기준정보 상세코드 삭제
	 * 
	 * @author : eunji
	 * @date : 2023. 9. 20.
	 * @parameter : selectedDetailCodeIds
	 * @return : String
	 */
	@PostMapping("/commoncode/delete/detailcode")
	@ResponseBody
	public String deleteDetailCode(
			@RequestParam(name = "selectedDetailCodeIds[]", required = false) List<String> selectedDetailCodeIds) {

		// 삭제여부'Y', updt 수정
		adminService.deleteGroupCode(selectedDetailCodeIds);
		String groupCodeId = adminService.getGroupCodeId(selectedDetailCodeIds.get(0));

		return groupCodeId;

	}

	/**
	 * 기준정보 그룹코드 검색
	 * 
	 * @author : eunji
	 * @date : 2023. 10. 17.
	 * @parameter : selectedDetailCodeIds
	 * @return : String
	 */
	@GetMapping("/commoncode/search/groupcode")
	@ResponseBody
	public Map<String, Object> searchGroupCode(@RequestParam(required = false) String tpcdId,
			@RequestParam(required = false) String cmcdNm, @RequestParam(required = false) String useYn) {
		Map<String, Object> map = new HashMap<String, Object>();

		List<CommonCodeVO> searchGroupCodeList = adminService.getSearchGroupCodeList(tpcdId.trim(), cmcdNm.trim(),
				useYn);
		map.put("groupCodeList", searchGroupCodeList);

		return map;
	}

	/**
	 * 기준정보 상세코드 검색
	 * 
	 * @author : eunji
	 * @date : 2023. 10. 17.
	 * @parameter : tpcdId, cmcdNm
	 * @return : String
	 */
	@GetMapping("/commoncode/search/detailcode")
	@ResponseBody
	public Map<String, Object> searchDetailCode(@RequestParam String tpcdId, @RequestParam String cmcdNm) {
		Map<String, Object> map = new HashMap<String, Object>();

		List<CommonCodeVO> searchDetailCodeList = adminService.getSearchDetailCodeList(tpcdId, cmcdNm.trim());
		map.put("detailCodeList", searchDetailCodeList);

		return map;
	}

	/**
	 * 지원금 목록조회
	 * 
	 * @author : eunji
	 * @date : 2023. 10. 12.
	 * @parameter : session, page, model
	 * @return : String
	 */
	@GetMapping("/subsidy/list/{page}")
	public String selectSubsidyList(HttpSession session, @PathVariable int page, Model model) {
		// 검색조건 - 교육과정
		List<ClassVO> classList = adminService.getClassListAll();
		model.addAttribute("classList", classList);

		// 검색조건 - 지원금지급상태
		List<CommonCodeVO> subsidyStatusList = adminService.getCommonCodeList("GRP0000012");
		model.addAttribute("subsidyStatusList", subsidyStatusList);

		// 지원금 리스트 페이징
		List<SubsidyDTO> subsidyList = adminService.getSubsidyList(page);
		model.addAttribute("subsidyList", subsidyList);

		// 지원금 전체 리스트
		List<SubsidyDTO> subsidyListAll = adminService.getSubsidyListAll();
		session.setAttribute("searchSubsidyList", subsidyListAll);

		// 페이징 처리
		session.setAttribute("page", page);

		int bbsCount = subsidyListAll.size();
		model.addAttribute("subsidyCnt", bbsCount);
		int totalPage = 0;
		if (bbsCount > 0) {
			totalPage = (int) Math.ceil(bbsCount / 20.0);
		}

		int totalPageBlock = (int) (Math.ceil(totalPage / 10.0));
		int nowPageBlock = (int) Math.ceil(page / 10.0);
		int startPage = (nowPageBlock - 1) * 10 + 1;
		int endPage = 0;
		if (totalPage > nowPageBlock * 10) {
			endPage = nowPageBlock * 10;
		} else {
			endPage = totalPage;
		}
		model.addAttribute("totalPageCount", totalPage);
		model.addAttribute("nowPage", page);
		model.addAttribute("totalPageBlock", totalPageBlock);
		model.addAttribute("nowPageBlock", nowPageBlock);
		model.addAttribute("startPage", startPage);
		model.addAttribute("endPage", endPage);

		return "admin/subsidy_list";
	}

	/**
	 * 지원금 조회 검색
	 * 
	 * @author : eunji
	 * @date : 2023. 10. 21.
	 * @parameter : session, page, model
	 * @return : String
	 */
	@GetMapping("/subsidy/search")
	@ResponseBody
	public Map<String, Object> searchSubsidy(HttpSession session,
			@RequestParam(name = "clssId", required = false) String clssId,
			@RequestParam(name = "startDate", required = false) String startDate,
			@RequestParam(name = "endDate", required = false) String endDate,
			@RequestParam(name = "keyword", required = false) String keyword,
			@RequestParam(name = "subsidyStatus", required = false) String subsidyStatus) {
		Map<String, Object> response = new HashMap<String, Object>();

		// 검색 결과 리스트 받기
		List<SubsidyDTO> subsidyList = adminService.getSearchSubsidyList(clssId, startDate, endDate, keyword.trim(),
				subsidyStatus);
		response.put("subsidyList", subsidyList);
		session.setAttribute("searchSubsidyList", subsidyList);

		return response;
	}

	/**
	 * 지원금 상태 수정
	 * 
	 * @author : eunji
	 * @date : 2023. 10. 12.
	 * @parameter : selectedDetailCodeIds
	 * @return : String
	 */
	@PostMapping("/subsidy/update")
	@ResponseBody
	public String subsidyUpdate(@RequestParam("selectedSubsidyIds[]") List<String> selectedSubsidyIds,
			@RequestParam String cmcdId) {

		adminService.updateSubsidyStatus(selectedSubsidyIds, cmcdId);

		return "success";
	}

}
