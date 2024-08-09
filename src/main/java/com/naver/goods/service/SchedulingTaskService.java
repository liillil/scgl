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
    @Scheduled(cron = "* 0/20 * * * *")
//    @Scheduled(cron = "*/20 * * * * ?")
    private void scheduledPriceParity() throws Exception {
        List<GoodsComPriceInfo> goodsComPriceInfoList = goodsInfoService.getGoodsComPriceInfo();
        if (CollectionUtils.isEmpty(goodsComPriceInfoList) || goodsComPriceInfoList.size() == 0){
            return;
        }
        log.info(">>>> goodsComPriceInfoList:{}", goodsComPriceInfoList);
        for (GoodsComPriceInfo comPriceInfo : goodsComPriceInfoList){
            goodsInfoService.oprGoodsInfo(comPriceInfo);
            try {
                Thread.sleep(5000); // 休眠5秒
            } catch (InterruptedException e) {
                log.error(">>>> sleep error:{}", e);
                // 处理中断异常
                Thread.currentThread().interrupt(); // 清除中断状态
            }
        }
    }

}
