package com.github.binarywang.wxpay.bean.result;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WxPayV3Applyment4SubResult {

  @SerializedName("applyment_id")
  private String applymentId;

}
