package com.github.binarywang.wxpay.bean.request;

import com.github.binarywang.wxpay.exception.WxPayException;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.*;

import java.util.List;

/**
 * <pre>
 * 请求单次分账.
 * 参考文档：https://pay.weixin.qq.com/wiki/doc/api/allocation_sl.php?chapter=25_1&index=1
 * Created by swm on 2019/9/17.
 * </pre>
 *
 * @author <a href="https://github.com/binarywang">Binary Wang</a>
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Builder(builderMethodName = "newBuilder")
@NoArgsConstructor
@AllArgsConstructor
@XStreamAlias("xml")
public class WxPayProfitSharingRequest extends BaseWxPayRequest {

  /**
   * <pre>
   * 字段名：微信订单号.
   * 变量名：transaction_id
   * 是否必填：是
   * 类型：String(32)
   * 示例值：4208450740201411110007820472
   * 描述：微信支付订单号。
   * 签名类型，目前只支持HMAC-SHA256
   * 更多信息，详见文档：https://pay.weixin.qq.com/wiki/doc/api/allocation_sl.php?chapter=25_1&index=1
   * </pre>
   */
  @XStreamAlias("transaction_id")
  private String transactionId;

  /**
   * <pre>
   * 字段名：商户分账单号.
   * 变量名：out_order_no
   * 是否必填：是
   * 类型：String(64)
   * 示例值：P20150806125346
   * 描述：服务商系统内部的分账单号，在服务商系统内部唯一（单次分账、多次分账、完结分账应使用不同的商户分账单号），同一分账单号多次请求等同一次。只能是数字、大小写字母_-|*@
   * 更多信息，详见文档：https://pay.weixin.qq.com/wiki/doc/api/allocation_sl.php?chapter=25_1&index=1
   * </pre>
   */
  @XStreamAlias("out_order_no")
  private String outOrderNo;

  /**
   * <pre>
   * 字段名：分账接收方列表.
   * 变量名：receivers
   * 是否必填：是
   * 类型：String(10240)
   * 示例值：[
   *     {
   *          "type": "MERCHANT_ID",
   *          "account":"190001001",
   *          "amount":100,
   *          "description": "分到商户"
   * },
   *     {
   *          "type": "PERSONAL_WECHATID",
   *          "account":"86693952",
   *          "amount":888,
   *          "description": "分到个人"
   * }
   * ]
   * 描述：分账接收方列表，不超过50个json对象，不能设置出资子商户作为分账接受方
   *     点击行前的+展开字段详情
   * 更多信息，详见文档：https://pay.weixin.qq.com/wiki/doc/api/allocation_sl.php?chapter=25_1&index=1
   * </pre>
   */
  @XStreamAlias("receivers")
  private String receivers;


  @AllArgsConstructor
  @NoArgsConstructor
  @Data
  public static class Receiver {

    /**
     * <pre>
     * 字段名：分账接收方类型.
     * 变量名：type
     * 是否必填：是
     * 类型：String(32)
     * 示例值：MERCHANT_ID
     * 描述：MERCHANT_ID：商户ID
     *     PERSONAL_WECHATID：个人微信号PERSONAL_OPENID：个人openid（由父商户APPID转换得到）PERSONAL_SUB_OPENID: 个人sub_openid（由子商户APPID转换得到）
     * 更多信息，详见文档：https://pay.weixin.qq.com/wiki/doc/api/allocation_sl.php?chapter=25_1&index=1
     * </pre>
     */
    String type;

    /**
     * <pre>
     * 字段名：分账接收方类型.
     * 变量名：account
     * 是否必填：是
     * 类型：String(32)
     * 示例值：86693852
     * 描述：类型是MERCHANT_ID时，是商户ID
     *     类型是PERSONAL_WECHATID时，是个人微信号
     *     类型是PERSONAL_OPENID时，是个人openid
     *     类型是PERSONAL_SUB_OPENID时，是个人sub_openid。
     * 更多信息，详见文档：https://pay.weixin.qq.com/wiki/doc/api/allocation_sl.php?chapter=25_1&index=1
     * </pre>
     */
    String account;

    /**
     * <pre>
     * 字段名：分账金额.
     * 变量名：amount
     * 是否必填：是
     * 类型：int
     * 示例值：888
     * 描述：分账金额，单位为分，只能为整数，不能超过原订单支付金额及最大分账比例金额
     * 更多信息，详见文档：https://pay.weixin.qq.com/wiki/doc/api/allocation_sl.php?chapter=25_1&index=1
     * </pre>
     */
    Integer amount;

    /**
     * <pre>
     * 字段名：分账描述.
     * 变量名：description
     * 是否必填：是
     * 类型：string(80)
     * 示例值：分给商户A
     * 描述：分账的原因描述，分账账单中需要体现
     * 更多信息，详见文档：https://pay.weixin.qq.com/wiki/doc/api/allocation_sl.php?chapter=25_1&index=1
     * </pre>
     */
    String description;
  }


  @Override
  protected void checkConstraints() throws WxPayException {
    return;
  }
}
