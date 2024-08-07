package com.naver.goods.service;

import com.naver.goods.common.CommonConstants;
import com.naver.goods.dto.CrawlerGoodsInfo;
import com.naver.goods.dto.GoodsComPriceInfo;
import com.naver.goods.entity.GoodsComException;
import com.naver.goods.mapper.GoodsComExceptionMapper;
import com.naver.goods.utils.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Slf4j
@Service
public class CrawlerService {

    @Autowired
    private GoodsComExceptionMapper goodsComExceptionMapper;
    /**
     * 爬取页面商品价格最低店铺名称和金额
     * @param comPriceInfo
     * @return
     */
    public CrawlerGoodsInfo crawlerGoodsInfo(GoodsComPriceInfo comPriceInfo){
        CrawlerGoodsInfo crawlerGoodsInfo = new CrawlerGoodsInfo();
        try {
            String crawUrl = CommonConstants.CRAW_URL + comPriceInfo.getComStoreId();
            Document document = HttpUtils.rawDataHomePage(crawUrl);
            Element priceElement = document.getElementsByClass(CommonConstants.CRAW_PRICE_LABEL).first();
            Elements comStoreNameElements = document.getElementsByClass(CommonConstants.CRAW_GOODS_NAME_LABEL);

            if (comStoreNameElements != null) {
                log.info(">>>> 商品id:{}, 比价id:{}, 比价店铺名称获取元素size", comPriceInfo.getGoodsNo(), comPriceInfo.getComStoreId(), comStoreNameElements.size());
                Element comStoreNameElement;
                if (comStoreNameElements.size() > 1){
                    comStoreNameElement = comStoreNameElements.get(1);
                }else {
                    comStoreNameElement = comStoreNameElements.first();
                }
                String comStoreName = comStoreNameElement.text();
                crawlerGoodsInfo.setComStoreName(comStoreName);
                log.info(">>>> 商品id:{}, 比价id:{}, 比价店铺名称: {}", comPriceInfo.getGoodsNo(), comPriceInfo.getComStoreId(), comStoreName);
            } else {
                log.info(">>>> 未找到比价店铺名称: 商品id:{}, 比价id:{}", comPriceInfo.getGoodsNo(), comPriceInfo.getComStoreId());
            }
            if (priceElement != null) {
                String price = priceElement.text();
                if(StringUtils.isNotEmpty(price)) {
                    crawlerGoodsInfo.setPrice(Integer.valueOf(price.replace(",","")));
                    log.info(">>>> 商品id:{}, 比价id:{}, 比价店铺商品价格: {}", comPriceInfo.getGoodsNo(), comPriceInfo.getComStoreId(), price);
                }
            } else {
                log.info(">>>> 未找到比价店铺商品价格: 商品id:{}, 比价id:{}", comPriceInfo.getGoodsNo(), comPriceInfo.getComStoreId());
            }
        } catch (Exception e) {
            log.info(">>>>>爬取页面商品价格最低店铺名称和金额异常，error msg:{}, 商品id:{}, 比价id:{}",e, comPriceInfo.getGoodsNo(), comPriceInfo.getComStoreId());
            GoodsComException goodsComException = new GoodsComException();
            goodsComException.setGoodsNo(comPriceInfo.getGoodsNo());
            goodsComException.setComStoreId(comPriceInfo.getComStoreId());
            goodsComException.setExceptionMsg("比价链接异常");
            this.saveGoodsComException(goodsComException);
            return null;
        }
        return crawlerGoodsInfo;
    }


    @Transactional
    public void saveGoodsComException(GoodsComException goodsComException){
        try {
            goodsComExceptionMapper.insert(goodsComException);
        }catch (Exception e){
            log.error("goodsComException insert error:{}, goodsComException:{}", e, goodsComException);
        }
    }
}
