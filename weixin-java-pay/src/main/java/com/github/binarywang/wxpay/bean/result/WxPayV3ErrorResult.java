package com.github.binarywang.wxpay.bean.result;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WxPayV3ErrorResult {

  @SerializedName("code")
  private String code;

  @SerializedName("message")
  private String message;

  @SerializedName("detail")
  private Detail detail;

  @Data
  public static class Detail {

    @SerializedName("detail")
    private String detail;

    @SerializedName("value")
    private String value;

    @SerializedName("issue")
    private String issue;

    @SerializedName("location")
    private String location;
  }

}
