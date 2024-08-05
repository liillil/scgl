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

    private static String storeName = "패션 핫이슈";
    /**
     * 定时比价
     */
    @Scheduled(cron = "0/10 * * * * *")
//    @Scheduled(cron = "*/5 * * * * ?")
    private void scheduledPriceParity() throws Exception {
        Map<String,String> map = new HashMap<>();
        map.put("10574690710","47446055679");
        map.put("10675560209","49207426792");
        map.put("10683874474","41757465114");
        map.put("10683981714","47910304236");
        map.put("10683923486","48745111622");
        map.put("10684282951","36992856412");
        map.put("10690868289","48279706519");
        map.put("10684183300","49231576755");
        map.put("10691122185","42922406763");
        map.put("10680545463","48121274533");
        for (String key:map.keySet()) {
            goodsInfoService.oprGoodsInfo(key, map.get(key), storeName);
        }
    }

}
