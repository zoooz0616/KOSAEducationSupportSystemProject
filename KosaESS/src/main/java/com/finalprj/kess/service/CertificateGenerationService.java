package com.finalprj.kess.service;

import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Map;

import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finalprj.kess.dto.RegistrationDTO;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;

@Service
public class CertificateGenerationService {

	@SuppressWarnings("unused")
	private final StudentService studentService;

	@Autowired
	public CertificateGenerationService(StudentService studentService) {
		this.studentService = studentService;
	}

	@SuppressWarnings("unused")
	public byte[] generatePdfCertificate(RegistrationDTO rgst) throws DocumentException, IOException {
		SimpleDateFormat sdf = new SimpleDateFormat("YYYY.MM.dd");
		SimpleDateFormat sdf2 = new SimpleDateFormat("YYYY년 MM월 dd일");

		String name = (String) rgst.getStdtNm();
		String classNm = (String) rgst.getClssNm();
		String cmptNm = (String) rgst.getCmptNm();
		Date classSt = rgst.getClssStartDd();
		String classStDate = sdf.format(classSt);
		Date classEt = rgst.getClssEndDd();
		String classEtDate = sdf.format(classEt);
		int classTt = rgst.getClssTotalTm();
		Timestamp printDd = rgst.getPrintDd();
		String printDate = sdf2.format(printDd);

		Font TitleFont = null;
		Font ContentFont = null;
		Font MainFont = null;
		Font DateFont = null;
		BaseFont bf = BaseFont.createFont("static/font/KoPubDotumLight.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

		ContentFont = new Font(bf, 20);
		ContentFont.setColor(BaseColor.BLACK);
		
		TitleFont = new Font(bf, 45);
		TitleFont.setColor(BaseColor.BLACK);
		TitleFont.setStyle(Font.BOLD);
		
		MainFont = new Font(bf, 27);
		MainFont.setColor(BaseColor.BLACK);
		
		DateFont = new Font(bf, 20);
		DateFont.setColor(BaseColor.BLACK);

		// PDF 생성 작업 수행 (예: iText 사용)
		Document document = new Document();
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		PdfWriter.getInstance(document, os);

		String imageUrl = "src/main/resources/static/img/logo.png";

		// 이미지 객체 생성
		Image image = Image.getInstance(imageUrl);

		image.scaleAbsolute(300f, 80f);

		// 이미지 삽입 위치 설정 (x, y 좌표)
		image.setAbsolutePosition(150f, 50f);

		document.open();
		Paragraph TitlePr = new Paragraph("이수증", TitleFont);
		TitlePr.setAlignment(Element.ALIGN_CENTER); 
		TitlePr.setSpacingBefore(60f);
		TitlePr.setSpacingAfter(80f);
		document.add(TitlePr);
		Paragraph ContentPr1 = new Paragraph("성      명 : " + name, ContentFont);
		ContentPr1.setSpacingAfter(10f); 
		ContentPr1.setIndentationLeft(20f);
		document.add(ContentPr1);
		Paragraph ContentPr2 = new Paragraph("교육과정 : " + classNm + "과정", ContentFont);
		ContentPr2.setSpacingAfter(10f); 
		ContentPr2.setIndentationLeft(20f);
		document.add(ContentPr2);
		Paragraph ContentPr3 = new Paragraph("이수시간 : " + classTt + "시간", ContentFont);
		ContentPr3.setSpacingAfter(10f); 
		ContentPr3.setIndentationLeft(20f);
		document.add(ContentPr3);
		Paragraph ContentPr4 = new Paragraph("교육일자 : " + classStDate + " ~ " + classEtDate, ContentFont);
		ContentPr4.setSpacingAfter(80f);
		ContentPr4.setIndentationLeft(20f);
		document.add(ContentPr4);
		Paragraph Mainpr = new Paragraph("위 사람은 KOSA 교육지원시스템에서\n 실시한 교육과정을 성실하게\n 이수하였기에 이 증서를 드립니다.", MainFont);
		Mainpr.setSpacingAfter(120f);
		Mainpr.setAlignment(Element.ALIGN_CENTER); // 중앙 정렬
		document.add(Mainpr);
		Paragraph DatePr = new Paragraph(printDate, DateFont);
		DatePr.setAlignment(Element.ALIGN_CENTER); 
		DatePr.setSpacingAfter(10f);
		document.add(DatePr);

		// 이미지 추가
		document.add(image);

		document.close();

		return os.toByteArray();
	}
}
