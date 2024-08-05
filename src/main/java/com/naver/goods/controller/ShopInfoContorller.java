package com.naver.goods.controller;

import com.naver.goods.dto.InfoResult;
import com.naver.goods.service.ShopInfoService;
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
@RequestMapping("/shop")
public class ShopInfoContorller {

    @Autowired
    private ShopInfoService shopInfoService;

    @GetMapping("/findAll")
    public InfoResult findAll(){
        return   shopInfoService.findAllShop();
    }

}
