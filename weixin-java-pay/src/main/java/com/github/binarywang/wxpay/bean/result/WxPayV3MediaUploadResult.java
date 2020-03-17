package com.github.binarywang.wxpay.bean.result;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WxPayV3MediaUploadResult {

  @SerializedName("media_id")
  private String mediaId;

}
