package com.example.myapp.user.inqry.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Inqry {
	private int inqryId;
	private String inqryTitle;
	private String inqryCntnt;
	private int inqryPwd;
	private int inqryRefId;
	private String inqryRegistDate;
	private String inqryDeleteYn;
	private String inqryMberId;
	private int inqryGroupOrd;
	private String inqryFileId;
	private String inqryFilePath;
}
