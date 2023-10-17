package com.example.myapp.user.mber.service;

import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.example.myapp.user.mber.dao.IMberRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@PropertySource("classpath:application.properties")
@Slf4j
@RequiredArgsConstructor
@Service
public class EmailService implements IEmailService {

	@Autowired
	IMberRepository mberRepository;

	private final JavaMailSender javaMailSender;

	private String authNum;
	private String maskId;
	private String tempPwd;

	Date now = new Date();
	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 날짜 포맷을 지정
	String nowTime = dateFormat.format(now); // 현재 날짜를 포맷에 맞게 문자열로 변환
	
	@Value("${spring.mail.username}")
	private String id;

	// 공통적인 이메일 작성
	private MimeMessage createEmail(String to, String subject, String content)
			throws MessagingException, UnsupportedEncodingException {
		MimeMessage message = javaMailSender.createMimeMessage();

		message.addRecipients(MimeMessage.RecipientType.TO, to);
		message.setSubject(subject);

		String msg = content;

		message.setText(msg, "utf-8", "html");
		message.setFrom(new InternetAddress(id, "K-Spectrum"));

		return message;
	}

	// 인증번호 메일 생성
	public MimeMessage createAuthEmail(String to) throws MessagingException, UnsupportedEncodingException {
		authNum = createAuthNum();
		String subject = "[K-Spectrum] 회원가입 이메일 인증 안내 ";
		String content = generateAuthEmailContent(authNum);
		log.info("보내는 대상 : " + to);
		log.info("인증 번호 : " + authNum);
		return createEmail(to, subject, content);
	}

	// 임시 비밀번호 메일 생성
	public MimeMessage createMaskMberIdEmail(String to) throws MessagingException, UnsupportedEncodingException {
		maskId = createTempPwd();
		String subject = "[K-Spectrum] 임시 비밀번호 안내 ";
		String content = generateTempPwdEmailContent(maskId);
		log.info("보내는 대상 : " + to);
		log.info("임시비밀번호 : " + tempPwd);
		return createEmail(to, subject, content);
	}

	// 임시 비밀번호 메일 생성
	public MimeMessage createTempPwdEmail(String to) throws MessagingException, UnsupportedEncodingException {
		tempPwd = createTempPwd();
		String subject = "[K-Spectrum] 임시 비밀번호 안내 ";
		String content = generateTempPwdEmailContent(tempPwd);
		log.info("보내는 대상 : " + to);
		log.info("임시비밀번호 : " + tempPwd);
		return createEmail(to, subject, content);
	}

	// 인증번호 메일 내용 생성
	private String generateAuthEmailContent(String authNum) {
		 
		String msg = "";
		msg += "<div><div style=\"width:700px;margin : 50px auto 0;border: 1px solid #D5D9E2;font-family:'Noto Sans KR',sans-serif;\">";
		msg += "<div style=\"height: 96px;\">";
		msg += "<img src=\"https://i.ibb.co/XxH7vsm/mainLogo.png\" alt=\"K-Spectrum\" width=\"140\" height=\"80\" style=\"float: left; padding:5px 0 0 25px;\" loading=\"lazy\">";
		msg += "<span style=\"float:right; font-size: 16px;align-self: flex-end;color: #777777; padding: 52px 20px 0 0;\">다양한 분야, 많은 컨텐츠! K-Spectrum</span>";
		msg += "</div>";
		msg += "<div style=\"margin-bottom: 8px;height: 95px;background-color: #14dbc8;\">";
		msg += "<p style=\"font-size: 32px;font-weight: 500; padding-top: 25px; color: #ffffff;margin:0 0 0 40px; padding-top:25px;\">K-Spectrum 임시 인증번호 안내</p>";
		msg += "</div>";
		msg += "<div style=\"border-top: 1px solid #eeeeee; border-bottom: 1px solid #eeeeee; padding-left: 40px;\">";
		msg += "<div style=\"margin-top: 40px;\">";
		msg += "<p style=\"font-size: 15px; line-height: 1.5; color: #333333; margin-bottom: 35px;\">안녕하세요!<br> 다양한 분야 많은 컨텐츠, K-Spectrum를 이용해 주셔서 감사드립니다.</p>";
		msg += "<p style=\"font-size: 15px; line-height: 1.5; color: #333333; margin-bottom: 35px;\">K-Spectrum 회원가입 이메일 인증 메일 전달드립니다.<br>";
		msg += "</div>";
		msg += "<table style=\"width: 620px; height: 170px; border-top: 2px solid #14dbc8; border-collapse: collapse; border-spacing: 0;\">";
		msg += "<tbody><tr style=\"border-bottom: 1px solid #eeeeee;\"><td style=\"background-color: #f6f6f6; padding-left: 24px; width: 30%; color: #111111; font-weight: bold;\">인증번호</td><td style=\"height: 56px; padding-left: 24px;font-size: 16px;\">";
		msg += authNum;
		msg += "</td></tr>";
		msg += "<tr style=\"border-bottom: 1px solid #eeeeee;\"><td style=\"background-color: #f6f6f6;padding-left: 24px;width: 30%;color: #111111;font-weight: bold;\">발송일자</td><td style=\"height: 56px; padding-left: 24px;font-size: 16px;\">";
		msg += nowTime;
		msg += "</td></tr>";
		msg += "</tbody></table>";
		msg += "<div style=\"height: 152px; margin-top: 43px; text-align:center;\">";
		msg += "<a href=\"http://localhost:8080/mber/signin\" style=\": 240px;: 48px;padding: 10px 20px;text-decoration: none;background-color: #14dbc8;outline: none;border: none;color: #ffffff;font-size: 16px; cursor: pointer;\" rel=\"noreferrer noopener\" target=\"_blank\">";
		msg += "K-Spectrum 로그인하기";
		msg += "</a>";
		msg += "</div>";
		msg += "</div>";
		msg += "<div style=\"display: flex; align-items: center; height: 120px; margin-left: 40px;\">";
		msg += "<p style=\"font-size: 11px; color: #777777; margin-top: 40px;\">K-Spectrum 회원가입 및 서비스 이용 시 제공하여 주신 소중한 개인정보는 안전하게 보호되고 있습니다.<br>";
		msg += "본 메일은 발신전용으로 회신을 통한 문의는 처리되지 않으며, 문의사항은 고객센터(kspectrumkorea@gmail.com)로 연락주시기 바랍니다.</p>";
		msg += "</div>";
		msg += "<div style=\"margin: 0 auto; height: 132px;background-color: #eeeeee;text-align: center;\">";
		msg += "<div style=\"padding-top: 15px;\">";
		msg += "<p style=\"font-size: 12px; color: #777777;\">서울특별시 종로구 창경궁로 254 동원빌딩 4층 403호 (주)K-Spectrum<br>";
		msg += "대표: 한국소프트산업협회  | 사업자등록번호: 111-22-33333 | 대표전화: 02-2188-6900<br>";
		msg += "<a href=\"#\" target=\"_blank\" style=\"color: #777777;\" rel=\"noreferrer noopener\">이용약관</a> |";
		msg += "<a href=\"#\" target=\"_blank\" style=\"color: #777777;\" rel=\"noreferrer noopener\">개인정보취급방침</a> |";
		msg += "<br>";
		msg += "<span style=\"font-weight: 600;\">COPYRIGHT © K-Spectrum. All rights reserved. Since 2023</span></p></div>";
		msg += "</div>";
		msg += "</div>";
		msg += "</div>";
		
		return msg;
	}

	// 아이디찾기 메일 내용 생성
	private String generateMaskMberIdEmailContent(String maskId) {
		String msg = "";
		msg += "<div><div style=\"width:700px;margin : 50px auto 0;border: 1px solid #D5D9E2;font-family:'Noto Sans KR',sans-serif;\">";
		msg += "<div style=\"height: 96px;\">";
		msg += "<img src=\"https://i.ibb.co/XxH7vsm/mainLogo.png\" alt=\"K-Spectrum\" width=\"120\" height=\"80\" style=\"float: left; padding:5px 0 0 25px;\" loading=\"lazy\">";
		msg += "<span style=\"float:right; font-size: 16px;align-self: flex-end;color: #777777; padding: 52px 20px 0 0;\">다양한 분야, 많은 컨텐츠! K-Spectrum</span>";
		msg += "</div>";
		msg += "<div style=\"margin-bottom: 8px;height: 95px;background-color: #14dbc8;\">";
		msg += "<p style=\"font-size: 32px;font-weight: 500; padding-top: 25px; color: #ffffff;margin:0 0 0 40px; padding-top:25px;\">K-Spectrum 아이디 찾기 안내</p>";
		msg += "</div>";
		msg += "<div style=\"border-top: 1px solid #eeeeee; border-bottom: 1px solid #eeeeee; padding-left: 40px;\">";
		msg += "<div style=\"margin-top: 40px;\">";
		msg += "<p style=\"font-size: 15px; line-height: 1.5; color: #333333; margin-bottom: 35px;\">안녕하세요!<br> 다양한 분야 많은 컨텐츠, K-Spectrum를 이용해 주셔서 감사드립니다.</p>";
		msg += "<p style=\"font-size: 15px; line-height: 1.5; color: #333333; margin-bottom: 35px;\">K-Spectrum 아이디 찾기 메일 전달드립니다.<br>";
		msg += "</div>";
		msg += "<table style=\"width: 620px; height: 170px; border-top: 2px solid #0092d3; border-collapse: collapse; border-spacing: 0;\">";
		msg += "<tbody><tr style=\"border-bottom: 1px solid #eeeeee;\"><td style=\"background-color: #f6f6f6; padding-left: 24px; width: 30%; color: #111111; font-weight: bold;\">마스킹 처리 아이디</td><td style=\"height: 56px; padding-left: 24px;font-size: 16px;\">";
		msg += maskId;
		msg += "</td></tr>";
		msg += "<tr style=\"border-bottom: 1px solid #eeeeee;\"><td style=\"background-color: #f6f6f6;padding-left: 24px;width: 30%;color: #111111;font-weight: bold;\">발송일자</td><td style=\"height: 56px; padding-left: 24px;font-size: 16px;\">";
		msg += nowTime;
		msg += "</td></tr>";
		msg += "</tbody></table>";
		msg += "<div style=\"height: 152px; margin-top: 43px; text-align:center;\">";
		msg += "<a href=\"http://localhost:8080/mber/signin\" style=\": 240px;: 48px;padding: 10px 20px;text-decoration: none;background-color: #14dbc8;outline: none;border: none;color: #ffffff;font-size: 16px; cursor: pointer;\" rel=\"noreferrer noopener\" target=\"_blank\">";
		msg += "K-Spectrum 로그인하기";
		msg += "</a>";
		msg += "</div>";
		msg += "</div>";
		msg += "<div style=\"display: flex; align-items: center; height: 120px; margin-left: 40px;\">";
		msg += "<p style=\"font-size: 11px; color: #777777; margin-top: 40px;\">K-Spectrum 회원가입 및 서비스 이용 시 제공하여 주신 소중한 개인정보는 안전하게 보호되고 있습니다.<br>";
		msg += "본 메일은 발신전용으로 회신을 통한 문의는 처리되지 않으며, 문의사항은 고객센터(kspectrumkorea@gmail.com)로 연락주시기 바랍니다.</p>";
		msg += "</div>";
		msg += "<div style=\"margin: 0 auto; height: 132px;background-color: #eeeeee;text-align: center;\">";
		msg += "<div style=\"padding-top: 15px;\">";
		msg += "<p style=\"font-size: 12px; color: #777777;\">서울특별시 종로구 창경궁로 254 동원빌딩 4층 403호 (주)K-Spectrum<br>";
		msg += "대표: 한국소프트산업협회  | 사업자등록번호: 111-22-33333 | 대표전화: 02-2188-6900<br>";
		msg += "<a href=\"#\" target=\"_blank\" style=\"color: #777777;\" rel=\"noreferrer noopener\">이용약관</a> |";
		msg += "<a href=\"#\" target=\"_blank\" style=\"color: #777777;\" rel=\"noreferrer noopener\">개인정보취급방침</a> |";
		msg += "<br>";
		msg += "<span style=\"font-weight: 600;\">COPYRIGHT © K-Spectrum. All rights reserved. Since 2023</span></p></div>";
		msg += "</div>";
		msg += "</div>";
		msg += "</div>";
		return msg;
	}

	// 임시 비밀번호 메일 내용 생성
	private String generateTempPwdEmailContent(String tempPwd) {
		String msg = "";
		msg += "<div><div style=\"width:700px;margin : 50px auto 0;border: 1px solid #D5D9E2;font-family:'Noto Sans KR',sans-serif;\">";
		msg += "<div style=\"height: 96px;\">";
		msg += "<img src=\"https://i.ibb.co/XxH7vsm/mainLogo.png\" alt=\"K-Spectrum\" width=\"120\" height=\"80\" style=\"float: left; padding:5px 0 0 25px;\" loading=\"lazy\">";
		msg += "<span style=\"float:right; font-size: 16px;align-self: flex-end;color: #777777; padding: 52px 20px 0 0;\">다양한 분야, 많은 컨텐츠! K-Spectrum</span>";
		msg += "</div>";
		msg += "<div style=\"margin-bottom: 8px;height: 95px;background-color: #14dbc8;\">";
		msg += "<p style=\"font-size: 32px;font-weight: 500; padding-top: 25px; color: #ffffff;margin:0 0 0 40px; padding-top:25px;\">K-Spectrum 임시 비밀번호 안내</p>";
		msg += "</div>";
		msg += "<div style=\"border-top: 1px solid #eeeeee; border-bottom: 1px solid #eeeeee; padding-left: 40px;\">";
		msg += "<div style=\"margin-top: 40px;\">";
		msg += "<p style=\"font-size: 15px; line-height: 1.5; color: #333333; margin-bottom: 35px;\">안녕하세요!<br> 다양한 분야 많은 컨텐츠, K-Spectrum를 이용해 주셔서 감사드립니다.</p>";
		msg += "<p style=\"font-size: 15px; line-height: 1.5; color: #333333; margin-bottom: 35px;\">K-Spectrum 비밀번호 찾기를 통한 임시비밀번호 메일 전달드립니다.";
		msg += "<p style=\"font-size: 15px; line-height: 1.5; color: #333333; margin-bottom: 35px;\">본 비밀번호는 임시 비밀번호이므로 보안이 취약하니, 최대한 빠른 시일 내 변경을 권장합니다.<br>";
		msg += "</div>";
		msg += "<table style=\"width: 620px; height: 170px; border-top: 2px solid #0092d3; border-collapse: collapse; border-spacing: 0;\">";
		msg += "<tbody><tr style=\"border-bottom: 1px solid #eeeeee;\"><td style=\"background-color: #f6f6f6; padding-left: 24px; width: 30%; color: #111111; font-weight: bold;\">임시 비밀번호</td><td style=\"height: 56px; padding-left: 24px;font-size: 16px;\">";
		msg += tempPwd;
		msg += "</td></tr>";
		msg += "<tr style=\"border-bottom: 1px solid #eeeeee;\"><td style=\"background-color: #f6f6f6;padding-left: 24px;width: 30%;color: #111111;font-weight: bold;\">발송일자</td><td style=\"height: 56px; padding-left: 24px;font-size: 16px;\">";
		msg += nowTime;
		msg += "</td></tr>";
		msg += "</tbody></table>";
		msg += "<div style=\"height: 152px; margin-top: 43px; text-align:center;\">";
		msg += "<a href=\"http://localhost:8080/mber/signin\" style=\": 240px;: 48px;padding: 10px 20px;text-decoration: none;background-color: #14dbc8;outline: none;border: none;color: #ffffff;font-size: 16px; cursor: pointer;\" rel=\"noreferrer noopener\" target=\"_blank\">";
		msg += "K-Spectrum 로그인하기";
		msg += "</a>";
		msg += "</div>";
		msg += "</div>";
		msg += "<div style=\"display: flex; align-items: center; height: 120px; margin-left: 40px;\">";
		msg += "<p style=\"font-size: 11px; color: #777777; margin-top: 40px;\">K-Spectrum 회원가입 및 서비스 이용 시 제공하여 주신 소중한 개인정보는 안전하게 보호되고 있습니다.<br>";
		msg += "본 메일은 발신전용으로 회신을 통한 문의는 처리되지 않으며, 문의사항은 고객센터(kspectrumkorea@gmail.com)로 연락주시기 바랍니다.</p>";
		msg += "</div>";
		msg += "<div style=\"margin: 0 auto; height: 132px;background-color: #eeeeee;text-align: center;\">";
		msg += "<div style=\"padding-top: 15px;\">";
		msg += "<p style=\"font-size: 12px; color: #777777;\">서울특별시 종로구 창경궁로 254 동원빌딩 4층 403호 (주)K-Spectrum<br>";
		msg += "대표: 한국소프트산업협회  | 사업자등록번호: 111-22-33333 | 대표전화: 02-2188-6900<br>";
		msg += "<a href=\"#\" target=\"_blank\" style=\"color: #777777;\" rel=\"noreferrer noopener\">이용약관</a> |";
		msg += "<a href=\"#\" target=\"_blank\" style=\"color: #777777;\" rel=\"noreferrer noopener\">개인정보취급방침</a> |";
		msg += "<br>";
		msg += "<span style=\"font-weight: 600;\">COPYRIGHT © K-Spectrum. All rights reserved. Since 2023</span></p></div>";
		msg += "</div>";
		msg += "</div>";
		msg += "</div>";
		
		return msg;
	}

	// 인증코드 만들기
	public static String createAuthNum() {
		StringBuffer key = new StringBuffer();
		Random rnd = new Random();

		for (int i = 0; i < 6; i++) { // 인증코드 6자리
			key.append((rnd.nextInt(10)));
		}
		return key.toString();
	}

	// 임시비밀번호 만들기
	@Override
	public String createTempPwd() {
		char[] charSet = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
				'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };

		SecureRandom random = new SecureRandom();
		StringBuilder strBuilder = new StringBuilder();

		// 랜덤한 숫자 하나 추가
		strBuilder.append(charSet[random.nextInt(10)]);

		// 랜덤한 소문자 하나 추가
		strBuilder.append(Character.toLowerCase(charSet[10 + random.nextInt(26)]));

		// 랜덤한 대문자 하나 추가
		strBuilder.append(charSet[10 + random.nextInt(26)]);

		// 나머지 문자들 추가하여 길이가 6이 될 때까지
		for (int i = 0; i < 3; i++) {
			int idx = random.nextInt(charSet.length);
			strBuilder.append(charSet[idx]);
		}

		// 문자들을 랜덤하게 섞음
		for (int i = strBuilder.length() - 1; i > 0; i--) {
			int j = random.nextInt(i + 1);
			char temp = strBuilder.charAt(i);
			strBuilder.setCharAt(i, strBuilder.charAt(j));
			strBuilder.setCharAt(j, temp);
		}

		return strBuilder.toString();
	}

	// 메일 전송 공통 메서드
	private void sendEmail(MimeMessage message) throws Exception {
		try {
			javaMailSender.send(message);
		} catch (MailException es) {
			es.printStackTrace();
			throw new IllegalArgumentException();
		}
	}

	@Override
	public String sendMaskId(String to) throws Exception {
		maskId = maskMberId(to);
		String subject = "[K-Spectrum] 아이디 찾기 ";
		String content = generateMaskMberIdEmailContent(maskId);

		MimeMessage message = createEmail(to, subject, content);
		sendEmail(message);

		return maskId;
	}

	@Override
	public String sendAuthNum(String to) throws Exception {
		authNum = createAuthNum();
		String subject = "[K-Spectrum] 회원가입 이메일 인증 안내 ";
		String content = generateAuthEmailContent(authNum);

		MimeMessage message = createEmail(to, subject, content);
		sendEmail(message);

		return authNum;
	}

	@Override
	public String sendTempPwd(String to) throws Exception {
		tempPwd = createTempPwd();
		String subject = "[K-Spectrum] 임시 비밀번호 안내 ";
		String content = generateTempPwdEmailContent(tempPwd);

		MimeMessage message = createEmail(to, subject, content);
		sendEmail(message);

		return tempPwd;
	}

	@Override
	public String maskMberId(String mberEmail) {
		return mberRepository.maskMberId(mberEmail);
	}

}
