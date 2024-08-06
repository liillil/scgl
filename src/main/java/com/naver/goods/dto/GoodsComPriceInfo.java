package com.naver.goods.dto;

import lombok.Data;

@Data
public class GoodsComPriceInfo {

    private String storeName;

    private String clientId;

    private String clientSecret;

    private String accountId;

    private String goodsNo;

    private String comStoreId;

    private Integer goodsLimitPrice;
}
