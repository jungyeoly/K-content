package com.example.myapp.cms.goods.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageBox {
    private int totalPageCount;
    private int nowPage;
    private int totalPageBlock;
    private int nowPageBlock;
    private int startPage;
    private int endPage;

}
