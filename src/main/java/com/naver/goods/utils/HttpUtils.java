package com.naver.goods.utils;

import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.apache.commons.httpclient.ConnectTimeoutException;
import org.apache.commons.io.IOUtils;
import org.apache.http.Consts;
import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.MDC;
import org.apache.http.client.config.RequestConfig.Builder;


import javax.net.ssl.*;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.security.GeneralSecurityException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @ClassName: HttpUtils
 * @Description:
 * @Author: lg
 * @Date: 2024/8/3 23:07
 */
@Slf4j
public class HttpUtils {
    static CloseableHttpClient client = null;
    static {
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(128);
        cm.setDefaultMaxPerRoute(128);
        client = HttpClients.custom().setConnectionManager(cm).build();
    }
    public static OkHttpClient getUnsafeOkHttpClient() throws Exception {
        // 创建一个信任所有证书的TrustManager
        final TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    }

                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[]{};
                    }
                }
        };

        // 初始化SSLContext
        final SSLContext sslContext = SSLContext.getInstance("SSL");
        sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

        // 创建一个SSLSocketFactory
        final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

        // 创建一个HostnameVerifier，它不会验证主机名
        HostnameVerifier trustAllHosts = new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true; // 不验证主机名
            }
        };

        // 创建一个OkHttpClient并配置它
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
        builder.hostnameVerifier(trustAllHosts);

        return builder.build();
    }

    public static String getForm(String url, Map<String, String> headers, Integer connTimeout, Integer readTimeout)throws ConnectTimeoutException,
            SocketTimeoutException, Exception {
        CloseableHttpClient client = null;
        HttpGet get = new HttpGet(url);
        try {


            if (headers != null && !headers.isEmpty()) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    get.addHeader(entry.getKey(), entry.getValue());
                }
            }
            setMDC(get);
            // 设置参数
            Builder customReqConf = RequestConfig.custom();
            if (connTimeout != null) {
                customReqConf.setConnectTimeout(connTimeout);
            }
            if (readTimeout != null) {
                customReqConf.setSocketTimeout(readTimeout);
            }
            get.setConfig(customReqConf.build());
            HttpResponse res = null;
            if (url.startsWith("https")) {
                // 执行 Https 请求.
                client = createSSLInsecureClient();
                res = client.execute(get);
            } else {
                // 执行 Http 请求.
                client = HttpUtils.client;
                res = client.execute(get);
            }
            return IOUtils.toString(res.getEntity().getContent(), "UTF-8");
        } catch (Exception e) {
        } finally {
            get.releaseConnection();
            if (url.startsWith("https") && client != null
                    && client instanceof CloseableHttpClient) {
                ((CloseableHttpClient) client).close();
            }
        }
        return "";
    }

    public static String putForm(String url, Map<String, Object> params, Map<String, String> headers, Integer connTimeout, Integer readTimeout)throws ConnectTimeoutException,
            SocketTimeoutException, Exception {
        CloseableHttpClient client = null;
        HttpPut put = new HttpPut(url);
        try {
            if (params != null && !params.isEmpty()) {
                List<BasicNameValuePair> formParams = new ArrayList<>();
                Set<Map.Entry<String, Object>> entrySet = params.entrySet();
                for (Map.Entry<String, Object> entry : entrySet) {
                    formParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()+""));
                }
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formParams, Consts.UTF_8);
                put.setEntity(entity);
            }

            if (headers != null && !headers.isEmpty()) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    put.addHeader(entry.getKey(), entry.getValue());
                }
            }
            setMDC(put);
            // 设置参数
            Builder customReqConf = RequestConfig.custom();
            if (connTimeout != null) {
                customReqConf.setConnectTimeout(connTimeout);
            }
            if (readTimeout != null) {
                customReqConf.setSocketTimeout(readTimeout);
            }
            put.setConfig(customReqConf.build());
            HttpResponse res = null;
            if (url.startsWith("https")) {
                // 执行 Https 请求.
                client = createSSLInsecureClient();
                res = client.execute(put);
            } else {
                // 执行 Http 请求.
                client = HttpUtils.client;
                res = client.execute(put);
            }
            return IOUtils.toString(res.getEntity().getContent(), "UTF-8");
        } catch (Exception e) {
        } finally {
            put.releaseConnection();
            if (url.startsWith("https") && client != null
                    && client instanceof CloseableHttpClient) {
                ((CloseableHttpClient) client).close();
            }
        }
        return "";
    }

    public static String postForm(String url, Map<String, Object> params, Map<String, String> headers, Integer connTimeout, Integer readTimeout) throws ConnectTimeoutException,
            SocketTimeoutException, Exception {

        CloseableHttpClient client = null;
        HttpPost post = new HttpPost(url);
        try {
            if (params != null && !params.isEmpty()) {
                List<BasicNameValuePair> formParams = new ArrayList<>();
                Set<Map.Entry<String, Object>> entrySet = params.entrySet();
                for (Map.Entry<String, Object> entry : entrySet) {
                    formParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()+""));
                }
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formParams, Consts.UTF_8);
                post.setEntity(entity);
            }

            if (headers != null && !headers.isEmpty()) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    post.addHeader(entry.getKey(), entry.getValue());
                }
            }
            setMDC(post);
            // 设置参数
            Builder customReqConf = RequestConfig.custom();
            if (connTimeout != null) {
                customReqConf.setConnectTimeout(connTimeout);
            }
            if (readTimeout != null) {
                customReqConf.setSocketTimeout(readTimeout);
            }
            post.setConfig(customReqConf.build());
            HttpResponse res = null;
            if (url.startsWith("https")) {
                // 执行 Https 请求.
                client = createSSLInsecureClient();
                res = client.execute(post);
            } else {
                // 执行 Http 请求.
                client = HttpUtils.client;
                res = client.execute(post);
            }
            return IOUtils.toString(res.getEntity().getContent(), "UTF-8");
        } catch (Exception e) {
        } finally {
            post.releaseConnection();
            if (url.startsWith("https") && client != null
                    && client instanceof CloseableHttpClient) {
                ((CloseableHttpClient) client).close();
            }
        }
        return "";
    }

    private static void setMDC(HttpRequestBase requestBase){
        if (MDC.getCopyOfContextMap() != null && MDC.getCopyOfContextMap().get("X-B3-TraceId") != null) {
            requestBase.setHeader("X-B3-TraceId", (String) MDC.getCopyOfContextMap().get("X-B3-TraceId"));
        }
        if (MDC.getCopyOfContextMap() != null && MDC.getCopyOfContextMap().get("X-B3-SpanId") != null) {
            requestBase.setHeader("X-B3-SpanId", MDC.getCopyOfContextMap().get("X-B3-SpanId"));
        }
        if (MDC.getCopyOfContextMap() != null && MDC.getCopyOfContextMap().get("X-B3-Sampled") != null) {
            requestBase.setHeader("X-B3-Sampled", MDC.getCopyOfContextMap().get("X-B3-Sampled"));
        }
    }

    /**
     * 创建 SSL连接
     * @return
     * @throws GeneralSecurityException
     */
    private static CloseableHttpClient createSSLInsecureClient() throws GeneralSecurityException {
        try {
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
                public boolean isTrusted(X509Certificate[] chain,String authType) throws CertificateException {
                    return true;
                }
            }).build();

            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, new X509HostnameVerifier() {

                @Override
                public boolean verify(String arg0, SSLSession arg1) {
                    return true;
                }

                @Override
                public void verify(String host, SSLSocket ssl)
                        throws IOException {
                }

                @Override
                public void verify(String host, X509Certificate cert)
                        throws SSLException {
                }

                @Override
                public void verify(String host, String[] cns,
                                   String[] subjectAlts) throws SSLException {
                }

            });

            return HttpClients.custom().setSSLSocketFactory(sslsf).build();

        } catch (GeneralSecurityException e) {
            throw e;
        }
    }

    public static String httpPostWithJson(String url, String json, Integer connTimeout, Integer readTimeout) {
        String returnValue = "";
        CloseableHttpClient httpClient = HttpClients.createDefault();
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        try{
            //第一步：创建HttpClient对象
            httpClient = HttpClients.createDefault();

            //第二步：创建httpPost对象
            HttpPost httpPost = new HttpPost(url);

            //第三步：给httpPost设置JSON格式的参数
            StringEntity requestEntity = new StringEntity(json,"utf-8");
            requestEntity.setContentEncoding("UTF-8");
            httpPost.setHeader("Content-type", "application/json");
            setMDC(httpPost);
            httpPost.setEntity(requestEntity);

            Builder customReqConf = RequestConfig.custom();
            if (connTimeout != null) {
                customReqConf.setConnectTimeout(connTimeout);
            }
            if (readTimeout != null) {
                customReqConf.setSocketTimeout(readTimeout);
            }
            httpPost.setConfig(customReqConf.build());

            //第四步：发送HttpPost请求，获取返回值
            returnValue = httpClient.execute(httpPost,responseHandler); //调接口获取返回值时，必须用此方法
        } catch(Exception e) {
            log.error("HttpPostWithJson error url" + url+ ";请求参数: " + json + ";Exception: " + e);
        }finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                log.error("HttpPostWithJson close error",e);
            }
        }
        //第五步：处理返回值
        return returnValue;
    }
}