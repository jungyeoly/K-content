package com.example.myapp.user.mber.service;

public interface IEmailService {

	String sendAuthNum(String to) throws Exception;

	String sendTempPwd(String to) throws Exception;
}
