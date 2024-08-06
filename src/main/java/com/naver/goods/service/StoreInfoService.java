package com.naver.goods.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.naver.goods.dto.InfoResult;
import com.naver.goods.dto.InfoResults;
import com.naver.goods.dto.GoodsComPriceInfo;
import com.naver.goods.entity.GoodsInfo;
import com.naver.goods.entity.StoreInfo;
import com.naver.goods.mapper.GoodsInfoMapper;
import com.naver.goods.mapper.StoreInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @program: scgl
 * @ClassName: ShopInfoService
 * @description:
 * @author: ligang
 * @create: 2024-08-05 20:45:29
 */
@Service
public class StoreInfoService {

    @Autowired
    private StoreInfoMapper storeInfoMapper;

    @Autowired
    private GoodsInfoMapper goodsInfoMapper;

    public InfoResult findAllShop(){
        QueryWrapper<StoreInfo> wrapper = new QueryWrapper<>();
        List<StoreInfo> list = storeInfoMapper.selectList(wrapper);
        return InfoResults.ok(list);
    }

    public List<GoodsComPriceInfo> getGoodsComPriceInfo(){
        MPJLambdaWrapper<GoodsComPriceInfo> mapMPJLambdaWrapper = new MPJLambdaWrapper<>();
        mapMPJLambdaWrapper.select(GoodsInfo::getGoodsNo, GoodsInfo::getComStoreId, GoodsInfo::getGoodsLimitPrice)
                .select(StoreInfo::getStoreName,StoreInfo::getClientId, StoreInfo::getClientSecret, StoreInfo::getAccountId)
                .leftJoin(StoreInfo.class, StoreInfo::getStoreNo, GoodsInfo::getStoreNo);
        return goodsInfoMapper.selectJoinList(GoodsComPriceInfo.class, mapMPJLambdaWrapper);
    }
}
