package com.naver.goods.service;

import com.naver.goods.common.CommonConstants;
import com.naver.goods.entity.CrawlerGoodsInfo;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

@Service
public class CrawlerService {

    public CrawlerGoodsInfo crawlerGoodsInfo(String comGoodsId){
        CrawlerGoodsInfo crawlerGoodsInfo = new CrawlerGoodsInfo();
        try {
            String crawUrl = CommonConstants.CRAW_URL + comGoodsId;
            Document document = Jsoup.connect(crawUrl).get();
            Element priceElement = document.getElementsByClass(CommonConstants.CRAW_PRICE_LABEL).first();
            Element comShopNameElement = document.getElementsByClass(CommonConstants.CRAW_GOODS_NAME_LABEL).first();
            if (comShopNameElement != null) {
                String comShopName = comShopNameElement.text();
                crawlerGoodsInfo.setComShopName(comShopName);
                System.out.println("比价商品名称: " + comShopName);
            } else {
                System.out.println("未找到商品名称");
            }
            if (priceElement != null) {
                String price = priceElement.text();
                crawlerGoodsInfo.setPrice(price);
                System.out.println("商品价格: " + price);
            } else {
                System.out.println("未找到商品价格");
            }
        } catch (Exception e) {
//            e.printStackTrace();
            return null;
        }
        return crawlerGoodsInfo;
    }
}
