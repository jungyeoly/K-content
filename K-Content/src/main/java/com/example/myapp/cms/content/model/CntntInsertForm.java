package com.example.myapp.cms.content.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CntntInsertForm {
    private int cntntId;
    private String is;
    private String cntntUrl;
    private String cntntTitle;
    private List<String> keywordList;
    private List<Integer> goodsList;
    private String cntntCateCode;


}
