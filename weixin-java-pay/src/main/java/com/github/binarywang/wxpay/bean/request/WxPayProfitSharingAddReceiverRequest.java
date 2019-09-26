package com.github.binarywang.wxpay.bean.request;

import com.github.binarywang.wxpay.exception.WxPayException;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.*;
import me.chanjar.weixin.common.annotation.Required;

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
public class WxPayProfitSharingAddReceiverRequest extends BaseWxPayRequest {

  /**
   * <pre>
   * 字段名：分账接收方.
   * 变量名：receiver
   * 是否必填：是
   * 类型：String(2048)
   * 示例值：{
   *   "type": "MERCHANT_ID",
   *   "account":"190001001",
   *   "name": "示例商户全称",
   *   "relation_type": "STORE_OWNER"
   * }
   * 描述：分账接收方列表，不超过50个json对象，不能设置出资子商户作为分账接受方
   *     点击行前的+展开字段详情
   * 更多信息，详见文档：https://pay.weixin.qq.com/wiki/doc/api/allocation.php?chapter=27_3&index=4
   * </pre>
   */
  @Required
  @XStreamAlias("receiver")
  private String receiver;


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
     * 更多信息，详见文档：https://pay.weixin.qq.com/wiki/doc/api/allocation.php?chapter=27_3&index=4
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
     * 更多信息，详见文档：https://pay.weixin.qq.com/wiki/doc/api/allocation.php?chapter=27_3&index=4
     * </pre>
     */
    String account;

    /**
     * <pre>
     * 字段名：分账接收方全称.
     * 变量名：name
     * 是否必填：否
     * 类型：string(1024)
     * 示例值：示例商户全称
     * 描述：分账接收方类型是MERCHANT_ID时，是商户全称（必传）
     *   分账接收方类型是PERSONAL_WECHATID 时，是个人姓名（必传）
     *   分账接收方类型是PERSONAL_OPENID时，是个人姓名（选传，传则校验）
     * 更多信息，详见文档：https://pay.weixin.qq.com/wiki/doc/api/allocation.php?chapter=27_3&index=4
     * </pre>
     */
    String name;

    /**
     * <pre>
     * 字段名：与分账方的关系类型.
     * 变量名：relation_type
     * 是否必填：是
     * 类型：string(32)
     * 示例值：SERVICE_PROVIDER
     * 描述：子商户与接收方的关系。
     *   本字段值为枚举：
     *   SERVICE_PROVIDER：服务商
     *   STORE：门店
     *   STAFF：员工
     *   STORE_OWNER：店主
     *   PARTNER：合作伙伴
     *   HEADQUARTER：总部
     *   BRAND：品牌方
     *   DISTRIBUTOR：分销商
     *   USER：用户
     *   SUPPLIER：供应商
     *   CUSTOM：自定义
     * 更多信息，详见文档：https://pay.weixin.qq.com/wiki/doc/api/allocation.php?chapter=27_3&index=4
     * </pre>
     */
    String relation_type;

    /**
     * <pre>
     * 字段名：自定义的分账关系.
     * 变量名：custom_relation
     * 是否必填：否
     * 类型：string(10)
     * 示例值：代理商
     * 描述：子商户与接收方具体的关系，本字段最多10个字。
     *   当字段relation_type的值为CUSTOM时，本字段必填
     *   当字段relation_type的值不为CUSTOM时，本字段无需填写
     * 更多信息，详见文档：https://pay.weixin.qq.com/wiki/doc/api/allocation.php?chapter=27_3&index=4
     * </pre>
     */
    String custom_relation;

  }


  @Override
  protected void checkConstraints() {
    return;
  }
}
