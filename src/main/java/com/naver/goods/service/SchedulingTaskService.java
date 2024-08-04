package com.naver.goods.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

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


    /**
     * 定时比价
     */
    @Scheduled(cron = "0/5 * * * * *")
    private void scheduledPriceParity() throws Exception {
        goodsInfoService.oprGoodsInfo("10574690710","47446055679","패션 핫이슈");
        log.info(">>>>1");
    }

}
