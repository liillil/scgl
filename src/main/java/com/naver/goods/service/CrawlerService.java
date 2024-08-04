package com.naver.goods.service;

import com.naver.goods.common.CommonConstants;
import com.naver.goods.entity.CrawlerGoodsInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.internal.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;
@Slf4j
@Service
public class CrawlerService {

    public CrawlerGoodsInfo crawlerGoodsInfo(String comGoodsId){
        CrawlerGoodsInfo crawlerGoodsInfo = new CrawlerGoodsInfo();
        try {
            String crawUrl = CommonConstants.CRAW_URL + comGoodsId;
            Document document = Jsoup.connect(crawUrl).get();
            Element priceElement = document.getElementsByClass(CommonConstants.CRAW_PRICE_LABEL).first();
            Element comStoreNameElement = document.getElementsByClass(CommonConstants.CRAW_GOODS_NAME_LABEL).first();
            if (comStoreNameElement != null) {
                String comStoreName = comStoreNameElement.text();
                crawlerGoodsInfo.setComStoreName(comStoreName);
                System.out.println("比价店铺名称: " + comStoreName);
            } else {
                System.out.println("未找到店铺名称");
            }
            if (priceElement != null) {
                String price = priceElement.text();
                if(StringUtils.isNotEmpty(price)) {
                    crawlerGoodsInfo.setPrice(Integer.valueOf(price));
                    System.out.println("商品价格: " + price);
                }
            } else {
                System.out.println("未找到商品价格");
            }
        } catch (Exception e) {
//            e.printStackTrace();
            log.info(">>>>>error msg:{}",e);
            return null;
        }
        return crawlerGoodsInfo;
    }

    public static void main(String[] args) {
        CrawlerService crawlerService = new CrawlerService();
        crawlerService.crawlerGoodsInfo("47446055679");
    }
}
