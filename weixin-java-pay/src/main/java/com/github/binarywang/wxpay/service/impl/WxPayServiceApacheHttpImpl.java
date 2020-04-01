package com.github.binarywang.wxpay.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;
import javax.net.ssl.SSLContext;

import com.github.binarywang.wxpay.bean.WxPayApiData;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.util.V3Utils;
import jodd.util.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * <pre>
 * 微信支付请求实现类，apache httpclient实现.
 * Created by Binary Wang on 2016/7/28.
 * </pre>
 *
 * @author <a href="https://github.com/binarywang">Binary Wang</a>
 */
public class WxPayServiceApacheHttpImpl extends BaseWxPayServiceImpl {
  @Override
  public byte[] postForBytes(String url, String requestStr, boolean useKey) throws WxPayException {
    try {
      HttpClientBuilder httpClientBuilder = createHttpClientBuilder(useKey);
      HttpPost httpPost = this.createHttpPost(url, requestStr);
      try (CloseableHttpClient httpClient = httpClientBuilder.build()) {
        try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
          final byte[] bytes = EntityUtils.toByteArray(response.getEntity());
          final String responseData = Base64.encodeToString(bytes);
          this.log.info("\n【请求地址】：{}\n【请求数据】：{}\n【响应数据(Base64编码后)】：{}", url, requestStr, responseData);
          wxApiData.set(new WxPayApiData(url, requestStr, responseData, null));
          return bytes;
        }
      } finally {
        httpPost.releaseConnection();
      }
    } catch (Exception e) {
      this.log.error("\n【请求地址】：{}\n【请求数据】：{}\n【异常信息】：{}", url, requestStr, e.getMessage());
      wxApiData.set(new WxPayApiData(url, requestStr, null, e.getMessage()));
      throw new WxPayException(e.getMessage(), e);
    }
  }

  @Override
  public String post(String url, String requestStr, boolean useKey) throws WxPayException {
    try {
      HttpClientBuilder httpClientBuilder = this.createHttpClientBuilder(useKey);
      HttpPost httpPost = this.createHttpPost(url, requestStr);
      try (CloseableHttpClient httpClient = httpClientBuilder.build()) {
        try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
          String responseString = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
          this.log.info("\n【请求地址】：{}\n【请求数据】：{}\n【响应数据】：{}", url, requestStr, responseString);
          if (this.getConfig().isIfSaveApiData()) {
            wxApiData.set(new WxPayApiData(url, requestStr, responseString, null));
          }
          return responseString;
        }
      } finally {
        httpPost.releaseConnection();
      }
    } catch (Exception e) {
      this.log.error("\n【请求地址】：{}\n【请求数据】：{}\n【异常信息】：{}", url, requestStr, e.getMessage());
      throw new WxPayException(e.getMessage(), e);
    }
  }

  @Override
  public String postV3(String urlSuffix, String body, String serialNo) throws WxPayException {
    try {
      HttpClientBuilder httpClientBuilder = this.createHttpClientBuilder(false);
      HttpPost httpPost = this.createHttpPostV3(this.getPayBaseUrl().concat(urlSuffix), this.getAuthorization(WxPayConstants.RequestMethod.POST, urlSuffix, body), body, serialNo);
      try (CloseableHttpClient httpClient = httpClientBuilder.build()) {
        try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
          String responseString = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
          this.log.info("\n【请求地址】：{}\n【请求数据】：{}\n【响应数据】：{}", urlSuffix, body, responseString);
          if (response.getStatusLine().getStatusCode() != 200) {
            throw new WxPayException(responseString);
          }
          return responseString;
        }
      } finally {
        httpPost.releaseConnection();
      }
    } catch (Exception e) {
      this.log.error("\n【请求地址】：{}\n【请求数据】：{}\n【异常信息】：{}", urlSuffix, body, e.getMessage());
      throw new WxPayException(e.getMessage(), e);
    }
  }

  @Override
  public String postFileV3(String urlSuffix, String fileName, File file) throws WxPayException {
    try {
      String fileSha256 = DigestUtils.sha256Hex(new FileInputStream(file));//文件sha256
      String meta = "{\"filename\":\""+fileName+"\",\"sha256\":\""+fileSha256+"\"}";

      HttpClientBuilder httpClientBuilder = this.createHttpClientBuilder(false);
      HttpPost httpPost = this.createHttpPostFileV3(this.getPayBaseUrl().concat(urlSuffix), this.getAuthorization(WxPayConstants.RequestMethod.POST, urlSuffix, meta), this.getFileHttpEntity(fileName, file, meta));
      try (CloseableHttpClient httpClient = httpClientBuilder.build()) {
        try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
          String responseString = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
          this.log.info("\n【请求地址】：{}\n【请求数据】：{}\n【响应数据】：{}", urlSuffix, fileName, responseString);
          if (response.getStatusLine().getStatusCode() != 200) {
            throw new WxPayException(responseString);
          }
          return responseString;
        }
      } finally {
        httpPost.releaseConnection();
      }
    } catch (Exception e) {
      this.log.error("\n【请求地址】：{}\n【请求数据】：{}\n【异常信息】：{}", urlSuffix, fileName, e.getMessage());
      throw new WxPayException(e.getMessage(), e);
    }
  }

  @Override
  public String getV3(String urlSuffix) throws WxPayException {
    try {
      HttpClientBuilder httpClientBuilder = this.createHttpClientBuilder(false);
      HttpGet httpGet = this.createHttpGetV3(this.getPayBaseUrl().concat(urlSuffix), this.getAuthorization(WxPayConstants.RequestMethod.GET, urlSuffix, ""));
      try (CloseableHttpClient httpClient = httpClientBuilder.build()) {
        try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
          String responseString = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
          this.log.info("\n【请求地址】：{}\n【响应数据】：{}", urlSuffix, responseString);
          if (response.getStatusLine().getStatusCode() != 200) {
            throw new WxPayException(responseString);
          }
          return responseString;
        }
      } finally {
        httpGet.releaseConnection();
      }
    } catch (Exception e) {
      this.log.error("\n【请求地址】：{}\n【异常信息】：{}", urlSuffix, e.getMessage());
      if (this.getConfig().isIfSaveApiData()) {
        wxApiData.set(new WxPayApiData(urlSuffix, "", null, e.getMessage()));
      }
      throw new WxPayException(e.getMessage(), e);
    }
  }

  private StringEntity createEntry(String requestStr) {
    try {
      return new StringEntity(new String(requestStr.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1));
    } catch (UnsupportedEncodingException e) {
      //cannot happen
      this.log.error(e.getMessage(), e);
      return null;
    }
  }

  private HttpClientBuilder createHttpClientBuilder(boolean useKey) throws WxPayException {
    HttpClientBuilder httpClientBuilder = HttpClients.custom();
    if (useKey) {
      this.initSSLContext(httpClientBuilder);
    }

    if (StringUtils.isNotBlank(this.getConfig().getHttpProxyHost()) && this.getConfig().getHttpProxyPort() > 0) {
      if (StringUtils.isEmpty(this.getConfig().getHttpProxyUsername())) {
        this.getConfig().setHttpProxyUsername("whatever");
      }

      // 使用代理服务器 需要用户认证的代理服务器
      CredentialsProvider provider = new BasicCredentialsProvider();
      provider.setCredentials(new AuthScope(this.getConfig().getHttpProxyHost(), this.getConfig().getHttpProxyPort()),
        new UsernamePasswordCredentials(this.getConfig().getHttpProxyUsername(), this.getConfig().getHttpProxyPassword()));
      httpClientBuilder.setDefaultCredentialsProvider(provider);
      httpClientBuilder.setProxy(new HttpHost(this.getConfig().getHttpProxyHost(), this.getConfig().getHttpProxyPort()));
    }
    return httpClientBuilder;
  }

  private HttpPost createHttpPost(String url, String requestStr) {
    HttpPost httpPost = new HttpPost(url);
    httpPost.setEntity(this.createEntry(requestStr));

    httpPost.setConfig(RequestConfig.custom()
      .setConnectionRequestTimeout(this.getConfig().getHttpConnectionTimeout())
      .setConnectTimeout(this.getConfig().getHttpConnectionTimeout())
      .setSocketTimeout(this.getConfig().getHttpTimeout())
      .build());

    return httpPost;
  }

  private HttpPost createHttpPostV3(String url, String authorization, String jsonData, String serialNo) {
    HttpPost httpPost = new HttpPost(url);
    Map<String, String> headers = getHeadersV3(authorization);
    for (Map.Entry<String, String> entry : headers.entrySet()) {
      httpPost.setHeader(entry.getKey(), entry.getValue());
    }
    if (StringUtils.isNotBlank(serialNo)) {
      httpPost.setHeader("Wechatpay-Serial", serialNo);
    }
    httpPost.setEntity(new StringEntity(jsonData, StandardCharsets.UTF_8));

    httpPost.setConfig(RequestConfig.custom()
      .setConnectionRequestTimeout(this.getConfig().getHttpConnectionTimeout())
      .setConnectTimeout(this.getConfig().getHttpConnectionTimeout())
      .setSocketTimeout(this.getConfig().getHttpTimeout())
      .build());

    return httpPost;
  }

  private HttpEntity getFileHttpEntity(String fileName, File file, String meta) {
    MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create().setMode(HttpMultipartMode.RFC6532);
    multipartEntityBuilder.setBoundary("boundary");
    multipartEntityBuilder.setCharset(Charset.forName("UTF-8"));
    multipartEntityBuilder.addTextBody("meta", meta, ContentType.APPLICATION_JSON);
    multipartEntityBuilder.addBinaryBody("file", file, ContentType.create("image/jpg"), fileName);
    return multipartEntityBuilder.build();
  }

  private HttpPost createHttpPostFileV3(String url, String authorization, HttpEntity httpEntity) {
    HttpPost httpPost = new HttpPost(url);
    Map<String, String> headers = getFileHeadersV3(authorization);
    for (Map.Entry<String, String> entry : headers.entrySet()) {
      httpPost.setHeader(entry.getKey(), entry.getValue());
    }
    httpPost.setEntity(httpEntity);

    httpPost.setConfig(RequestConfig.custom()
      .setConnectionRequestTimeout(this.getConfig().getHttpConnectionTimeout())
      .setConnectTimeout(this.getConfig().getHttpConnectionTimeout())
      .setSocketTimeout(this.getConfig().getHttpTimeout())
      .build());

    return httpPost;
  }

  private HttpGet createHttpGetV3(String url, String authorization) {
    HttpGet httpGet = new HttpGet(url);
    Map<String, String> headers = getHeadersV3(authorization);
    for (Map.Entry<String, String> entry : headers.entrySet()) {
      httpGet.addHeader(entry.getKey(), entry.getValue());
    }

    httpGet.setConfig(RequestConfig.custom()
      .setConnectionRequestTimeout(this.getConfig().getHttpConnectionTimeout())
      .setConnectTimeout(this.getConfig().getHttpConnectionTimeout())
      .setSocketTimeout(this.getConfig().getHttpTimeout())
      .build());

    return httpGet;
  }

  private void initSSLContext(HttpClientBuilder httpClientBuilder) throws WxPayException {
    SSLContext sslContext = this.getConfig().getSslContext();
    if (null == sslContext) {
      sslContext = this.getConfig().initSSLContext();
    }

    SSLConnectionSocketFactory connectionSocketFactory = new SSLConnectionSocketFactory(sslContext,
      new String[]{"TLSv1"}, null, new DefaultHostnameVerifier());
    httpClientBuilder.setSSLSocketFactory(connectionSocketFactory);
  }

  private String getAuthorization(String requestMethod, String urlSuffix, String body) throws WxPayException{
    long timestamp = System.currentTimeMillis() / 1000;
    String nonceStr = String.valueOf(System.currentTimeMillis());
    // 构建签名参数
    String buildSignMessage = V3Utils.buildSignMessage(requestMethod, urlSuffix, timestamp, nonceStr, body);
    // 获取商户私钥
    String key = this.config.getPrivateKeyStr();
    // 生成签名
    String signature = V3Utils.encryptByPrivateKey(buildSignMessage, key);
    // 根据平台规则生成请求头 authorization
    String authType = "WECHATPAY2-SHA256-RSA2048";
    return V3Utils.getAuthorization(this.config.getMchId(), this.config.getSerialNo(), nonceStr, String.valueOf(timestamp), signature, authType);
  }

  private Map<String, String> getHeadersV3(String authorization) {
    Map<String, String> headers = new HashMap<>();
    headers.put("Charset", "UTF-8");
    headers.put("Content-Type", "application/json");
    headers.put("Accept", "application/json");
    headers.put("Authorization", authorization);
    return headers;
  }

  private Map<String, String> getFileHeadersV3(String authorization) {
    Map<String, String> headers = new HashMap<>();
    headers.put("Charset", "UTF-8");
    headers.put("Content-Type", "multipart/form-data;boundary=boundary");
    headers.put("Accept", "application/json");
    headers.put("Authorization", authorization);
    return headers;
  }
}
