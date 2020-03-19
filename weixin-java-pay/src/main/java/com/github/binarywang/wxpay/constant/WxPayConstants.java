package com.github.binarywang.wxpay.constant;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.time.FastDateFormat;

import com.google.common.collect.Lists;
import org.bouncycastle.asn1.mozilla.PublicKeyAndChallenge;
import java.text.Format;
import java.util.List;

/**
 * <pre>
 * 微信支付常量类
 * Created by Binary Wang on 2017-8-24.
 * </pre>
 *
 * @author <a href="https://github.com/binarywang">Binary Wang</a>
 */
public class WxPayConstants {

  /**
   * 拉取订单评价数据接口的参数中日期格式.
   */
  public static final Format QUERY_COMMENT_DATE_FORMAT = FastDateFormat.getInstance("yyyyMMddHHmmss");

  public static class RequestMethod {
    /**
     * post 请求
     */
    public static final String POST = "POST";
    /**
     * get 请求
     */
    public static final String GET = "GET";
    /**
     * put 请求
     */
    public static final String PUT = "PUT";
    /**
     * delete 请求
     */
    public static final String DELETE = "DELETE";
    /**
     * options 请求
     */
    public static final String OPTIONS = "OPTIONS";
    /**
     * head 请求
     */
    public static final String HEAD = "HEAD";
    /**
     * trace 请求
     */
    public static final String TRACE = "TRACE";
    /**
     * connect 请求
     */
    public static final String CONNECT = "CONNECT";
  }

  /**
   * 校验用户姓名选项，企业付款时使用.
   */
  public static class CheckNameOption {
    /**
     * 不校验真实姓名.
     */
    public static final String NO_CHECK = "NO_CHECK";

    /**
     * 强校验真实姓名.
     */
    public static final String FORCE_CHECK = "FORCE_CHECK";
  }

  /**
   * 压缩账单的类型.
   */
  public static class TarType {
    /**
     * 固定值：GZIP，返回格式为.gzip的压缩包账单.
     */
    public static final String GZIP = "GZIP";
  }

  /**
   * 账单类型.
   */
  public static class BillType {
    /**
     * 查询红包时使用：通过商户订单号获取红包信息.
     */
    public static final String MCHT = "MCHT";

    //以下为下载对账单时的账单类型
    /**
     * 返回当日所有订单信息，默认值.
     */
    public static final String ALL = "ALL";
    /**
     * 返回当日成功支付的订单.
     */
    public static final String SUCCESS = "SUCCESS";
    /**
     * 返回当日退款订单.
     */
    public static final String REFUND = "REFUND";
    /**
     * 返回当日充值退款订单（相比其他对账单多一栏“返还手续费”）.
     */
    public static final String RECHARGE_REFUND = "RECHARGE_REFUND";
  }

  /**
   * 交易类型.
   */
  public static class TradeType {
    /**
     * 原生扫码支付.
     */
    public static final String NATIVE = "NATIVE";

    /**
     * App支付.
     */
    public static final String APP = "APP";

    /**
     * 公众号支付/小程序支付.
     */
    public static final String JSAPI = "JSAPI";

    /**
     * H5支付.
     */
    public static final String MWEB = "MWEB";

    /**
     * 刷卡支付.
     * 刷卡支付有单独的支付接口，不调用统一下单接口
     */
    public static final String MICROPAY = "MICROPAY";
  }

  /**
   * 账户类型
   */
  public static class AccountType {
    /**
     * 基本账户
     */
    public static final String BASIC = "Basic";
    /**
     * 运营账户
     */
    public static final String OPERATION = "Operation";
    /**
     * Fees
     */
    public static final String FEES = "Fees";
  }

  /**
   * 签名类型.
   */
  public static class SignType {
    /**
     * The constant HMAC_SHA256.
     */
    public static final String HMAC_SHA256 = "HMAC-SHA256";
    /**
     * The constant MD5.
     */
    public static final String MD5 = "MD5";
    /**
     * The constant ALL_SIGN_TYPES.
     */
    public static final List<String> ALL_SIGN_TYPES = Lists.newArrayList(HMAC_SHA256, MD5);
  }

  /**
   * 限定支付方式.
   */
  public static class LimitPay {
    /**
     * no_credit--指定不能使用信用卡支付.
     */
    public static final String NO_CREDIT = "no_credit";
  }

  /**
   * 业务结果代码.
   */
  public static class ResultCode {
    /**
     * 成功.
     */
    public static final String SUCCESS = "SUCCESS";

    /**
     * 失败.
     */
    public static final String FAIL = "FAIL";
  }

  /**
   * 退款资金来源.
   */
  public static class RefundAccountSource {
    /**
     * 可用余额退款/基本账户.
     */
    public static final String RECHARGE_FUNDS = "REFUND_SOURCE_RECHARGE_FUNDS";

    /**
     * 未结算资金退款.
     */
    public static final String UNSETTLED_FUNDS = "REFUND_SOURCE_UNSETTLED_FUNDS";

  }

  /**
   * 退款渠道.
   */
  public static class RefundChannel {
    /**
     * 原路退款.
     */
    public static final String ORIGINAL = "ORIGINAL";

    /**
     * 退回到余额.
     */
    public static final String BALANCE = "BALANCE";

    /**
     * 原账户异常退到其他余额账户.
     */
    public static final String OTHER_BALANCE = "OTHER_BALANCE";

    /**
     * 原银行卡异常退到其他银行卡.
     */
    public static final String OTHER_BANKCARD = "OTHER_BANKCARD";
  }

  /**
   * 交易状态.
   */
  public static class WxpayTradeStatus {
    /**
     * 支付成功.
     */
    public static final String SUCCESS = "SUCCESS";

    /**
     * 支付失败(其他原因，如银行返回失败).
     */
    public static final String PAY_ERROR = "PAYERROR";

    /**
     * 用户支付中.
     */
    public static final String USER_PAYING = "USERPAYING";

    /**
     * 已关闭.
     */
    public static final String CLOSED = "CLOSED";

    /**
     * 未支付.
     */
    public static final String NOTPAY = "NOTPAY";

    /**
     * 转入退款.
     */
    public static final String REFUND = "REFUND";

    /**
     * 已撤销（刷卡支付）.
     */
    public static final String REVOKED = "REVOKED";
  }

  /**
   * 退款状态.
   */
  public static class RefundStatus {
    /**
     * 退款成功.
     */
    public static final String SUCCESS = "SUCCESS";

    /**
     * 退款关闭.
     */
    public static final String REFUND_CLOSE = "REFUNDCLOSE";

    /**
     * 退款处理中.
     */
    public static final String PROCESSING = "PROCESSING";

    /**
     * 退款异常.
     * 退款到银行发现用户的卡作废或者冻结了，导致原路退款银行卡失败，可前往商户平台（pay.weixin.qq.com）-交易中心，手动处理此笔退款。
     */
    public static final String CHANGE = "CHANGE";
  }

  /**
   * 主体类型
   */
  public static class SubjectType {
    /**
     * 个体户
     */
    public static final String INDIVIDUAL = "SUBJECT_TYPE_INDIVIDUAL";

    /**
     * 企业
     */
    public static final String ENTERPRISE = "SUBJECT_TYPE_ENTERPRISE";

    /**
     * 党政、机关及事业单位
     */
    public static final String INSTITUTIONS = "SUBJECT_TYPE_INSTITUTIONS";

    /**
     * 其他组织
     */
    public static final String OTHERS = "SUBJECT_TYPE_OTHERS";
  }

  /**
   * 证件类型
   */
  public static class IdentificationType {
    /**
     * 中国大陆居民-身份证
     */
    public static final String IDCARD = "IDENTIFICATION_TYPE_IDCARD";

    /**
     * 其他国家或地区居民-护照
     */
    public static final String OVERSEA_PASSPORT = "IDENTIFICATION_TYPE_OVERSEA_PASSPORT";

    /**
     * 中国香港居民-来往内地通行证
     */
    public static final String HONGKONG_PASSPORT = "IDENTIFICATION_TYPE_HONGKONG_PASSPORT";

    /**
     * 中国澳门居民-来往内地通行证
     */
    public static final String MACAO_PASSPORT = "IDENTIFICATION_TYPE_MACAO_PASSPORT";

    /**
     * 中国台湾居民-来往大陆通行证
     */
    public static final String TAIWAN_PASSPORT = "IDENTIFICATION_TYPE_TAIWAN_PASSPORT";
  }

  /**
   * 经营场景类型
   */
  public static class SalesScenesType {
    /**
     * 线下门店
     */
    public static final String STORE = "SALES_SCENES_STORE";

    /**
     * 公众号
     */
    public static final String MP = "SALES_SCENES_MP";

    /**
     * 小程序
     */
    public static final String MINI_PROGRAM = "SALES_SCENES_MINI_PROGRAM";

    /**
     * 互联网
     */
    public static final String WEB = "SALES_SCENES_WEB";

    /**
     * APP
     */
    public static final String APP = "SALES_SCENES_APP";

    /**
     * 企业微信
     */
    public static final String WEWORK = "SALES_SCENES_WEWORKP";
  }

  /**
   * 账户类型
   */
  public static class BankAccountType {
    /**
     * 对公银行账户
     */
    public static final String CORPORATE = "BANK_ACCOUNT_TYPE_CORPORATE";

    /**
     * 经营者个人银行卡
     */
    public static final String PERSONAL = "BANK_ACCOUNT_TYPE_PERSONAL";
  }

  /**
   * 申请单状态
   */
  public static class ApplymentState {
    /**
     * （编辑中）：提交申请发生错误导致，请尝试重新提交。
     */
    public static final String EDITTING = "APPLYMENT_STATE_EDITTING";

    /**
     * （审核中）：申请单正在审核中，超级管理员用微信打开“签约链接”，完成绑定微信号后，申请单进度将通过微信公众号通知超级管理员，引导完成后续步骤。
     */
    public static final String AUDITING = "APPLYMENT_STATE_AUDITING";

    /**
     * （已驳回）：请按照驳回原因修改申请资料，超级管理员用微信打开“签约链接”，完成绑定微信号，后续申请单进度将通过微信公众号通知超级管理员。
     */
    public static final String REJECTED = "APPLYMENT_STATE_REJECTED";

    /**
     * （待账户验证）：请超级管理员使用微信打开返回的“签约链接”，根据页面指引完成账户验证。
     */
    public static final String TO_BE_CONFIRMED = "APPLYMENT_STATE_TO_BE_CONFIRMED";

    /**
     * （待签约）：请超级管理员使用微信打开返回的“签约链接”，根据页面指引完成签约。
     */
    public static final String TO_BE_SIGNED = "APPLYMENT_STATE_TO_BE_SIGNED";

    /**
     * （开通权限中）：系统开通相关权限中，请耐心等待。
     */
    public static final String SIGNING = "APPLYMENT_STATE_SIGNING";

    /**
     * （已完成）：商户入驻申请已完成。
     */
    public static final String FINISHED = "APPLYMENT_STATE_FINISHED";

    /**
     * （已作废）：申请单已被撤销。
     */
    public static final String CANCELED = "APPLYMENT_STATE_CANCELED";
  }

  public static class ReceiverType {
    /**
     * 商户id
     */
    public static final String MERCHANT_ID = "MERCHANT_ID";
    /**
     * 个人微信号
     */
    public static final String PERSONAL_WECHATID = "PERSONAL_WECHATID";
    /**
     * 个人openid
     */
    public static final String PERSONAL_OPENID = "PERSONAL_OPENID";
    /**
     * 个人sub_openid
     */
    public static final String PERSONAL_SUB_OPENID = "PERSONAL_SUB_OPENID";
  }
}
