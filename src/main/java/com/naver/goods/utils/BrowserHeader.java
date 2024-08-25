package com.naver.goods.utils;

import java.util.HashMap;
import java.util.Map;

public class BrowserHeader {

    public static Map<String,String> firefoxHeader(String url){
        Map<String,String> headers = new HashMap<>();
        headers.put("accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8");
        headers.put("accept-encoding","gzip, deflate, br");
        headers.put("accept-language","zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2");

        headers.put("connection","keep-alive");
        headers.put("cookie"," NNB=6QERHX6N33DGM; BUC=yHlk2LAjFrox1XMtlQqqJf77UhwCyxCly3cMXrcboo4=; sus_val=" + HttpUtils.getCookie(url));
        headers.put("host","search.shopping.naver.com");
        headers.put("sec-fetch-dest", "document");
        headers.put("sec-fetch-mode","navigate");
        headers.put("sec-fetch-site", "none");
        headers.put("sec-fetch-user","?1");
        headers.put("upgrade-insecure-requests","1");
        headers.put("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/109.0");
        return headers;
    }

    public static Map<String,String> edgeHeader(String url){
        Map<String,String> headers = new HashMap<>();
        headers.put("accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7");
        headers.put("accept-encoding","gzip, deflate, br, zstd");
        headers.put("accept-language","zh-CN,zh;q=0.9,en;q=0.8,en-GB;q=0.7,en-US;q=0.6");

        headers.put("cache-control","max-age=0");
        headers.put("cookie","NNB=TMGINJ2YYGWWM; SHP_BUCKET_ID=8; BUC=ez7x9slRX4aEna8Sk4AWJx_8ybqE49EBxJtW-lq8Qp4=;sus_val=" + HttpUtils.getCookie(url));
        headers.put("priority","u=0, i");
        headers.put("sec-ch-ua","\"Not)A;Brand\";v=\"99\", \"Microsoft Edge\";v=\"127\", \"Chromium\";v=\"127\"");


        headers.put("sec-ch-ua-arch","x86");

        headers.put("sec-ch-ua-bitness","64");

        headers.put("sec-ch-ua-form-factors","Desktop");
        headers.put("sec-ch-ua-full-version-list","\"Not)A;Brand\";v=\"99.0.0.0\", \"Microsoft Edge\";v=\"127.0.2651.105\", \"Chromium\";v=\"127.0.6533.120\"");
        headers.put("sec-ch-ua-mobile","?0");
        headers.put("sec-ch-ua-model","");
        headers.put("sec-ch-ua-platform","Windows");
        headers.put("sec-ch-ua-platform-version","15.0.0");
        headers.put("sec-ch-ua-wow64","?0");
        headers.put("sec-fetch-dest","document");
        headers.put("sec-fetch-mode","navigate");
        headers.put("sec-fetch-site","same-origin");
        headers.put("sec-fetch-user","?1");
        headers.put("upgrade-insecure-requests","1");
        headers.put("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/127.0.0.0 Safari/537.36");
        return headers;
    }

    public static Map<String,String> chrome128Header(String url){
            Map<String,String> headers = new HashMap<>();
            headers.put("accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7");
            headers.put("accept-encoding","gzip, deflate, br, zstd");
            headers.put("accept-language","zh-CN,zh;q=0.9");

            headers.put("cache-control","max-age=0");
            headers.put("cookie","NNB=LUTXTTURIS4GM; SHP_BUCKET_ID=9; NAC=22R6BQQfKkxKA; _fwb=50IHkDJqDPOUvNyrggSQgh.1723353314841; ASID=ac687df50000019140ba76af00000070; ncpa=3639268|m024m19k|4ebf029313a9007b4ef7d56a60a61bf669844b77|s_a26d2545fb4|20f9e5fa9062da95a99c5233bce305fbc546e2e2:6943384|m024m8zc|57f4c9a3ccb9e8a30ec8e517a934b0f2c33deaff|s_4374388d2f41|db9edae54360c95ab9589fb11c6d777f96268831:6335714|m024mbao|abc57029185434d0e02f0694222644d7602ce6ef|s_480030d9c77b|8e37e8d753ed2e1566518525a8b80b37206855ab:11124714|m024meds|ebdebc17ffd394db0349a2d39d3da5fd95022dc5|s_2e8a5f07745c|cbfe12a83a4480f3c65c9e9294aebe7067835f08:3441128|m024mi8o|bfecd03f0d7ca08d51630e95a3a20d351268c592|s_1b6e8dc120ed|f22e6f1ae58746ae10cbab125a75b17c66d726a9:6026124|m024mw4o|2acad6b9ed5ae4589b9d0faebccf4968bca1fe90|s_22b6092e1340|f009874d27c1e19450a74b4c7722b31e909d35d0:24|m024mzzk|e9ccc7139d42833a0889e4b5ef1f02282c5b750b|s_419afe53a6bb|6c9c4a625bd43d6eb844e00933d4b40fa86b5d8f:870069|m024n3ug|46f8a236ccf35ff3304ce26f71e88888ba669c33|s_bfc50422034|d45043d081db283bc449843cfee0dc3c9b31264d:6093745|m024pjt4|051b9a90ab63e9b298e07a833ad17c6ecfaf31a1|s_1e6fa6eb9b71d|7e21cd67a7ebe59e6ba6fe7fc908fffe3984b39c:95694|m024royw|477050ff131edf257551b5c84a3070bd776a5177|95694|20acd4c48c9f9a43c1953bdab6a362ddf7423bd5; sus_val=" + HttpUtils.getCookie(url));
            headers.put("priority","u=0, i");
            headers.put("referer", "https://shimo.im/");
            headers.put("sec-ch-ua","\"Chromium\";v=\"128\", \"Not;A=Brand\";v=\"24\", \"Google Chrome\";v=\"128\"");


            headers.put("sec-ch-ua-arch","x86");

            headers.put("sec-ch-ua-bitness","64");

            headers.put("sec-ch-ua-form-factors","Desktop");
            headers.put("sec-ch-ua-full-version-list","\"Chromium\";v=\"128.0.6613.85\", \"Not;A=Brand\";v=\"24.0.0.0\", \"Google Chrome\";v=\"128.0.6613.85\"");
            headers.put("sec-ch-ua-mobile","?0");
            headers.put("sec-ch-ua-model","");
            headers.put("sec-ch-ua-platform","Windows");
            headers.put("sec-ch-ua-platform-version","15.0.0");
            headers.put("sec-ch-ua-wow64","?0");
            headers.put("sec-fetch-dest","document");
            headers.put("sec-fetch-mode","navigate");
            headers.put("sec-fetch-site","same-origin");
            headers.put("sec-fetch-user","?1");
            headers.put("upgrade-insecure-requests","1");
            headers.put("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/128.0.0.0 Safari/537.36");
            return headers;
    }

    public static Map<String,String> chrome127Header(String url){
        Map<String,String> headers = new HashMap<>();
        headers.put("accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7");
        headers.put("accept-encoding","gzip, deflate, br, zstd");
        headers.put("accept-language","zh-CN,zh;q=0.9");

        headers.put("cache-control","max-age=0");
        headers.put("cookie","NNB=V2UT6KAJVKUGM; BUC=XNGGl5os6Hw8HSeNhhJ3GxuP1KoWuwnndPfu0vWqd-M=; sus_val=" + HttpUtils.getCookie(url));
        headers.put("priority","u=0, i");
        headers.put("sec-ch-ua","\"Not)A;Brand\";v=\"99\", \"Google Chrome\";v=\"127\", \"Chromium\";v=\"127\"");


        headers.put("sec-ch-ua-arch","x86");

        headers.put("sec-ch-ua-bitness","64");

        headers.put("sec-ch-ua-form-factors","Desktop");
        headers.put("sec-ch-ua-full-version-list","\"Not)A;Brand\";v=\"99.0.0.0\", \"Google Chrome\";v=\"127.0.6533.120\", \"Chromium\";v=\"127.0.6533.120\"");
        headers.put("sec-ch-ua-mobile","?0");
        headers.put("sec-ch-ua-platform","Windows");
        headers.put("sec-ch-ua-platform-version","10.0.0");
        headers.put("sec-ch-ua-wow64","?0");
        headers.put("sec-fetch-dest","document");
        headers.put("sec-fetch-mode","navigate");
        headers.put("sec-fetch-site","none");
        headers.put("sec-fetch-user","?1");
        headers.put("upgrade-insecure-requests","1");
        headers.put("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/127.0.0.0 Safari/537.36");
        return headers;
    }

    public static void main(String[] args) {
        Integer i = 28800;
        Integer j = 10;
        Integer m = i % j;
        System.out.println("余数：" + m);
    }
}
