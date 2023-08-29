package com.example.myapp.user.inqry.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InqryFile {
	private String inqryFileId;
	private String inqryFileName;
	private long inqryFileSize;
	private String inqryFileExt;
	private String inqryFilePath;
	private String inqryFileRegistDate;
	private String inqryFileUpdateDate;
	private String inqryFileDeleteYn;
	private int inqryFileInqryId;
}
