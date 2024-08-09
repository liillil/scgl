package com.naver.goods.controller;

import com.naver.goods.dto.GoodsComPriceInfo;
import com.naver.goods.service.GoodsInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @program: scgl
 * @ClassName: GoodsInfoContorller
 * @description:
 * @author: ligang
 * @create: 2024-08-05 20:53:16
 */
@RestController
@RequestMapping("/goods")
@Slf4j
public class GoodsInfoContorller {

    @Autowired
    private GoodsInfoService goodsInfoService;

    @GetMapping("/scheduledPriceParity")
    private void scheduledPriceParity() throws Exception {
        List<GoodsComPriceInfo> goodsComPriceInfoList = goodsInfoService.getGoodsComPriceInfo();
        if (CollectionUtils.isEmpty(goodsComPriceInfoList) || goodsComPriceInfoList.size() == 0){
            return;
        }
        log.info(">>>> goodsComPriceInfoList:{}", goodsComPriceInfoList);
        for (GoodsComPriceInfo comPriceInfo : goodsComPriceInfoList){
            goodsInfoService.oprGoodsInfo(comPriceInfo);
            try {
                Thread.sleep(2000); // 休眠2秒
            } catch (InterruptedException e) {
                log.error(">>>> sleep error:{}", e);
                // 处理中断异常
                Thread.currentThread().interrupt(); // 清除中断状态
            }
        }
    }
}
