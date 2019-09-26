package com.github.binarywang.wxpay.bean.request;

import com.github.binarywang.wxpay.exception.WxPayException;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.*;

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
public class WxPayProfitSharingFinishRequest extends BaseWxPayRequest {

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
   * 字段名：分账完结描述.
   * 变量名：description
   * 是否必填：是
   * 类型：string(80)
   * 示例值：	分账已完成
   * 描述：分账已完成
   * 更多信息，详见文档：https://pay.weixin.qq.com/wiki/doc/api/allocation.php?chapter=27_5&index=6
   * </pre>
   */
  @XStreamAlias("description")
  private String description;


  @Override
  protected void checkConstraints() {
    return;
  }
}
