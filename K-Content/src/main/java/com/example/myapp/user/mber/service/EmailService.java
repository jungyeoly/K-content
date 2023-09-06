package com.example.myapp.user.mber.service;

import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.example.myapp.user.mber.dao.IMberRepository;
import com.example.myapp.user.mber.model.Mber;

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
		msg += "<h1 style=\"font-size: 30px; padding-right: 30px; padding-left: 30px;\">이메일 주소 확인</h1>";
		msg += "<p style=\"font-size: 17px; padding-right: 30px; padding-left: 30px;\">아래 확인 코드를 회원가입 화면에서 입력해주세요.</p>";
		msg += "<div style=\"padding-right: 30px; padding-left: 30px; margin: 32px 0 40px;\"><table style=\"border-collapse: collapse; border: 0; background-color: #F4F4F4; height: 70px; table-layout: fixed; word-wrap: break-word; border-radius: 6px;\"><tbody><tr><td style=\"text-align: center; vertical-align: middle; font-size: 30px;\">";
		msg += authNum;
		msg += "</td></tr></tbody></table></div>";
		return msg;
	}
	
	// 인증번호 메일 내용 생성
	private String generateMaskMberIdEmailContent(String maskId) {
		String msg = "";
		msg += "<h1 style=\"font-size: 30px; padding-right: 30px; padding-left: 30px;\">아이디 찾기</h1>";
		msg += "<p style=\"font-size: 17px; padding-right: 30px; padding-left: 30px;\">아래는 마스킹 처리된 아이디입니다. 확인 후에도 아이디를 알 수 없다면 고객센터로 문의주세요.</p>";
		msg += "<div style=\"padding-right: 30px; padding-left: 30px; margin: 32px 0 40px;\"><table style=\"border-collapse: collapse; border: 0; background-color: #F4F4F4; height: 70px; table-layout: fixed; word-wrap: break-word; border-radius: 6px;\"><tbody><tr><td style=\"text-align: center; vertical-align: middle; font-size: 30px;\">";
		msg += maskId;
		msg += "</td></tr></tbody></table></div>";
		return msg;
	}

	// 임시 비밀번호 메일 내용 생성
	private String generateTempPwdEmailContent(String tempPwd) {
		String msg = "";
		msg += "<h1 style=\"font-size: 30px; padding-right: 30px; padding-left: 30px;\">임시 비밀번호 안내</h1>";
		msg += "<p style=\"font-size: 17px; padding-right: 30px; padding-left: 30px;\">임시 비밀번호를 확인하세요. 임시비밀번호로 로그인 후 보안을 위해 반드시 비밀번호를 변경해주세요.</p>";
		msg += "<div style=\"padding-right: 30px; padding-left: 30px; margin: 32px 0 40px;\"><table style=\"border-collapse: collapse; border: 0; background-color: #F4F4F4; height: 70px; table-layout: fixed; word-wrap: break-word; border-radius: 6px;\"><tbody><tr><td style=\"text-align: center; vertical-align: middle; font-size: 30px;\">";
		msg += tempPwd;
		msg += "</td></tr></tbody></table></div>";
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
