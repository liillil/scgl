package com.naver.goods.controller;

import com.naver.goods.config.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName: RedisTestController
 * @Description:
 * @Author: lg
 * @Date: 2024/8/3 22:30
 */
@RestController
public class RedisTestController {
    @Autowired
    private RedisUtil redisUtil;
    @GetMapping("/redisTest")
    public boolean redisTest(@RequestParam(value = "key") String key,@RequestParam(value = "value") String value){
        return  redisUtil.set(key,value,300);
    }

}
