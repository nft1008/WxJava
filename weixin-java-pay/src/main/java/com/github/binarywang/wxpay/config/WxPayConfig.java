package com.github.binarywang.wxpay.config;

import com.github.binarywang.wxpay.bean.result.WxPayV3CertificatesResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import lombok.Data;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.ssl.SSLContexts;
import org.bouncycastle.util.io.pem.PemReader;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.SSLContext;
import java.io.*;
import java.net.URL;
import java.security.*;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Enumeration;

/**
 * 微信支付配置
 *
 * @author Binary Wang (https://github.com/binarywang)
 */
@Data
public class WxPayConfig {
  private static final String DEFAULT_PAY_BASE_URL = "https://api.mch.weixin.qq.com";

  /**
   * 微信支付接口请求地址域名部分.
   */
  private String payBaseUrl = DEFAULT_PAY_BASE_URL;

  /**
   * http请求连接超时时间.
   */
  private int httpConnectionTimeout = 5000;

  /**
   * http请求数据读取等待时间.
   */
  private int httpTimeout = 10000;

  /**
   * 公众号appid.
   */
  private String appId;
  /**
   * 服务商模式下的子商户公众账号ID.
   */
  private String subAppId;
  /**
   * 商户号.
   */
  private String mchId;
  /**
   * 商户密钥.
   */
  private String mchKey;
  /**
   * 企业支付密钥.
   */
  private String entPayKey;
  /**
   * 服务商模式下的子商户号.
   */
  private String subMchId;
  /**
   * 微信支付异步回掉地址，通知url必须为直接可访问的url，不能携带参数.
   */
  private String notifyUrl;
  /**
   * 交易类型.
   * <pre>
   * JSAPI--公众号支付
   * NATIVE--原生扫码支付
   * APP--app支付
   * </pre>
   */
  private String tradeType;
  /**
   * 签名方式.
   * 有两种HMAC_SHA256 和MD5
   *
   * @see com.github.binarywang.wxpay.constant.WxPayConstants.SignType
   */
  private String signType;
  private SSLContext sslContext;
  /**
   * p12证书文件的绝对路径或者以classpath:开头的类路径.
   */
  private String keyPath;

  /**
   * p12证书文件内容的字节数组.
   */
  private byte[] keyContent;
  /**
   * 微信支付是否使用仿真测试环境.
   * 默认不使用
   */
  private boolean useSandboxEnv = false;

  /**
   * 是否将接口请求日志信息保存到threadLocal中.
   * 默认不保存
   */
  private boolean ifSaveApiData = false;

  private String httpProxyHost;
  private Integer httpProxyPort;
  private String httpProxyUsername;
  private String httpProxyPassword;

  /**
   * 返回所设置的微信支付接口请求地址域名.
   * @return 微信支付接口请求地址域名
   */
  public String getPayBaseUrl() {
    if (StringUtils.isEmpty(this.payBaseUrl)) {
      return DEFAULT_PAY_BASE_URL;
    }

    return this.payBaseUrl;
  }

  private InputStream getKeyData() throws WxPayException {
    if (StringUtils.isBlank(this.getMchId())) {
      throw new WxPayException("请确保商户号mchId已设置");
    }

    InputStream inputStream;
    if (this.keyContent == null) {
      if (StringUtils.isBlank(this.getKeyPath())) {
        throw new WxPayException("请确保证书文件地址keyPath已配置");
      }

      final String prefix = "classpath:";
      String fileHasProblemMsg = "证书文件【" + this.getKeyPath() + "】有问题，请核实！";
      String fileNotFoundMsg = "证书文件【" + this.getKeyPath() + "】不存在，请核实！";
      if (this.getKeyPath().startsWith(prefix)) {
        String path = StringUtils.removeFirst(this.getKeyPath(), prefix);
        if (!path.startsWith("/")) {
          path = "/" + path;
        }
        inputStream = WxPayConfig.class.getResourceAsStream(path);
        if (inputStream == null) {
          throw new WxPayException(fileNotFoundMsg);
        }
      } else if (this.getKeyPath().startsWith("http://") || this.getKeyPath().startsWith("https://")) {
        try {
          inputStream = new URL(this.keyPath).openStream();
          if (inputStream == null) {
            throw new WxPayException(fileNotFoundMsg);
          }
        } catch (IOException e) {
          throw new WxPayException(fileNotFoundMsg, e);
        }
      } else {
        try {
          File file = new File(this.getKeyPath());
          if (!file.exists()) {
            throw new WxPayException(fileNotFoundMsg);
          }

          inputStream = new FileInputStream(file);
        } catch (IOException e) {
          throw new WxPayException(fileHasProblemMsg, e);
        }
      }

      ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
      byte[] buffer = new byte[1024];
      int len;
      try {
        while ((len = inputStream.read(buffer)) > -1) {
          swapStream.write(buffer, 0, len);
        }
        swapStream.flush();
        this.keyContent = swapStream.toByteArray();
      } catch (IOException e) {
        throw new WxPayException("请确保证书文件地址keyPath已配置");
      }
    }

    return new ByteArrayInputStream(this.keyContent);
  }

  /**
   * 初始化ssl.
   *
   * @return the ssl context
   * @throws WxPayException the wx pay exception
   */
  public SSLContext initSSLContext() throws WxPayException {
    InputStream inputStream = getKeyData();

    try {
      KeyStore keystore = KeyStore.getInstance("PKCS12");
      char[] partnerId2charArray = this.getMchId().toCharArray();
      keystore.load(inputStream, partnerId2charArray);
      this.sslContext = SSLContexts.custom().loadKeyMaterial(keystore, partnerId2charArray).build();
      return this.sslContext;
    } catch (Exception e) {
      throw new WxPayException("证书文件有问题，请核实！", e);
    } finally {
      IOUtils.closeQuietly(inputStream);
    }
  }


  /**
   * 获取私钥。
   *
   * @return 私钥对象
   */
  public PrivateKey getPrivateKey() throws WxPayException {
    InputStream inputStream = getKeyData();

    try {
      KeyStore keystore = KeyStore.getInstance("PKCS12");
      char[] partnerId2charArray = this.getMchId().toCharArray();
      keystore.load(inputStream, partnerId2charArray);

      Enumeration enume = keystore.aliases();
      String keyAlias = null;
      if (enume.hasMoreElements()) {
        keyAlias = (String) enume.nextElement();
      }
      return (PrivateKey) keystore.getKey(keyAlias, partnerId2charArray);
    } catch (Exception e) {
      throw new WxPayException("证书文件有问题，请核实！", e);
    } finally {
      IOUtils.closeQuietly(inputStream);
    }
  }

  /**
   * 获取证书
   * @return
   * @throws WxPayException
   */
  public X509Certificate getCert() throws WxPayException {
    InputStream inputStream = getKeyData();

    try {
      KeyStore keystore = KeyStore.getInstance("PKCS12");
      char[] partnerId2charArray = this.getMchId().toCharArray();
      keystore.load(inputStream, partnerId2charArray);

      Enumeration enume = keystore.aliases();
      String keyAlias = null;
      if (enume.hasMoreElements()) {
        keyAlias = (String) enume.nextElement();
      }
      return (X509Certificate) keystore.getCertificate(keyAlias);
    } catch (Exception e) {
      throw new WxPayException("证书文件有问题，请核实！", e);
    } finally {
      IOUtils.closeQuietly(inputStream);
    }
  }

  /**
   * 获取证书序列号
   * @return
   * @throws WxPayException
   */
  public String getSerialNo() throws WxPayException{
    return this.getCert().getSerialNumber().toString(16).toUpperCase();
  }

  /**
   * 平台证书
   * @return
   * @throws WxPayException
   */
  public X509Certificate getPlatformCert(WxPayV3CertificatesResult.Certificate.EncryptCertificate encryptCertificate) throws WxPayException {
    try {
      String certStr = this.decryptToString(encryptCertificate.getAssociatedData().getBytes(), encryptCertificate.getNonce().getBytes(), encryptCertificate.getCiphertext());
      CertificateFactory ft = CertificateFactory.getInstance("X.509");
      return (X509Certificate) ft.generateCertificate(new ByteArrayInputStream(certStr.getBytes()));
    } catch (Exception e) {
      throw new WxPayException("证书文件有问题，请核实！", e);
    }
  }

  public String getSerialNo(X509Certificate certificate) {
    return certificate.getSerialNumber().toString(16).toUpperCase();
  }

  public String decryptToString(byte[] associatedData, byte[] nonce, String ciphertext)
    throws GeneralSecurityException, IOException {
    try {
      Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");

      SecretKeySpec key = new SecretKeySpec(this.getMchKey().getBytes(), "AES");
      GCMParameterSpec spec = new GCMParameterSpec(128, nonce);

      cipher.init(Cipher.DECRYPT_MODE, key, spec);
      cipher.updateAAD(associatedData);

      return new String(cipher.doFinal(Base64.getDecoder().decode(ciphertext)), "utf-8");
    } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
      throw new IllegalStateException(e);
    } catch (InvalidKeyException | InvalidAlgorithmParameterException e) {
      throw new IllegalArgumentException(e);
    }
  }

  /**
   * 获取公钥
   * @return
   * @throws WxPayException
   */
  public PublicKey getPublicKey() throws WxPayException {
    return this.getCert().getPublicKey();
  }

  public String getPrivateKeyStr() throws WxPayException {
    return Base64.getEncoder().encodeToString(this.getPrivateKey().getEncoded());
  }

}
