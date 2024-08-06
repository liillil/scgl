package com.naver.goods.controller;

import com.naver.goods.dto.InfoResult;
import com.naver.goods.dto.InfoResults;
import com.naver.goods.service.StoreInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: scgl
 * @ClassName: ShopInfoContorller
 * @description:
 * @author: ligang
 * @create: 2024-08-05 20:53:16
 */
@RestController
@RequestMapping("/store")
public class StoreInfoContorller {

    @Autowired
    private StoreInfoService storeInfoService;

    @GetMapping("/findAll")
    public InfoResult findAll() {
        return storeInfoService.findAllShop();
    }

    @GetMapping("/getGoodsComPriceInfo")
    public InfoResult getGoodsComPriceInfo() {
        return InfoResults.ok(storeInfoService.getGoodsComPriceInfo());
    }
}
