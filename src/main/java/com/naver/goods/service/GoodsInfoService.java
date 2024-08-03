package com.naver.goods.service;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.naver.goods.common.CommonConstants;
import com.naver.goods.entity.CrawlerGoodsInfo;
import com.naver.goods.utils.CerfTokenUtils;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class GoodsInfoService {

    private CrawlerService crawlerService;

    private String getGoodsInfoByGoodsNo(String goodsNo) {
        OkHttpClient client = new OkHttpClient();
        String crefToken = crefToken();
        if (StringUtils.isBlank(crefToken)) {
            return null;
        }

        String productNoUrl = CommonConstants.PRODUCT_URL + goodsNo;
        Request request = new Request.Builder()
                .url(productNoUrl)
                .get()
                .addHeader("Authorization", crefToken)
                .build();
        String goodsInfo = null;
        try {
            Response response = client.newCall(request).execute();
            if (response.code() == 200) {
                goodsInfo = response.body().toString();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return goodsInfo;
    }

    private void oprGoodsInfo(String goodsNo, String comGoodsId, String storeName) {
        String goodsInfo = getGoodsInfoByGoodsNo(goodsNo);

        JSONObject gooodInfoJson = JSONObject.parseObject(goodsInfo);
        JSONObject productJson = gooodInfoJson.getJSONObject("originProduct");
        Integer goodsPrice = productJson.getInteger("salePrice");
        String goodsName = productJson.getString("name");

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

    private void updateGoodsInfo(String goodsNo, String updateParams) {
        OkHttpClient client = new OkHttpClient();
        String crefToken = crefToken();
        if (StringUtils.isBlank(crefToken)) {
            return;
        }
        MediaType mediaType = MediaType.parse("application/json");

        String productUrl = CommonConstants.PRODUCT_URL + goodsNo;
        RequestBody body = RequestBody.create(mediaType, updateParams);
        Request request = new Request.Builder()
                .url(CommonConstants.PRODUCT_URL)
                .put(body)
                .addHeader("Authorization", crefToken)
                .addHeader("content-type", "application/json")
                .build();

        try {
            Response response = client.newCall(request).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String crefToken() {
        Long timestamp = System.currentTimeMillis();
        String generateSignature = CerfTokenUtils.generateSignature(CommonConstants.CLIENT_ID, CommonConstants.CLIENT_SECRET, timestamp);
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        String parameter = "client_id=" + CommonConstants.CLIENT_ID + "&timestamp=" + timestamp + "&grant_type=client_credentials&client_secret_sign=" + generateSignature + "&type=SELF" + "&account_id=" + CommonConstants.ACCOUNT_ID;
        RequestBody body = RequestBody.create(mediaType, parameter);
        Request request = new Request.Builder()
                .url(CommonConstants.CREF_TOKEN_URL)
                .post(body)
                .build();
        JSONObject jsonObject = null;
        try {
            Response response = client.newCall(request).execute();
            if (response.code() == 200) {
                jsonObject = JSONObject.parseObject(response.body().toString());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (jsonObject != null) {
            return jsonObject.getString("access_token");
        }
        return null;
    }
}
