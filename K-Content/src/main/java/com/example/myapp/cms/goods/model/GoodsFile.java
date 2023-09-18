package com.example.myapp.cms.goods.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GoodsFile {

    private String goodsFileID;
    private String goodsFileName;
    private long goodsFileSize;
    private String goodsFileExt;
    private String goodsFilePath;
    private String goodsFileRegistDate;
    private String goodsFileUpdateDate;
    private char goodsFileDeleteYn;
    private int goodsFileGoodsId;

}
