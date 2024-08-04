package com.naver.goods.service;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.naver.goods.common.CommonConstants;
import com.naver.goods.config.RedisUtil;
import com.naver.goods.entity.CrawlerGoodsInfo;
import com.naver.goods.utils.CerfTokenUtils;
import com.naver.goods.utils.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
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

    private String getGoodsInfoByGoodsNo(String goodsNo) throws Exception {
        OkHttpClient client = HttpUtils.getUnsafeOkHttpClient();
        String crefToken = crefToken();
        if (StringUtils.isBlank(crefToken)) {
            return null;
        }
        String  goodsInfo = null;
        try {
        String productNoUrl = CommonConstants.PRODUCT_URL + goodsNo;
//        Request request = new Request.Builder()
//                .url(productNoUrl)
//                .get()
//                .addHeader("Authorization", crefToken)
//                .build();
            Map<String,String> headers = new HashMap<>();
            headers.put("Authorization","Bearer "+crefToken);
            goodsInfo = HttpUtils.getForm(productNoUrl,headers,30000,30000);
            log.info(">>>>getGoodsInfoByGoodsNo response:{}",goodsInfo);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return goodsInfo;
    }

    public void oprGoodsInfo(String goodsNo, String comGoodsId, String storeName) throws Exception {
        String goodsInfo = getGoodsInfoByGoodsNo(goodsNo);

        JSONObject gooodInfoJson = JSONObject.parseObject(goodsInfo);
        if(gooodInfoJson !=null && gooodInfoJson.containsKey("originProduct")) {
            JSONObject productJson = gooodInfoJson.getJSONObject("originProduct");
            Integer goodsPrice = productJson.getInteger("salePrice");

            JSONObject customerBenefitJson = productJson.getJSONObject("customerBenefit");
            JSONObject discountPolicyJson = customerBenefitJson.getJSONObject("immediateDiscountPolicy");
            JSONObject discountMethodJson = discountPolicyJson.getJSONObject("discountMethod");
            Integer discountPrice = discountMethodJson.getInteger("value");
            Integer goodsDiscountPrice = goodsPrice - discountPrice;

            CrawlerGoodsInfo crawlerGoodsInfo = crawlerService.crawlerGoodsInfo(comGoodsId);
            if (StringUtils.equals(crawlerGoodsInfo.getComStoreName(), storeName)) {
                return;
            }
            Integer updateDiscountPrice;
            Integer diffPrice = goodsDiscountPrice - crawlerGoodsInfo.getPrice();
            if (diffPrice == 0) {
                updateDiscountPrice = goodsPrice - 10;
            } else {
                updateDiscountPrice = goodsPrice - discountPrice - 10;
            }

            // 使用Jackson的ObjectMapper解析JSON字符串
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = null;

            try {
                jsonNode = mapper.readTree(goodsInfo);
                JsonNode outerNode = jsonNode.path("originProduct");
                if (outerNode.isObject()) {
                    ObjectNode objectNode = (ObjectNode) outerNode;
                    objectNode.put("salePrice", updateDiscountPrice); // 修改嵌套对象的值

                    goodsInfo = mapper.writeValueAsString(jsonNode);
                }
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            this.updateGoodsInfo(goodsNo, goodsInfo);
        }
    }

    private void updateGoodsInfo(String goodsNo, String updateParams) throws Exception {
//        OkHttpClient client = HttpUtils.getUnsafeOkHttpClient();
        String crefToken = crefToken();
        log.info("crefToken:{}", crefToken);
        if (StringUtils.isBlank(crefToken)) {
            return;
        }
//        MediaType mediaType = MediaType.parse("application/json");

        String productUrl = CommonConstants.PRODUCT_URL + goodsNo;
//        RequestBody body = RequestBody.create(mediaType, updateParams);
//        Request request = new Request.Builder()
//                .url(productUrl)
//                .put(body)
//                .addHeader("Authorization", "Bearer "+crefToken)
//                .addHeader("content-type", "application/json")
//                .build();
        try {
            Map<String,String> headers = new HashMap<>();
            headers.put("Authorization","Bearer "+crefToken);
//            Response response = client.newCall(request).execute();
            String resp = HttpUtils.httpPutWithJson(productUrl,updateParams, headers,30000, 30000);
            log.info(">>>>更新价格返回：{}，goodsNo:{}",resp,goodsNo);

        } catch (Exception e) {
           log.error(">>>>updateGoodsInfo error msg:{}",e);
        }
    }

    private String crefToken() throws Exception {
        Object o = redisUtil.get(CommonConstants.CLIENT_ID+"_token");
        try {
            if(o ==null) {
                Long timestamp = System.currentTimeMillis();
                String generateSignature = CerfTokenUtils.generateSignature(CommonConstants.CLIENT_ID, CommonConstants.CLIENT_SECRET, timestamp);
    //            OkHttpClient client = HttpUtils.getUnsafeOkHttpClient();
    //            MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
    //            String parameter = "client_id=" + CommonConstants.CLIENT_ID + "&timestamp=" + timestamp + "&grant_type=client_credentials&client_secret_sign=" + generateSignature + "&type=SELF" + "&account_id=" + CommonConstants.ACCOUNT_ID;
    //            RequestBody body = RequestBody.create(mediaType, parameter);
    //            Request request = new Request.Builder()
    //                    .url(CommonConstants.CREF_TOKEN_URL)
    //                    .post(body)
    //                    .build();
                    JSONObject jsonObject = null;

    //                Response response = client.newCall(request).execute();

                    Map<String,Object> map = new HashMap<>();
                    map.put("client_id",CommonConstants.CLIENT_ID);
                    map.put("timestamp",timestamp);
                    map.put("grant_type","client_credentials");
                    map.put("client_secret_sign",generateSignature);
                    map.put("type","SELF");
                    map.put("account_id",CommonConstants.ACCOUNT_ID);
                    Map<String,String> headers = new HashMap<>();
                    headers.put("contentType","application/x-www-form-urlencoded;charset=utf-8");
                    String response =  HttpUtils.postForm(CommonConstants.CREF_TOKEN_URL,map,headers,30000,30000);
                    jsonObject = JSONObject.parseObject(response);
                    log.info(">>>>crefToken response:{}",response);
                if (jsonObject != null && jsonObject.containsKey("access_token")) {
                    redisUtil.set(CommonConstants.CLIENT_ID+"_token",jsonObject.getString("access_token"),1800);
                    return jsonObject.getString("access_token");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return (String) o;
    }
}
