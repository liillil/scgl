package com.naver.goods.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.naver.goods.dto.InfoResult;
import com.naver.goods.dto.InfoResults;
import com.naver.goods.entity.ShopInfoTb;
import com.naver.goods.mapper.ShopInfoTbMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program: scgl
 * @ClassName: ShopInfoService
 * @description:
 * @author: ligang
 * @create: 2024-08-05 20:45:29
 */
@Service
public class ShopInfoService {

    @Autowired
    private ShopInfoTbMapper shopInfoTbMapper;

    public InfoResult findAllShop(){
        QueryWrapper<ShopInfoTb> wrapper = new QueryWrapper<>();
        List<ShopInfoTb> list = shopInfoTbMapper.selectList(wrapper);
        return InfoResults.ok(list);
    }

}
