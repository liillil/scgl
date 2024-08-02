package com.example.scgl.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;


/**
 * @ClassName: NetworkUtils
 * @Description:
 * @Author: lg
 * @Date: 2024/8/2 21:06
 */
public class NetworkUtils {
    private static String url = "https://search.shopping.naver.com/catalog/47446055679";// 替换为具体商品页面URL


    public static void main(String[] args) {

        String priceSelector = "lowestPrice_num__A5gM9"; // 根据商品页面结构修改选择器

        try {
            Document document = Jsoup.connect(url).get();
            Element priceElement = document.getElementsByClass(priceSelector).first();
            if (priceElement != null) {
                String price = priceElement.text();
                System.out.println("商品价格: " + price);
            } else {
                System.out.println("未找到价格信息");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}