package com.github.binarywang.wxpay.util;

import com.github.binarywang.wxpay.exception.WxPayException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.*;
import java.security.cert.Certificate;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.*;

public class V3Utils {

  /**
   * 构造签名串
   *
   * @param requestMethod  GET,POST,PUT等
   * @param url       请求接口 /v3/certificates
   * @param timestamp 获取发起请求时的系统当前时间戳
   * @param nonceStr  随机字符串
   * @param body      请求报文主体
   * @return 待签名字符串
   */
  public static String buildSignMessage(String requestMethod, String url, long timestamp, String nonceStr, String body) {
    return new StringBuffer()
      .append(requestMethod)
      .append("\n")
      .append(url)
      .append("\n")
      .append(timestamp)
      .append("\n")
      .append(nonceStr)
      .append("\n")
      .append(body)
      .append("\n")
      .toString();
  }

  /**
   * 获取授权认证信息
   *
   * @param mchId     商户号
   * @param serialNo  商户API证书序列号
   * @param nonceStr  请求随机串
   * @param timestamp 时间戳
   * @param signature 签名值
   * @param authType  认证类型，目前为WECHATPAY2-SHA256-RSA2048
   * @return 请求头 Authorization
   */
  public static String getAuthorization(String mchId, String serialNo, String nonceStr, String timestamp, String signature, String authType) {
    Map<String, String> params = new HashMap<>(5);
    params.put("mchid", mchId);
    params.put("serial_no", serialNo);
    params.put("nonce_str", nonceStr);
    params.put("timestamp", timestamp);
    params.put("signature", signature);
    return authType.concat(" ").concat(createLinkString(params, ",", false, true));
  }

  public static String createLinkString(Map<String, String> params, String connStr, boolean encode, boolean quotes) {
    List<String> keys = new ArrayList<String>(params.keySet());
    Collections.sort(keys);
    StringBuffer content = new StringBuffer();
    for (int i = 0; i < keys.size(); i++) {
      String key = keys.get(i);
      String value = params.get(key);
      // 拼接时，不包括最后一个&字符
      if (i == keys.size() - 1) {
        if (quotes) {
          content.append(key + "=" + '"' + (encode ? urlEncode(value) : value) + '"');
        } else {
          content.append(key + "=" + (encode ? urlEncode(value) : value));
        }
      } else {
        if (quotes) {
          content.append(key + "=" + '"' + (encode ? urlEncode(value) : value) + '"' + connStr);
        } else {
          content.append(key + "=" + (encode ? urlEncode(value) : value) + connStr);
        }
      }
    }
    return content.toString();
  }

  /**
   * URL 编码
   *
   * @param src 需要编码的字符串
   * @return 编码后的字符串
   */
  public static String urlEncode(String src) {
    try {
      return URLEncoder.encode(src, "UTF-8").replace("+", "%20");
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * 私钥签名
   * @param data       需要加密的数据
   * @param privateKey 私钥
   * @return 加密后的数据
   * @throws Exception
   */
  public static String encryptByPrivateKey(String data, String privateKey) throws WxPayException {
    try {
      PKCS8EncodedKeySpec priPkcs8 = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey));
      KeyFactory keyFactory = KeyFactory.getInstance("RSA");
      PrivateKey priKey = keyFactory.generatePrivate(priPkcs8);
      java.security.Signature signature = java.security.Signature.getInstance("SHA256WithRSA");

      signature.initSign(priKey);
      signature.update(data.getBytes("UTF-8"));
      byte[] signed = signature.sign();
      return Base64.getEncoder().encodeToString(signed);
    } catch (Exception e) {
      throw new WxPayException("签名失败");
    }
  }

  /**
   * 加密数据
   * @param message
   * @param certificate
   * @return
   * @throws IllegalBlockSizeException
   * @throws IOException
   */
  public static String rsaEncryptOAEP(String message, Certificate certificate)
    throws IllegalBlockSizeException, IOException {
    try {
      Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-1AndMGF1Padding");
      cipher.init(Cipher.ENCRYPT_MODE, certificate.getPublicKey());

      byte[] data = message.getBytes("utf-8");
      byte[] cipherdata = cipher.doFinal(data);
      return Base64.getEncoder().encodeToString(cipherdata);
    } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
      throw new RuntimeException("当前Java环境不支持RSA v1.5/OAEP", e);
    } catch (InvalidKeyException e) {
      throw new IllegalArgumentException("无效的证书", e);
    } catch (IllegalBlockSizeException | BadPaddingException e) {
      throw new IllegalBlockSizeException("加密原串的长度不能超过214字节");
    }
  }

  public static String rsaEncryptOAEP(String message, PublicKey publicKey)
    throws IllegalBlockSizeException, IOException {
    try {
      Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-1AndMGF1Padding");
      cipher.init(Cipher.ENCRYPT_MODE, publicKey);

      byte[] data = message.getBytes("utf-8");
      byte[] cipherdata = cipher.doFinal(data);
      return Base64.getEncoder().encodeToString(cipherdata);
    } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
      throw new RuntimeException("当前Java环境不支持RSA v1.5/OAEP", e);
    } catch (InvalidKeyException e) {
      throw new IllegalArgumentException("无效的证书", e);
    } catch (IllegalBlockSizeException | BadPaddingException e) {
      throw new IllegalBlockSizeException("加密原串的长度不能超过214字节");
    }
  }

  /**
   * 加密数据
   * @param ciphertext
   * @param privateKey
   * @return
   * @throws BadPaddingException
   * @throws IOException
   */
  public static String rsaDecryptOAEP(String ciphertext, PrivateKey privateKey)
    throws BadPaddingException, IOException {
    try {
      Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-1AndMGF1Padding");
      cipher.init(Cipher.DECRYPT_MODE, privateKey);

      byte[] data = Base64.getDecoder().decode(ciphertext);
      return new String(cipher.doFinal(data), "utf-8");
    } catch (NoSuchPaddingException | NoSuchAlgorithmException e) {
      throw new RuntimeException("当前Java环境不支持RSA v1.5/OAEP", e);
    } catch (InvalidKeyException e) {
      throw new IllegalArgumentException("无效的私钥", e);
    } catch (BadPaddingException | IllegalBlockSizeException e) {
      throw new BadPaddingException("解密失败");
    }
  }

}
