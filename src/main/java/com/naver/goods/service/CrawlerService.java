package com.naver.goods.service;

import com.naver.goods.common.CommonConstants;
import com.naver.goods.entity.CrawlerGoodsInfo;
import com.naver.goods.utils.HttpUtils;
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
    /**
     * 爬取页面商品价格最低店铺名称和金额
     * @param comGoodsId
     * @return
     */
    public CrawlerGoodsInfo crawlerGoodsInfo(String comGoodsId){
        CrawlerGoodsInfo crawlerGoodsInfo = new CrawlerGoodsInfo();
        try {
            String crawUrl = CommonConstants.CRAW_URL + comGoodsId;
            Document document = HttpUtils.rawDataHomePage(crawUrl);
            Element priceElement = document.getElementsByClass(CommonConstants.CRAW_PRICE_LABEL).first();
            Element comStoreNameElement = document.getElementsByClass(CommonConstants.CRAW_GOODS_NAME_LABEL).get(1);
            if (comStoreNameElement != null) {
                String comStoreName = comStoreNameElement.text();
                crawlerGoodsInfo.setComStoreName(comStoreName);
                log.info("比价店铺名称: {},比价id:{}",comStoreName,comGoodsId);
            } else {
                log.info("未找到店铺名称");
            }
            if (priceElement != null) {
                String price = priceElement.text();
                if(StringUtils.isNotEmpty(price)) {
                    crawlerGoodsInfo.setPrice(Integer.valueOf(price.replace(",","")));
                    log.info("商品价格: {}", price);
                }
            } else {
                log.info("未找到商品价格");
            }
        } catch (Exception e) {
            log.info(">>>>>爬取页面商品价格最低店铺名称和金额异常，error msg:{}",e);
            return null;
        }
        return crawlerGoodsInfo;
    }
}
