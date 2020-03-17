package com.github.binarywang.wxpay.bean.request;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(builderMethodName = "newBuilder")
@NoArgsConstructor
@AllArgsConstructor
public class WxPayV3MediaUploadRequest {
  /**
   * 图片文件
   * 将媒体图片进行二进制转换，得到的媒体图片二进制内容，在请求body中上传此二进制内容。媒体图片只支持JPG、BMP、PNG格式，文件大小不能超过2M。
   */
  @SerializedName("file")
  private byte[] file;

  /**
   * 媒体文件元信息
   * 媒体文件元信息，使用json表示，包含两个对象：filename、sha256。
   */
  @SerializedName("meta")
  private Meta meta;

  public static class Meta {
    /**
     * 文件名称
     * 商户上传的媒体图片的名称，商户自定义，必须以JPG、BMP、PNG为后缀。
     */
    @SerializedName("filename")
    private String filename;

    /**
     * 文件摘要
     * 图片文件的文件摘要，即对图片文件的二进制内容进行sha256计算得到的值。
     */
    @SerializedName("sha256")
    private String sha256;
  }

}
