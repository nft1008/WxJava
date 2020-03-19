package com.github.binarywang.wxpay.bean.result;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.w3c.dom.Document;

/**
 * <pre>
 * 在发起微信支付前，需要调用统一下单接口，获取"预支付交易会话标识"返回的结果
 * 统一下单(详见https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_1)
 * </pre>
 *
 * @author chanjarster
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@XStreamAlias("xml")
public class WxPayProfitSharingResult extends BaseWxPayResult {

  /**
   * 微信订单号
   */
  @XStreamAlias("transaction_id")
  private String transactionId;

  /**
   * 商户分账单号
   */
  @XStreamAlias("out_order_no")
  private String outOrderNo;

  /**
   * 微信分账单号
   */
  @XStreamAlias("order_id")
  private String orderId;

  @Override
  protected void loadXML(Document d) {
    transactionId = readXMLString(d, "transaction_id");
    outOrderNo = readXMLString(d, "out_order_no");
    orderId = readXMLString(d, "order_id");
  }
}
