package com.example.myapp.cms.goods.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Goods {
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
}
