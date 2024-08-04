package com.naver.goods.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

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

    private static String shopName = "패션 핫이슈";
    /**
     * 定时比价
     */
    @Scheduled(cron = "* 0/20 * * * *")
    private void scheduledPriceParity() throws Exception {
        Map<String,String> map = new HashMap<>();
        map.put("10574690710","47446055679");
        map.put("10675560209","49207426792");
        map.put("10683874474","41757465114");
        map.put("10683981714","47910304236");
        for (String key:map.keySet()) {
            goodsInfoService.oprGoodsInfo(key, map.get(key), shopName);
        }
    }

}
