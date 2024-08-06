package com.naver.goods.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.util.Date;

/**
 * @ClassName: GoodsInfo
 * @Description:
 * @Author: lg
 * @Date: 2024/8/4 16:42
 */
@TableName("goods_info")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoodsInfo {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Column(name = "store_no")
    private String storeNo;

    @Column(name = "goods_no")
    private String goodsNo;

    @Column(name = "com_store_id")
    private String comStoreId;

    @Column(name = "goods_limit_price")
    private Integer goodsLimitPrice;

    @Column(name = "created_time")
    private Date createdTime;

    @Column(name = "updated_time")
    private Date updatedTime;

}
