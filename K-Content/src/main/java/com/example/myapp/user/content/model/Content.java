package com.example.myapp.user.content.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Content {
	private int cntntId;
    private String cntntTitle;
    private String cntntKwrd;
    private String cntntUrl;
    private String cntntRegistDate;
    private String cntntUpdateDate;
    private String cntntDeleteYn;
    private String cntntCateCode;

    private int goodsId;
    private String goodsName;
    private String goodsBrand;
    private int goodsPrice;
    private String goodsPurchsLink;
    private String goodsKwrd;
    private String goodsRegistDate;
    private String goodsUpdateDate;
    private String goodsDeleteYn;

    private String commonCodeVal;
    private String cntntThumnail;
}
