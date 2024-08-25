package com.naver.goods.service;

import com.naver.goods.dto.GoodsComPriceInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @ClassName: SchedulingTaskService
 * @Description:
 * @Author: lg
 * @Date: 2024/8/3 21:41
 */
@Slf4j
@Component
public class SchedulingTaskService {

    @Autowired
    private GoodsInfoService goodsInfoService;

    private static String storeName = "패션 핫이슈";
    /**
     * 定时比价
     */
    @Scheduled(cron = "* 0/15 * * * *")
//    @Scheduled(cron = "*/20 * * * * ?")
    private void scheduledPriceParity() throws Exception {
        long startTime = System.currentTimeMillis();
        List<GoodsComPriceInfo> goodsComPriceInfoList = goodsInfoService.getGoodsComPriceInfo();
        if (CollectionUtils.isEmpty(goodsComPriceInfoList) || goodsComPriceInfoList.size() == 0){
            return;
        }
        log.info(">>>> goodsComPriceInfoList:{}", goodsComPriceInfoList);
        for (GoodsComPriceInfo comPriceInfo : goodsComPriceInfoList){
            goodsInfoService.oprGoodsInfo(comPriceInfo);
        }
        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;
        log.info("scheduledTask execution time:{}", executionTime / 60000);
    }

}
