package com.example.myapp.user.mber.model;

import lombok.Data;

import java.sql.Date;

@Data
public class Mber {
	String mberId;
	String mberEmail;
	String mberPwd;
	String mberName;
	String mberBirth;
	String mberPhone;
	String mberRegistDate;
	String mberUpdateDate;
	String mberGenderCode;
	String mberStatCode;
}
