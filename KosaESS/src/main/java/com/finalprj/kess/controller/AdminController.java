package com.finalprj.kess.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.javassist.compiler.ast.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.finalprj.kess.service.IAdminService;

import jakarta.servlet.http.HttpSession;



@Controller
public class AdminController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	IAdminService adminService;

	/**
	 * @author : eunji
	 * @date : 2023. 8. 22.
	 * @parameter :
	 * @return :
	 */
	@RequestMapping("/login")
	public String login() {
		//로그인 하면서 역할 나누고 세션에 역할 저장하기
		return "login";
	}

	/**
	 * @author : eunji
	 * @date : 2023. 8. 22.
	 * @parameter : session, model
	 * @return : String
	 */
	@RequestMapping("/admin/main")
	public String main(HttpSession session, Model model) {
		//로그인 정보 저장
		session.setAttribute("id", "Admin");
		session.setAttribute("role", "System master");

		//화면 상단 타이틀 값 전달.
		String title = "메인";
		model.addAttribute("title", title);

		//교육과정 상태가 접수중인 행의 개수 가져오기
		int waitClassCnt = adminService.getWaitClassCnt();
		logger.warn("교육 접수중 : " +waitClassCnt);

		//문의답변 게시 상태가 답변대기인 행의 개수 가져오기
		int waitInquiryCnt = adminService.getWaitInquiryCnt();
		logger.warn("문의답변 대기 : " +waitInquiryCnt);

		//이수완료 대기 개수 가져오기
		int completeClassCnt = adminService.getCompleteClassCnt();
		logger.warn("이수완료 대기 : " +completeClassCnt);

		/*만족도 조사 등록 대기 개수 가져오기
		--추후 구현
		 */
		
		//subTitle 리스트로 전달하기
//		List<String> subTitleList = new ArrayList<>();
//		subTitleList.add("교육과정 접수중");
//		subTitleList.add("문의 답변 대기");
//		subTitleList.add("이수완료 대기");
//		model.addAttribute("subTitleList", subTitleList);

		//Count 리스트로 전달하기
//		List<Integer> cntList = new ArrayList<>();
//		cntList.add(waitClassCnt);
//		cntList.add(waitInquiryCnt);
//		cntList.add(completeClassCnt);
//		model.addAttribute("cntList", cntList);
	
		Map<String, Integer> dataMap = new HashMap<String, Integer>();
		dataMap.put("교육과정 접수중", waitClassCnt);
		dataMap.put("문의 답변 대기", waitInquiryCnt);
		dataMap.put("이수완료 대기", completeClassCnt);
		model.addAttribute("dataMap", dataMap);
		
		return "main_manager";
	}
}
