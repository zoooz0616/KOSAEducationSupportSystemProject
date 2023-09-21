package com.finalprj.kess.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class MailService implements IMailService {

	@Autowired
	private JavaMailSender javaMailSender;

	private String from="zoooz0616@naver.com";


	@Override
	public void sendMail(String recipient, String clssNm) throws Exception {
		MimeMessage m = javaMailSender.createMimeMessage();
		MimeMessageHelper h = new MimeMessageHelper(m,"UTF-8");
		h.setFrom(from);
		h.setTo(recipient);
		h.setSubject("KOSA 교육과정지원 결과발표 안내");

		// HTML 형식의 본문 설정
		String htmlContent = "<html><body>"
				+ "<h1>안녕하세요, KOSA 교육과정지원 결과발표 안내입니다.</h1>"
				+ "<h3>지원해주신 <span style='color: blue;'>"+ clssNm +"</span> 교육과정에 합격되신 것을 진심으로 축하드립니다.</h3>"
				+ "<a href='localhost:8080/student'>홈페이지 바로가기</a>"
				+ "</body></html>";

		h.setText(htmlContent, true);
		javaMailSender.send(m);
	}

}
