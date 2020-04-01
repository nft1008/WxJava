package com.github.binarywang.wxpay.bean.result;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WxPayV3CertificatesResult {

  @SerializedName("data")
  private Certificate[] data;

  @Data
  public static class Certificate {
    @SerializedName("serial_no")
    private String serialNo;

    @SerializedName("effective_time")
    private String effectiveTime;

    @SerializedName("expire_time")
    private String expireTime;

    @SerializedName("encrypt_certificate")
    private EncryptCertificate encryptCertificate;

    @Data
    public static class EncryptCertificate {
      @SerializedName("algorithm")
      private String algorithm;

      @SerializedName("nonce")
      private String nonce;

      @SerializedName("associated_data")
      private String associatedData;

      @SerializedName("ciphertext")
      private String ciphertext;
    }
  }

}
