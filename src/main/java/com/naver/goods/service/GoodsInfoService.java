package com.naver.goods.service;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.naver.goods.common.CommonConstants;
import com.naver.goods.config.RedisUtil;
import com.naver.goods.dto.CrawlerGoodsInfo;
import com.naver.goods.dto.GoodsComPriceInfo;
import com.naver.goods.utils.CerfTokenUtils;
import com.naver.goods.utils.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class GoodsInfoService {
    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private CrawlerService crawlerService;

    /**
     * 查询商品信息
     *
     * @param goodsNo 商品id
     * @return
     * @throws Exception
     */
    private String getGoodsInfoByGoodsNo(GoodsComPriceInfo comPriceInfo) throws Exception {
        String crefToken = crefToken(comPriceInfo);
        if (StringUtils.isBlank(crefToken)) {
            return null;
        }
        String goodsInfo = null;
        try {
            String productNoUrl = CommonConstants.PRODUCT_URL + comPriceInfo.getGoodsNo();
            Map<String, String> headers = new HashMap<>();
            headers.put("Authorization", "Bearer " + crefToken);
            goodsInfo = HttpUtils.getForm(productNoUrl, headers, 30000, 30000);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return goodsInfo;
    }
//    private String getGoodsInfoByGoodsNo(String goodsNo) throws Exception {
//        String crefToken = crefToken();
//        if (StringUtils.isBlank(crefToken)) {
//            return null;
//        }
//        String goodsInfo = null;
//        try {
//            String productNoUrl = CommonConstants.PRODUCT_URL + goodsNo;
//            Map<String, String> headers = new HashMap<>();
//            headers.put("Authorization", "Bearer " + crefToken);
//            goodsInfo = HttpUtils.getForm(productNoUrl, headers, 30000, 30000);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        return goodsInfo;
//    }

    /**
     * 比较并修改商品至最低价格
     *
     * @param goodsNo    商品id
     * @param comGoodsId 比价id
     * @param storeName  店铺名称
     * @throws Exception
     */
    public void oprGoodsInfo(GoodsComPriceInfo comPriceInfo) throws Exception {
        try {
            String goodsInfo = getGoodsInfoByGoodsNo(comPriceInfo);

            JSONObject gooodInfoJson = JSONObject.parseObject(goodsInfo);
            if (gooodInfoJson != null && gooodInfoJson.containsKey("originProduct")) {
                JSONObject productJson = gooodInfoJson.getJSONObject("originProduct");
                Integer goodsPrice = productJson.getInteger("salePrice");

                Integer goodsLimitPrice = comPriceInfo.getGoodsLimitPrice();
                if (goodsLimitPrice != null && goodsPrice < goodsLimitPrice){
                    return;
                }

                JSONObject customerBenefitJson = productJson.getJSONObject("customerBenefit");
                JSONObject discountPolicyJson = customerBenefitJson.getJSONObject("immediateDiscountPolicy");
                JSONObject discountMethodJson = discountPolicyJson.getJSONObject("discountMethod");
                Integer discountPrice = discountMethodJson.getInteger("value");
                Integer goodsDiscountPrice = goodsPrice - discountPrice;

                CrawlerGoodsInfo crawlerGoodsInfo = crawlerService.crawlerGoodsInfo(comPriceInfo.getComStoreId());
                if (crawlerGoodsInfo == null){
                    return;
                }
                if (StringUtils.equals(crawlerGoodsInfo.getComStoreName(), comPriceInfo.getStoreName())) {
                    return;
                }

                Integer updateDiscountPrice;
                Integer diffPrice = goodsDiscountPrice - crawlerGoodsInfo.getPrice();
                if (diffPrice == 0) {
                    updateDiscountPrice = goodsPrice - 10;
                } else {
                    updateDiscountPrice = goodsPrice - diffPrice - 10;
                }
                log.info("比价折扣后价格:{}", updateDiscountPrice);

                // 使用Jackson的ObjectMapper解析JSON字符串
                ObjectMapper mapper = new ObjectMapper();
                JsonNode jsonNode;
                jsonNode = mapper.readTree(goodsInfo);
                JsonNode outerNode = jsonNode.path("originProduct");
                if (outerNode.isObject()) {
                    ObjectNode objectNode = (ObjectNode) outerNode;
                    objectNode.put("salePrice", updateDiscountPrice); // 修改嵌套对象的值
                    goodsInfo = mapper.writeValueAsString(jsonNode);
                }
                this.updateGoodsInfo(comPriceInfo, goodsInfo);
            }
        } catch (Exception e) {
            log.error(">>>>比较并修改商品至最低价格异常，error msg:{}", e);
        }
    }
//    public void oprGoodsInfo(String goodsNo, String comGoodsId, String storeName) throws Exception {
//        try {
//            String goodsInfo = getGoodsInfoByGoodsNo(goodsNo);
//
//            JSONObject gooodInfoJson = JSONObject.parseObject(goodsInfo);
//            if (gooodInfoJson != null && gooodInfoJson.containsKey("originProduct")) {
//                JSONObject productJson = gooodInfoJson.getJSONObject("originProduct");
//                Integer goodsPrice = productJson.getInteger("salePrice");
//
//                JSONObject customerBenefitJson = productJson.getJSONObject("customerBenefit");
//                JSONObject discountPolicyJson = customerBenefitJson.getJSONObject("immediateDiscountPolicy");
//                JSONObject discountMethodJson = discountPolicyJson.getJSONObject("discountMethod");
//                Integer discountPrice = discountMethodJson.getInteger("value");
//                Integer goodsDiscountPrice = goodsPrice - discountPrice;
//
//                CrawlerGoodsInfo crawlerGoodsInfo = crawlerService.crawlerGoodsInfo(comGoodsId);
//                if (crawlerGoodsInfo == null){
//                    return;
//                }
//                if (StringUtils.equals(crawlerGoodsInfo.getComStoreName(), storeName)) {
//                    return;
//                }
//                Integer updateDiscountPrice;
//                Integer diffPrice = goodsDiscountPrice - crawlerGoodsInfo.getPrice();
//                if (diffPrice == 0) {
//                    updateDiscountPrice = goodsPrice - 10;
//                } else {
//                    updateDiscountPrice = goodsPrice - diffPrice - 10;
//                }
//                log.info("比价折扣后价格:{}", updateDiscountPrice);
//                // 使用Jackson的ObjectMapper解析JSON字符串
//                ObjectMapper mapper = new ObjectMapper();
//                JsonNode jsonNode;
//                jsonNode = mapper.readTree(goodsInfo);
//                JsonNode outerNode = jsonNode.path("originProduct");
//                if (outerNode.isObject()) {
//                    ObjectNode objectNode = (ObjectNode) outerNode;
//                    objectNode.put("salePrice", updateDiscountPrice); // 修改嵌套对象的值
//                    goodsInfo = mapper.writeValueAsString(jsonNode);
//                }
//                this.updateGoodsInfo(goodsNo, goodsInfo);
//            }
//        } catch (Exception e) {
//            log.error(">>>>比较并修改商品至最低价格异常，error msg:{}", e);
//        }
//    }

    /**
     * 更新商品价格
     *
     * @param goodsNo      商品id
     * @param updateParams 商品详情
     * @throws Exception
     */
    private void updateGoodsInfo(GoodsComPriceInfo comPriceInfo, String updateParams) throws Exception {
        String crefToken = crefToken(comPriceInfo);
        if (StringUtils.isBlank(crefToken)) {
            return;
        }
        String productUrl = CommonConstants.PRODUCT_URL + comPriceInfo.getGoodsNo();
        try {
            Map<String, String> headers = new HashMap<>();
            headers.put("Authorization", "Bearer " + crefToken);
            String resp = HttpUtils.httpPutWithJson(productUrl, updateParams, headers, 300000, 300000);
            log.info(">>>>更新价格返回：{}，商品id:{}", resp, comPriceInfo.getGoodsNo());
        } catch (Exception e) {
            log.error(">>>>更新商品价格异常，error msg:{}", e);
        }
    }
//    private void updateGoodsInfo(String goodsNo, String updateParams) throws Exception {
//        String crefToken = crefToken();
//        if (StringUtils.isBlank(crefToken)) {
//            return;
//        }
//        String productUrl = CommonConstants.PRODUCT_URL + goodsNo;
//        try {
//            Map<String, String> headers = new HashMap<>();
//            headers.put("Authorization", "Bearer " + crefToken);
//            String resp = HttpUtils.httpPutWithJson(productUrl, updateParams, headers, 300000, 300000);
//            log.info(">>>>更新价格返回：{}，商品id:{}", resp, goodsNo);
//        } catch (Exception e) {
//            log.error(">>>>更新商品价格异常，error msg:{}", e);
//        }
//    }

    /**
     * 获取패션 핫이슈店铺 token
     *
     * @return
     * @throws Exception
     */
//    private String crefToken(GoodsComPriceInfo comPriceInfo){
//        return crefToken(comPriceInfo);
//    }

    /**
     * 获取token
     *
     * @param storeClientId
     * @return
     * @throws Exception
     */
    private String crefToken(GoodsComPriceInfo comPriceInfo) {
        Object o = redisUtil.get(comPriceInfo.getClientId() + "_token");
        try {
            if (o == null) {
                Long timestamp = System.currentTimeMillis();
                String generateSignature = CerfTokenUtils.generateSignature(comPriceInfo.getClientId(), comPriceInfo.getClientSecret(), timestamp);
                JSONObject jsonObject;
                Map<String, Object> map = new HashMap<>();
                map.put("client_id", comPriceInfo.getClientId());
                map.put("timestamp", timestamp);
                map.put("grant_type", "client_credentials");
                map.put("client_secret_sign", generateSignature);
                map.put("type", "SELF");
                map.put("account_id", comPriceInfo.getAccountId());
                Map<String, String> headers = new HashMap<>();
                headers.put("contentType", "application/x-www-form-urlencoded;charset=utf-8");
                String response = HttpUtils.postForm(CommonConstants.CREF_TOKEN_URL, map, headers, 30000, 30000);
                jsonObject = JSONObject.parseObject(response);
                log.info(">>>>获取店铺：{} token返回信息:{}",comPriceInfo.getClientId(), response);
                if (jsonObject != null && jsonObject.containsKey("access_token")) {
                    redisUtil.set(comPriceInfo.getClientId() + "_token", jsonObject.getString("access_token"), 1500);
                    return jsonObject.getString("access_token");
                }
            }
        } catch (Exception e) {
            log.info(">>>>>>>>获取店铺：{} token异常，error msg:{}",e);
        }
        return (String) o;
    }
//    private String crefToken(String storeClientId) {
//        Object o = redisUtil.get(storeClientId + "_token");
//        try {
//            if (o == null) {
//                Long timestamp = System.currentTimeMillis();
//                String generateSignature = CerfTokenUtils.generateSignature(CommonConstants.CLIENT_ID, CommonConstants.CLIENT_SECRET, timestamp);
//                JSONObject jsonObject;
//                Map<String, Object> map = new HashMap<>();
//                map.put("client_id", CommonConstants.CLIENT_ID);
//                map.put("timestamp", timestamp);
//                map.put("grant_type", "client_credentials");
//                map.put("client_secret_sign", generateSignature);
//                map.put("type", "SELF");
//                map.put("account_id", CommonConstants.ACCOUNT_ID);
//                Map<String, String> headers = new HashMap<>();
//                headers.put("contentType", "application/x-www-form-urlencoded;charset=utf-8");
//                String response = HttpUtils.postForm(CommonConstants.CREF_TOKEN_URL, map, headers, 30000, 30000);
//                jsonObject = JSONObject.parseObject(response);
//                log.info(">>>>获取店铺：{} token返回信息:{}",storeClientId, response);
//                if (jsonObject != null && jsonObject.containsKey("access_token")) {
//                    redisUtil.set(CommonConstants.CLIENT_ID + "_token", jsonObject.getString("access_token"), 1500);
//                    return jsonObject.getString("access_token");
//                }
//            }
//        } catch (Exception e) {
//            log.info(">>>>>>>>获取店铺：{} token异常，error msg:{}",e);
//        }
//        return (String) o;
//    }

}
