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
 * @ClassName: StoreInfo
 * @Description:
 * @Author: lg
 * @Date: 2024/8/4 16:42
 */
@TableName("store_info")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StoreInfo {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Column(name = "store_no")
    private String storeNo;

    @Column(name = "store_name")
    private String storeName;

    @Column(name = "client_id")
    private String clientId;

    @Column(name = "client_secret")
    private String clientSecret;

    @Column(name = "account_id")
    private String accountId;

    @Column(name = "created_time")
    private Date createdTime;

    @Column(name = "updated_time")
    private Date updatedTime;

}
