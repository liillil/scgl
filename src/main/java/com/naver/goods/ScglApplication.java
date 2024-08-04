package com.naver.goods;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@MapperScan("com.naver.goods.mapper")
public class ScglApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScglApplication.class, args);
    }

}
