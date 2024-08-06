package com.naver.goods;


import com.naver.goods.dto.GoodsComPriceInfo;
import com.naver.goods.service.StoreInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class ScglApplicationTests {

    @Autowired
    private StoreInfoService storeInfoService;

//    @Test
//    void contextLoads() {
//        List<GoodsComPriceInfo> goodsComPriceInfoList= storeInfoService.getGoodsComPriceInfo();
//        System.out.println(goodsComPriceInfoList);
//    }


}
