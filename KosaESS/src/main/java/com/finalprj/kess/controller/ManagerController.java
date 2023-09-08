package com.finalprj.kess.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.finalprj.kess.dto.StudentInfoDTO;
import com.finalprj.kess.model.ClassVO;
import com.finalprj.kess.model.CommonCodeVO;
import com.finalprj.kess.model.FileVO;
import com.finalprj.kess.model.StudentVO;
import com.finalprj.kess.service.IManagerService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/manager")
public class ManagerController {
	LocalDate today = LocalDate.now();
	LocalDate firstDay = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth());
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	IManagerService managerService;

	//	메인 페이지
	@GetMapping("")
	public String managerMain(Model model, HttpSession session) {
		String roleCd = (String) session.getAttribute("roleCd");
//		if (roleCd.equals("ROL0000003")) {
//			return "redirect:/manager/class";
//		} else {
//			return "redirect:/login";
//		}
		if (roleCd == null || !roleCd.equals("ROL0000003")) {
			return "redirect:/login";
		} else {
			return "redirect:/manager/class";
		}
	}

	//	담당 교육 목록 조회
	@GetMapping("/class")
	public String getClassList(Model model, HttpSession session) {
		String roleCd = (String) session.getAttribute("roleCd");
		if (roleCd == null || !roleCd.equals("ROL0000003")) {
			return "login";
		} else {
			model.addAttribute("title", "교육 과정 목록");
			List<ClassVO> classList = managerService.getClassListByMngrId((String) session.getAttribute("mngrId"));
			// session의 key-value를 설정 할 때 value가 object로 업캐스팅 된다. get 할 때 다운캐스팅 할 것
			for (ClassVO vo : classList) {
				vo.setRgstCnt(managerService.getRgstCountByClssId(vo.getClssId()));
			}
			model.addAttribute("classList", classList);
			List<CommonCodeVO> classCodeNameList = managerService.getCodeNameList("CLS");
			model.addAttribute("classCodeNameList", classCodeNameList);
			return "manager/class_list";
		}
	}

	//	교육 상세 조회
	@GetMapping("/class/{classId}")
	public String getClassDetail(Model model, @PathVariable String classId) {
		model.addAttribute("title", "교육 과정 상세");
		ClassVO thisClass = new ClassVO();
		thisClass = managerService.getClassDetailByClssId(classId);
		thisClass.setRgstCnt(managerService.getRgstCountByClssId(classId));
		model.addAttribute("clss", thisClass);
		List<Integer> imageFileSubIdList = managerService.getFileSubIdListByFileId(thisClass.getFileId());
		List<FileVO> NonImageFileInfoList = new ArrayList<FileVO>();
		for (int i = 0, j = 0; i < imageFileSubIdList.size(); i++) {
			FileVO vo = managerService.getFileInfoByIds(thisClass.getFileId(), imageFileSubIdList.get(i));
			if (vo.getFileType().split("/")[0].equals("image")) {
				j++;
			} else {
				NonImageFileInfoList.add(vo);
				imageFileSubIdList.remove(j);
			}
		}
		model.addAttribute("imageFileSubIdList", imageFileSubIdList);
		model.addAttribute("NonImageFileInfoList", NonImageFileInfoList);
		return "manager/class_detail";
	}

	// 교육 상세 정보 > 파일 요청
	@GetMapping("/{fileId}/{fileSubId}")
	public ResponseEntity<byte[]> getFile(@PathVariable String fileId, @PathVariable int fileSubId) {
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
	@GetMapping("/student/{classId}")
	public String getStudentList(Model model, HttpSession session, @PathVariable String classId) {
		model.addAttribute("title", "교육생 목록");
		model.addAttribute("thisClassName", (String) managerService.getClassNameByClssId(classId));//title 옆에 현재 수업 이름 출력
		model.addAttribute("thisClassId", classId);

		List<StudentInfoDTO> stdtList = managerService.getStudentListByClssId(classId);

		List<ClassVO> classNameList = managerService.getClassListByMngrId((String) session.getAttribute("mngrId"));//검색창에 수업 목록 넘김

		List<CommonCodeVO> classCodeNameList = managerService.getCodeNameList("CLS");
		model.addAttribute("classCodeNameList", classCodeNameList);//교육과정 상태 넘김
		
		List<CommonCodeVO> stdtCodeNameList = managerService.getCodeNameList("RST");
		model.addAttribute("stdtCodeNameList", stdtCodeNameList);//학생 등록 상태 넘김
		
		List<CommonCodeVO> cmptCodeNameList = managerService.getCodeNameList("CMP");
		model.addAttribute("cmptCodeNameList", cmptCodeNameList);//이수 여부 관련 상태 넘김
		
		List<CommonCodeVO> wlogCodeNameList = managerService.getCodeNameList("WOK");
		model.addAttribute("wlogCodeNameList", wlogCodeNameList);//출퇴근 상태 넘김


		for (StudentInfoDTO stdt : stdtList) {
			for (CommonCodeVO cmcd : wlogCodeNameList) {
				stdt.appendWlogCnt(String.valueOf(managerService.getCountByClssIdWlogCdStdtId(classId, cmcd.getCmcdId(), stdt.getStdtId())));
				stdt.appendWlogCnt(",");
			}
		}

		model.addAttribute("classNameList", classNameList);
		model.addAttribute("stdtList", stdtList);
		model.addAttribute("stdtCnt", stdtList.size());
		return "manager/student_list";
	}
	
	// clssId, wlogCd, stdtId 받아서 숫자 반환
	@GetMapping("/student/{clssId}/{wlogCd}/{stdtId}")
	public int getStudentList(Model model, HttpSession session, @PathVariable String clssId,@PathVariable String wlogCd, @PathVariable String stdtId) {
		return managerService.getCountByClssIdWlogCdStdtId(clssId, wlogCd, stdtId);
	}
	
	//	개인정보 조회
	@GetMapping("/mypage")
	public String getMngrInfo(Model model) {
		return "manager/mypage";
	}
}