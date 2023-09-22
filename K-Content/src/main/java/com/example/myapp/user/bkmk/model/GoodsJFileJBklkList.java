package com.example.myapp.user.bkmk.model;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
public class GoodsJFileJBklkList {
    private int goodsId;
    private String goodsName;
    private String goodsBrand;
    private int goodsPrice;
    private String goodsPurchsLink;
    private String goodsKwrd;
    private String goodsRegistDate;
    private String goodsUpdateDate;
    private char goodsDeleteYn;
    private String goodsFileId;
    private String goodsFilePath;
    private String goodsFileName;
    private String goodsBkmkRegistDate;
}
