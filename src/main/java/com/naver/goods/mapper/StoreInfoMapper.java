package com.naver.goods.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.yulichang.base.MPJBaseMapper;
import com.naver.goods.dto.GoodsComPriceInfo;
import com.naver.goods.entity.StoreInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @ClassName: StoreInfoMapper
 * @Description:
 * @Author: lg
 * @Date: 2024/8/4 16:43
 */
@Mapper
public interface StoreInfoMapper extends MPJBaseMapper<StoreInfo> {

//    List<GoodsComPriceInfo> getGoodsComPriceInfo();
}
