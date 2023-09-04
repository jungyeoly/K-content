package com.example.myapp.cms.content.model;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Content {
    private int cntntId;
    private String cntntCateCode;
    private String cntntTitle;
    private String cntntKwrd;
    private String cntntUrl;
    private String cntntRegistDate;
    private String cntntUpdeDate;
    private char cntntDeleteYn;
    private String cntntThumnail;
}
