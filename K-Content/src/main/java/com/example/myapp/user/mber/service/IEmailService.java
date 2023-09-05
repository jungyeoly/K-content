package com.example.myapp.user.mber.service;

public interface IEmailService {

	String sendMaskId(String to) throws Exception;

	String sendAuthNum(String to) throws Exception;

	String sendTempPwd(String to) throws Exception;

	String maskMberId(String mberEmail);
}
