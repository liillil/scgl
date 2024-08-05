package com.naver.goods.entity;

import lombok.Data;

/**
 * @ClassName: ShopInfoTb
 * @Description:
 * @Author: lg
 * @Date: 2024/8/4 16:42
 */
@Data
public class ShopInfoTb {

    private Long id;

    private String shopName;

    private String clientId;

    private String clientSecret;

    private String accountId;

    private Long createTime; //创建时间

    private Integer comparePrices; //比较价格 1比较 2不比较

}
