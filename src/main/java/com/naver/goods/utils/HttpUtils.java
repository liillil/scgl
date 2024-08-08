package com.naver.goods.utils;

import com.alibaba.fastjson.JSONObject;
import com.naver.goods.entity.GoodsComException;
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
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
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
import org.eclipse.jetty.client.HttpProxy;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.MDC;
import org.apache.http.client.config.RequestConfig.Builder;


import javax.net.ssl.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketTimeoutException;
import java.security.GeneralSecurityException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
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

    public static String getForm(String url, Map<String, String> headers, Integer connTimeout, Integer readTimeout) throws ConnectTimeoutException,
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
            log.error(">>>getForm error:{}", e);
        } finally {
            get.releaseConnection();
            if (url.startsWith("https") && client != null
                    && client instanceof CloseableHttpClient) {
                ((CloseableHttpClient) client).close();
            }
        }
        return "";
    }

    public static String putForm(String url, Map<String, Object> params, Map<String, String> headers, Integer connTimeout, Integer readTimeout) throws ConnectTimeoutException,
            SocketTimeoutException, Exception {
        CloseableHttpClient client = null;
        HttpPut put = new HttpPut(url);
        try {
            if (params != null && !params.isEmpty()) {
                List<BasicNameValuePair> formParams = new ArrayList<>();
                Set<Map.Entry<String, Object>> entrySet = params.entrySet();
                for (Map.Entry<String, Object> entry : entrySet) {
                    formParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue() + ""));
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
                    formParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue() + ""));
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

    private static void setMDC(HttpRequestBase requestBase) {
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
     *
     * @return
     * @throws GeneralSecurityException
     */
    private static CloseableHttpClient createSSLInsecureClient() throws GeneralSecurityException {
        try {
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
                public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
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

    private static void initUnSecureTSL() {
        // 创建信任管理器(不验证证书)
        final TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    //检查客户端证书
                    public void checkClientTrusted(final X509Certificate[] chain, final String authType) {
                        //do nothing 接受任意客户端证书
                    }

                    //检查服务器端证书
                    public void checkServerTrusted(final X509Certificate[] chain, final String authType) {
                        //do nothing 接受任意服务端证书
                    }

                    //返回受信任的X509证书
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
                }
        };
        try {
            // 创建SSLContext对象，并使用指定的信任管理器初始化
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            //基于信任管理器，创建套接字工厂
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            //为HttpsURLConnection配置套接字工厂
            HttpsURLConnection.setDefaultSSLSocketFactory(sslSocketFactory);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Document rawDataHomePage(String url) {
        try {
//            initUnSecureTSL();
            Document document = Jsoup.connect(url)
//                    .proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("14.29.233.12", 8093)))
                    .get();
            return document;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static SSLContext createIgnoreVerifySSL() throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext sc = SSLContext.getInstance("SSLv3");

        // 实现一个X509TrustManager接口，用于绕过验证，不用修改里面的方法
        X509TrustManager trustManager = new X509TrustManager() {
            @Override
            public void checkClientTrusted(
                    java.security.cert.X509Certificate[] paramArrayOfX509Certificate,
                    String paramString) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(
                    java.security.cert.X509Certificate[] paramArrayOfX509Certificate,
                    String paramString) throws CertificateException {
            }

            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };

        sc.init(null, new TrustManager[]{trustManager}, null);
        return sc;
    }

    public static String httpPutWithJson(String url, String json, Map<String, String> headers, Integer connTimeout, Integer readTimeout) throws Exception{
        String returnValue = "";
        CloseableHttpClient httpClient = null;
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        try {
//            SSLContext sslcontext = createIgnoreVerifySSL();
//
//            //设置协议http和https对应的处理socket链接工厂的对象
//            Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
//                    .register("http", PlainConnectionSocketFactory.INSTANCE)
//                    .register("https", new SSLConnectionSocketFactory(sslcontext))
//                    .build();
//            PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
//            HttpClients.custom().setConnectionManager(connManager);
//
//
//            //创建自定义的httpclient对象
//            httpClient = HttpClients.custom().setConnectionManager(connManager).build();

            //第一步：创建HttpClient对象
            httpClient = HttpClients.createDefault();

            //第二步：创建httpPost对象
            HttpPut httpPut = new HttpPut(url);

            //第三步：给httpPost设置JSON格式的参数
            StringEntity requestEntity = new StringEntity(json, "utf-8");
            requestEntity.setContentEncoding("UTF-8");
            httpPut.setHeader("Content-type", "application/json");
            if (headers != null && headers.size() > 0) {
                for (String key : headers.keySet()) {
                    httpPut.setHeader(key, headers.get(key));
                }
            }
            setMDC(httpPut);
            httpPut.setEntity(requestEntity);

            Builder customReqConf = RequestConfig.custom();
            if (connTimeout != null) {
                customReqConf.setConnectTimeout(connTimeout);
            }
            if (readTimeout != null) {
                customReqConf.setSocketTimeout(readTimeout);
            }
            httpPut.setConfig(customReqConf.build());

            //第四步：发送HttpPost请求，获取返回值
            returnValue = httpClient.execute(httpPut, responseHandler); //调接口获取返回值时，必须用此方法
//            log.info(">>> returnValue:{}", returnValue);
        } catch (Exception e) {
            log.error("HttpPutWithJson error url" + url + ";Exception: " + e);
            throw e;
        } finally {
            try {
                httpClient.close();
            } catch (IOException ioException) {
                log.error("HttpPutWithJson close error", ioException);
            }
        }
        //第五步：处理返回值
        return returnValue;
    }
}
