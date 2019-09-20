package com.github.binarywang.wxpay.bean.result;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

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
public class WxPayProfitSharingAddReceiverResult extends BaseWxPayResult {

  /**
   * 分账接收方
   * 分账接收方对象（不包含分账接收方全称），json格式
   * {"type":"MERCHANT_ID","account":"190001001" }
   */
  @XStreamAlias("receiver")
  private String receiver;

}
