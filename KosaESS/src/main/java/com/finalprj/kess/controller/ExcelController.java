package com.finalprj.kess.controller;

import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor.HSSFColorPredefined;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.finalprj.kess.model.ClassVO;
import com.finalprj.kess.model.ManagerVO;
import com.finalprj.kess.model.PostVO;
import com.finalprj.kess.model.StudentVO;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/excel")
public class ExcelController {

	@RequestMapping("/admin/class")
	public void excelDownAdminClass(HttpSession session, HttpServletResponse response) throws Exception {

		// 교육과정 목록조회
		@SuppressWarnings("unchecked")
		List<ClassVO> list = (List<ClassVO>) session.getAttribute("searchClassList");

		System.out.println(list);

		// 워크북 생성
		Workbook wb = new HSSFWorkbook();
		Sheet sheet = wb.createSheet("교육과정");
		Row row = null;
		Cell cell = null;
		int rowNo = 0;

		// 테이블 헤더용 스타일
		CellStyle headStyle = wb.createCellStyle();

		// 가는 경계선을 가집니다.
		headStyle.setBorderTop(BorderStyle.THIN);
		headStyle.setBorderBottom(BorderStyle.THIN);
		headStyle.setBorderLeft(BorderStyle.THIN);
		headStyle.setBorderRight(BorderStyle.THIN);

		// 배경색은 하늘색입니다.
		headStyle.setFillForegroundColor(HSSFColorPredefined.LIGHT_CORNFLOWER_BLUE.getIndex());
		headStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		// 데이터는 가운데 정렬합니다.
		headStyle.setAlignment(HorizontalAlignment.CENTER);

		// 데이터용 경계 스타일 테두리만 지정
		CellStyle bodyStyle = wb.createCellStyle();
		bodyStyle.setBorderTop(BorderStyle.THIN);
		bodyStyle.setBorderBottom(BorderStyle.THIN);
		bodyStyle.setBorderLeft(BorderStyle.THIN);
		bodyStyle.setBorderRight(BorderStyle.THIN);

		// 헤더 생성
		row = sheet.createRow(rowNo++);
		cell = row.createCell(0);
		cell.setCellStyle(headStyle);
		cell.setCellValue("교육과정명");
		cell = row.createCell(1);
		cell.setCellStyle(headStyle);
		cell.setCellValue("기업");
		cell = row.createCell(2);
		cell.setCellStyle(headStyle);
		cell.setCellValue("교육상태");
		cell = row.createCell(3);
		cell.setCellStyle(headStyle);
		cell.setCellValue("지원기간");
		cell = row.createCell(4);
		cell.setCellStyle(headStyle);
		cell.setCellValue("교육기간");
		cell = row.createCell(5);
		cell.setCellStyle(headStyle);
		cell.setCellValue("교육시간");
		cell = row.createCell(6);
		cell.setCellStyle(headStyle);
		cell.setCellValue("교육장소");
		cell = row.createCell(7);
		cell.setCellStyle(headStyle);
		cell.setCellValue("수강가능인원");
		cell = row.createCell(8);
		cell.setCellStyle(headStyle);
		cell.setCellValue("업무담당자");

		// 데이터 부분 생성
		for (ClassVO vo : list) {
			row = sheet.createRow(rowNo++);
			cell = row.createCell(0);
			cell.setCellStyle(bodyStyle);
			cell.setCellValue(vo.getClssNm());
			cell = row.createCell(1);
			cell.setCellStyle(bodyStyle);
			cell.setCellValue(vo.getCmpyNm());
			cell = row.createCell(2);
			cell.setCellStyle(bodyStyle);
			cell.setCellValue(vo.getClssCdNm());
			cell = row.createCell(3);
			cell.setCellStyle(bodyStyle);
			cell.setCellValue(vo.getAplyStartDt() + "~" + vo.getAplyEndDt());
			cell = row.createCell(4);
			cell.setCellStyle(bodyStyle);
			cell.setCellValue(vo.getClssStartDd() + "~" + vo.getClssEndDd());
			cell = row.createCell(5);
			cell.setCellStyle(bodyStyle);
			cell.setCellValue(vo.getSetInTm() + "~" + vo.getSetOutTm());
			cell = row.createCell(6);
			cell.setCellStyle(bodyStyle);
			cell.setCellValue(vo.getClssAdr());
			cell = row.createCell(7);
			cell.setCellStyle(bodyStyle);
			cell.setCellValue(vo.getLimitCnt());
			cell = row.createCell(8);
			cell.setCellStyle(bodyStyle);
			cell.setCellValue(vo.getMngrNm());
		}

		// 엑셀 파일이름
		String fileName = "KOSA_교육과정_리스트.xls";
		String outputFileName = new String(fileName.getBytes("KSC5601"), "8859_1");

		// 컨텐츠 타입과 파일명 지정
		response.setContentType("ms-vnd/excel");
		response.setHeader("Content-Disposition", "attachment; fileName=\"" + outputFileName + "\"");

		// 엑셀 출력
		wb.write(response.getOutputStream());
		wb.close();
	}

	@RequestMapping("/admin/notice")
	public void excelDownAdminNotice(HttpSession session, HttpServletResponse response) throws Exception {

		// 공지사항 목록조회
		@SuppressWarnings("unchecked")
		List<PostVO> list = (List<PostVO>) session.getAttribute("searchNoticeList");

		System.out.println(list);

		// 워크북 생성
		Workbook wb = new HSSFWorkbook();
		Sheet sheet = wb.createSheet("공지사항");
		Row row = null;
		Cell cell = null;
		int rowNo = 0;

		// 테이블 헤더용 스타일
		CellStyle headStyle = wb.createCellStyle();

		// 가는 경계선을 가집니다.
		headStyle.setBorderTop(BorderStyle.THIN);
		headStyle.setBorderBottom(BorderStyle.THIN);
		headStyle.setBorderLeft(BorderStyle.THIN);
		headStyle.setBorderRight(BorderStyle.THIN);

		// 배경색은 하늘색입니다.
		headStyle.setFillForegroundColor(HSSFColorPredefined.LIGHT_CORNFLOWER_BLUE.getIndex());
		headStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		// 데이터는 가운데 정렬합니다.
		headStyle.setAlignment(HorizontalAlignment.CENTER);

		// 데이터용 경계 스타일 테두리만 지정
		CellStyle bodyStyle = wb.createCellStyle();
		bodyStyle.setBorderTop(BorderStyle.THIN);
		bodyStyle.setBorderBottom(BorderStyle.THIN);
		bodyStyle.setBorderLeft(BorderStyle.THIN);
		bodyStyle.setBorderRight(BorderStyle.THIN);

		// 헤더 생성
		row = sheet.createRow(rowNo++);
		cell = row.createCell(0);
		cell.setCellStyle(headStyle);
		cell.setCellValue("제목");
		cell = row.createCell(1);
		cell.setCellStyle(headStyle);
		cell.setCellValue("내용");
		cell = row.createCell(2);
		cell.setCellStyle(headStyle);
		cell.setCellValue("작성자");
		cell = row.createCell(3);
		cell.setCellStyle(headStyle);
		cell.setCellValue("등록일자");
		cell = row.createCell(4);
		cell.setCellStyle(headStyle);
		cell.setCellValue("조회수");
		cell = row.createCell(5);
		cell.setCellStyle(headStyle);
		cell.setCellValue("게시상태");

		// 데이터 부분 생성
		for (PostVO vo : list) {
			row = sheet.createRow(rowNo++);
			cell = row.createCell(0);
			cell.setCellStyle(bodyStyle);
			cell.setCellValue(vo.getPostTitle());
			cell = row.createCell(1);
			cell.setCellStyle(bodyStyle);
			cell.setCellValue(vo.getPostContent());
			cell = row.createCell(2);
			cell.setCellStyle(bodyStyle);
			cell.setCellValue(vo.getMngrNm());
			cell = row.createCell(3);
			cell.setCellStyle(bodyStyle);
			cell.setCellValue(vo.getRgstDt().toString());
			cell = row.createCell(4);
			cell.setCellStyle(bodyStyle);
			cell.setCellValue(vo.getPostHit());
			cell = row.createCell(5);
			cell.setCellStyle(bodyStyle);
			cell.setCellValue(vo.getCmcdNm());
		}

		// 엑셀 파일이름
		String fileName = "KOSA_공지사항_리스트.xls";
		String outputFileName = new String(fileName.getBytes("KSC5601"), "8859_1");

		// 컨텐츠 타입과 파일명 지정
		response.setContentType("ms-vnd/excel");
		response.setHeader("Content-Disposition", "attachment; fileName=\"" + outputFileName + "\"");

		// 엑셀 출력
		wb.write(response.getOutputStream());
		wb.close();
	}

	@RequestMapping("/admin/inquiry")
	public void excelDownAdminInquiry(HttpSession session, HttpServletResponse response) throws Exception {

		// 문의사항 목록조회
		@SuppressWarnings("unchecked")
		List<PostVO> list = (List<PostVO>) session.getAttribute("searchInquiryList");

		System.out.println(list);

		// 워크북 생성
		Workbook wb = new HSSFWorkbook();
		Sheet sheet = wb.createSheet("문의사항");
		Row row = null;
		Cell cell = null;
		int rowNo = 0;

		// 테이블 헤더용 스타일
		CellStyle headStyle = wb.createCellStyle();

		// 가는 경계선을 가집니다.
		headStyle.setBorderTop(BorderStyle.THIN);
		headStyle.setBorderBottom(BorderStyle.THIN);
		headStyle.setBorderLeft(BorderStyle.THIN);
		headStyle.setBorderRight(BorderStyle.THIN);

		// 배경색은 하늘색입니다.
		headStyle.setFillForegroundColor(HSSFColorPredefined.LIGHT_CORNFLOWER_BLUE.getIndex());
		headStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		// 데이터는 가운데 정렬합니다.
		headStyle.setAlignment(HorizontalAlignment.CENTER);

		// 데이터용 경계 스타일 테두리만 지정
		CellStyle bodyStyle = wb.createCellStyle();
		bodyStyle.setBorderTop(BorderStyle.THIN);
		bodyStyle.setBorderBottom(BorderStyle.THIN);
		bodyStyle.setBorderLeft(BorderStyle.THIN);
		bodyStyle.setBorderRight(BorderStyle.THIN);

		// 헤더 생성
		row = sheet.createRow(rowNo++);
		cell = row.createCell(0);
		cell.setCellStyle(headStyle);
		cell.setCellValue("제목");
		cell = row.createCell(1);
		cell.setCellStyle(headStyle);
		cell.setCellValue("내용");
		cell = row.createCell(2);
		cell.setCellStyle(headStyle);
		cell.setCellValue("작성자");
		cell = row.createCell(3);
		cell.setCellStyle(headStyle);
		cell.setCellValue("등록일자");
		cell = row.createCell(4);
		cell.setCellStyle(headStyle);
		cell.setCellValue("조회수");
		cell = row.createCell(5);
		cell.setCellStyle(headStyle);
		cell.setCellValue("답변상태");

		// 데이터 부분 생성
		for (PostVO vo : list) {
			row = sheet.createRow(rowNo++);
			cell = row.createCell(0);
			cell.setCellStyle(bodyStyle);
			cell.setCellValue(vo.getPostTitle());
			cell = row.createCell(1);
			cell.setCellStyle(bodyStyle);
			cell.setCellValue(vo.getPostContent());
			cell = row.createCell(2);
			cell.setCellStyle(bodyStyle);

			if (vo.getMngrNm() == null) {
				cell.setCellValue(vo.getStdtNm());
			} else if (vo.getStdtNm() == null) {
				cell.setCellValue(vo.getMngrNm());
			}

			// cell.setCellValue(vo.getMngrNm()+vo.getStdtNm());
			cell = row.createCell(3);
			cell.setCellStyle(bodyStyle);
			cell.setCellValue(vo.getRgstDt().toString());
			cell = row.createCell(4);
			cell.setCellStyle(bodyStyle);
			cell.setCellValue(vo.getPostHit());
			cell = row.createCell(5);
			cell.setCellStyle(bodyStyle);
			cell.setCellValue(vo.getCmcdNm());
		}

		// 엑셀 파일이름
		String fileName = "KOSA_문의사항_리스트.xls";
		String outputFileName = new String(fileName.getBytes("KSC5601"), "8859_1");

		// 컨텐츠 타입과 파일명 지정
		response.setContentType("ms-vnd/excel");
		response.setHeader("Content-Disposition", "attachment; fileName=\"" + outputFileName + "\"");

		// 엑셀 출력
		wb.write(response.getOutputStream());
		wb.close();
	}

	@RequestMapping("/admin/manager")
	public void excelDownAdminManager(HttpSession session, HttpServletResponse response) throws Exception {

		// 업무담당자 목록조회
		@SuppressWarnings("unchecked")
		List<ManagerVO> list = (List<ManagerVO>) session.getAttribute("searchManagerList");

		System.out.println(list);

		// 워크북 생성
		Workbook wb = new HSSFWorkbook();
		Sheet sheet = wb.createSheet("업무담당자");
		Row row = null;
		Cell cell = null;
		int rowNo = 0;

		// 테이블 헤더용 스타일
		CellStyle headStyle = wb.createCellStyle();

		// 가는 경계선을 가집니다.
		headStyle.setBorderTop(BorderStyle.THIN);
		headStyle.setBorderBottom(BorderStyle.THIN);
		headStyle.setBorderLeft(BorderStyle.THIN);
		headStyle.setBorderRight(BorderStyle.THIN);

		// 배경색은 하늘색입니다.
		headStyle.setFillForegroundColor(HSSFColorPredefined.LIGHT_CORNFLOWER_BLUE.getIndex());
		headStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		// 데이터는 가운데 정렬합니다.
		headStyle.setAlignment(HorizontalAlignment.CENTER);

		// 데이터용 경계 스타일 테두리만 지정
		CellStyle bodyStyle = wb.createCellStyle();
		bodyStyle.setBorderTop(BorderStyle.THIN);
		bodyStyle.setBorderBottom(BorderStyle.THIN);
		bodyStyle.setBorderLeft(BorderStyle.THIN);
		bodyStyle.setBorderRight(BorderStyle.THIN);

		// 헤더 생성
		row = sheet.createRow(rowNo++);
		cell = row.createCell(0);
		cell.setCellStyle(headStyle);
		cell.setCellValue("이름");
		cell = row.createCell(1);
		cell.setCellStyle(headStyle);
		cell.setCellValue("전화번호");
		cell = row.createCell(2);
		cell.setCellStyle(headStyle);
		cell.setCellValue("이메일");
		cell = row.createCell(3);
		cell.setCellStyle(headStyle);
		cell.setCellValue("계정상태");
		cell = row.createCell(4);
		cell.setCellStyle(headStyle);
		cell.setCellValue("마지막 로그인 일시");

		// 데이터 부분 생성
		for (ManagerVO vo : list) {
			row = sheet.createRow(rowNo++);
			cell = row.createCell(0);
			cell.setCellStyle(bodyStyle);
			cell.setCellValue(vo.getMngrNm());
			cell = row.createCell(1);
			cell.setCellStyle(bodyStyle);
			cell.setCellValue(vo.getMngrTel());
			cell = row.createCell(2);
			cell.setCellStyle(bodyStyle);
			cell.setCellValue(vo.getUserEmail());
			cell = row.createCell(3);
			cell.setCellStyle(bodyStyle);
			cell.setCellValue(vo.getCmcdNm());
			cell = row.createCell(4);
			cell.setCellStyle(bodyStyle);
			cell.setCellValue(String.valueOf(vo.getLastLoginDt()));
		}

		// 엑셀 파일이름
		String fileName = "KOSA_업무담당자_리스트.xls";
		String outputFileName = new String(fileName.getBytes("KSC5601"), "8859_1");

		// 컨텐츠 타입과 파일명 지정
		response.setContentType("ms-vnd/excel");
		response.setHeader("Content-Disposition", "attachment; fileName=\"" + outputFileName + "\"");

		// 엑셀 출력
		wb.write(response.getOutputStream());
		wb.close();
	}
	
	@RequestMapping("/admin/student")
	public void excelDownAdminStudent(HttpSession session, HttpServletResponse response) throws Exception {

		// 업무담당자 목록조회
		@SuppressWarnings("unchecked")
		List<StudentVO> list = (List<StudentVO>) session.getAttribute("searchStudentList");

		System.out.println(list);

		// 워크북 생성
		Workbook wb = new HSSFWorkbook();
		Sheet sheet = wb.createSheet("교육생");
		Row row = null;
		Cell cell = null;
		int rowNo = 0;

		// 테이블 헤더용 스타일
		CellStyle headStyle = wb.createCellStyle();

		// 가는 경계선을 가집니다.
		headStyle.setBorderTop(BorderStyle.THIN);
		headStyle.setBorderBottom(BorderStyle.THIN);
		headStyle.setBorderLeft(BorderStyle.THIN);
		headStyle.setBorderRight(BorderStyle.THIN);

		// 배경색은 하늘색입니다.
		headStyle.setFillForegroundColor(HSSFColorPredefined.LIGHT_CORNFLOWER_BLUE.getIndex());
		headStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		// 데이터는 가운데 정렬합니다.
		headStyle.setAlignment(HorizontalAlignment.CENTER);

		// 데이터용 경계 스타일 테두리만 지정
		CellStyle bodyStyle = wb.createCellStyle();
		bodyStyle.setBorderTop(BorderStyle.THIN);
		bodyStyle.setBorderBottom(BorderStyle.THIN);
		bodyStyle.setBorderLeft(BorderStyle.THIN);
		bodyStyle.setBorderRight(BorderStyle.THIN);

		// 헤더 생성
		row = sheet.createRow(rowNo++);
		cell = row.createCell(0);
		cell.setCellStyle(headStyle);
		cell.setCellValue("이름");
		cell = row.createCell(1);
		cell.setCellStyle(headStyle);
		cell.setCellValue("이메일");
		cell = row.createCell(2);
		cell.setCellStyle(headStyle);
		cell.setCellValue("성별");
		cell = row.createCell(3);
		cell.setCellStyle(headStyle);
		cell.setCellValue("생년월일");
		cell = row.createCell(4);
		cell.setCellStyle(headStyle);
		cell.setCellValue("전화번호");
		cell = row.createCell(5);
		cell.setCellStyle(headStyle);
		cell.setCellValue("직업");
		cell = row.createCell(6);
		cell.setCellStyle(headStyle);
		cell.setCellValue("계정상태");
		cell = row.createCell(7);
		cell.setCellStyle(headStyle);
		cell.setCellValue("마지막 로그인 일시");

		// 데이터 부분 생성
		for (StudentVO vo : list) {
			row = sheet.createRow(rowNo++);
			cell = row.createCell(0);
			cell.setCellStyle(bodyStyle);
			cell.setCellValue(vo.getStdtNm());
			cell = row.createCell(1);
			cell.setCellStyle(bodyStyle);
			cell.setCellValue(vo.getUserEmail());
			cell = row.createCell(2);
			cell.setCellStyle(bodyStyle);
			cell.setCellValue(vo.getGenderNm());
			cell = row.createCell(3);
			cell.setCellStyle(bodyStyle);
			cell.setCellValue(String.valueOf(vo.getBirthDd()));
			cell = row.createCell(4);
			cell.setCellStyle(bodyStyle);
			cell.setCellValue(vo.getStdtTel());
			cell = row.createCell(5);
			cell.setCellStyle(bodyStyle);
			cell.setCellValue(vo.getJobNm());
			cell = row.createCell(6);
			cell.setCellStyle(bodyStyle);
			cell.setCellValue(vo.getUserNm());
			cell = row.createCell(7);
			cell.setCellStyle(bodyStyle);
			cell.setCellValue(String.valueOf(vo.getLastLoginDt()));
		}

		// 엑셀 파일이름
		String fileName = "KOSA_교육생_리스트.xls";
		String outputFileName = new String(fileName.getBytes("KSC5601"), "8859_1");

		// 컨텐츠 타입과 파일명 지정
		response.setContentType("ms-vnd/excel");
		response.setHeader("Content-Disposition", "attachment; fileName=\"" + outputFileName + "\"");

		// 엑셀 출력
		wb.write(response.getOutputStream());
		wb.close();
	}
}
