package com.github.binarywang.wxpay.bean.result;

import com.github.binarywang.wxpay.exception.WxPayException;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WxPayV3Applyment4SubStatusResult {

  /**
   * 业务申请编号
   */
  @SerializedName("business_code")
  private String businessCode;

  /**
   * 微信支付申请单号
   */
  @SerializedName("applyment_id")
  private String applymentId;

  /**
   * 特约商户号
   */
  @SerializedName("sub_mchid")
  private String subMchid;

  /**
   * 超级管理员签约链接
   */
  @SerializedName("sign_url")
  private String signUrl;

  /**
   * 申请单状态
   */
  @SerializedName("applyment_state")
  private String applymentState;

  /**
   * 申请状态描述
   */
  @SerializedName("applyment_state_msg")
  private String applymentStateMsg;

  /**
   * 驳回原因详情
   * 各项资料的审核情况，当申请状态为APPLYMENT_STATE_REJECTED时才返回。
   */
  @SerializedName("audit_detail")
  private AuditDetail[] auditDetail;

  @Data
  static class AuditDetail {
    /**
     * 字段名
     * 提交申请单的资料项字段名。
     */
    @SerializedName("field")
    private String field;

    /**
     * 字段名称
     * 提交申请单的资料项字段名称。
     */
    @SerializedName("field_name")
    private String fieldName;

    /**
     * 驳回原因
     * 提交资料项被驳回的原因。
     */
    @SerializedName("reject_reason")
    private String rejectReason;
  }

}
