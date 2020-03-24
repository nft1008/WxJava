package com.github.binarywang.wxpay.bean.request;

import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.util.V3Utils;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.security.cert.X509Certificate;

@Data
@Builder(builderMethodName = "newBuilder")
@NoArgsConstructor
@AllArgsConstructor
public class WxPayV3Applyment4SubRequest {

  /**
   * 业务申请编号
   * 1、服务商自定义的唯一编号。
   * 2、每个编号对应一个申请单，每个申请单审核通过后会生成一个微信支付商户号。
   * 3、若申请单被驳回，可填写相同的“业务申请编号”，即可覆盖修改原申请单信息。
   */
  @SerializedName("business_code")
  private String businessCode;

  @SerializedName("contact_info")
  private ContactInfo contactInfo;

  @SerializedName("subject_info")
  private SubjectInfo subjectInfo;

  @SerializedName("business_info")
  private BusinessInfo businessInfo;

  @SerializedName("settlement_info")
  private SettlementInfo settlementInfo;

  @SerializedName("bank_account_info")
  private BankAccountInfo bankAccountInfo;


  /**
   * 超级管理员信息
   * 超级管理员需在开户后进行签约，并接收日常重要管理信息和进行资金操作，请确定其为商户法定代表人或负责人。
   */
  @Data
  @Builder
  public static class ContactInfo {

    /**
     * 超级管理员姓名
     * 该字段需进行加密处理
     */
    @SerializedName("contact_name")
    private String contactName;

    /**
     * 超级管理员身份证件号码
     * 1、“超级管理员身份证号码”与“超级管理员微信openid”，二选一必填。
     * 2、超级管理员签约时，校验微信号绑定的银行卡实名信息，是否与该证件号码一致。
     * 3、可传身份证、来往内地通行证、来往大陆通行证、护照等证件号码。
     * 4、该字段需进行加密处理
     */
    @SerializedName("contact_id_number")
    private String contactIdNumber;

    /**
     * 超级管理员微信openid
     * 1、“超级管理员身份证件号码”与“超级管理员微信openid”，二选一必填。
     * 2、超级管理员签约时，校验微信号是否与该微信openid一致。
     */
    @SerializedName("openid")
    private String openid;

    /**
     * 联系手机
     * 1、11位数字。
     * 2、用于接收微信支付的重要管理信息及日常操作验证码。
     * 3、该字段需进行加密处理
     */
    @SerializedName("mobile_phone")
    private String mobilePhone;

    /**
     * 联系邮箱
     * 1、用于接收微信支付的开户邮件及日常业务通知。
     * 2、需要带@，遵循邮箱格式校验，该字段需进行加密处理
     */
    @SerializedName("contact_email")
    private String contactEmail;

  }

  /**
   * 主体资料
   * 请填写商家的营业执照/登记证书、经营者/法人的证件等信息。
   */
  @Data
  @Builder
  public static class SubjectInfo {

    /**
     * 主体类型
     * 主体类型包括：个体户/企业/党政、机关及事业单位/其他组织
     * SUBJECT_TYPE_INDIVIDUAL（个体户）：营业执照上的主体类型一般为个体户、个体工商户、个体经营。
     * SUBJECT_TYPE_ENTERPRISE（企业）：营业执照上的主体类型一般为有限公司、有限责任公司。
     * SUBJECT_TYPE_INSTITUTIONS（党政、机关及事业单位）：包括国内各级、各类政府机构、事业单位等（如：公安、党团、司法、交通、旅游、工商税务、市政、医疗、教育、学校等机构）。
     * SUBJECT_TYPE_OTHERS（其他组织）：不属于企业、政府/事业单位的组织机构（如社会团体、民办非企业、基金会），要求机构已办理组织机构代码证。
     */
    @SerializedName("subject_type")
    private String subjectType;

    @SerializedName("business_license_info")
    private BusinessLicenseInfo businessLicenseInfo;

    @SerializedName("identity_info")
    private IdentityInfo identityInfo;


    /**
     * 营业执照
     * 1、主体为个体户/企业，必填。
     * 2、请上传“营业执照”，需年检章齐全，当年注册除外。
     */
    @Data
    @Builder
    public static class BusinessLicenseInfo {

      /**
       * 营业执照照片
       * 可上传1张图片，请填写通过图片上传接口预先上传图片生成好的MediaID。
       */
      @SerializedName("license_copy")
      private String licenseCopy;

      /**
       * 注册号/统一社会信用代码
       * 请填写营业执照上的营业执照注册号，注册号格式须为15位数字或18位数字|大写字母。
       */
      @SerializedName("license_number")
      private String licenseNumber;

      /**
       * 商户名称
       * 1、请填写营业执照上的商户名称，2~110个字符，支持括号。
       * 2、个体户，不能以“公司”结尾。
       * 3、个体户，若营业执照上商户名称为“空“或“无”时，填写"个体户+经营者姓名"，如“个体户张三”。
       */
      @SerializedName("merchant_name")
      private String merchantName;

      /**
       * 个体户经营者/法人姓名
       * 请填写营业执照的经营者/法定代表人姓名。
       */
      @SerializedName("legal_person")
      private String legalPerson;

    }

    /**
     * 经营者/法人身份证件
     * 1、个体户：请上传经营者的身份证件。
     * 2、企业/党政、机关及事业单位/其他组织：请上传法人的身份证件。
     */
    @Data
    @Builder
    public static class IdentityInfo {

      /**
       * 证件类型
       */
      @SerializedName("id_doc_type")
      private String idDocType;

      /**
       * 身份证信息
       */
      @SerializedName("id_card_info")
      private IdCardInfo idCardInfo;

      /**
       * 经营者/法人是否为受益人
       * 1、若经营者/法人是最终受益人，则填写：true。
       * 2、若经营者/法人不是最终受益人，则填写：false。
       */
      @SerializedName("owner")
      private Boolean owner;

      @Data
      @Builder
      public static class IdCardInfo {

        /**
         * 身份证人像面照片
         * 1、请上传个体户经营者/法人的身份证人像面照片。
         * 2、可上传1张图片，请填写通过图片上传接口预先上传图片生成好的MediaID。
         */
        @SerializedName("id_card_copy")
        private String idCardCopy;

        /**
         * 身份证国徽面照片
         * 1、请上传个体户经营者/法定代表人的身份证国徽面照片。
         * 2、可上传1张图片，请填写通过图片上传接口预先上传图片生成好的MediaID。
         */
        @SerializedName("id_card_national")
        private String idCardNational;

        /**
         * 身份证姓名
         * 1、请填写个体户经营者/法定代表人对应身份证的姓名，2~30个中文字符、英文字符、符号。
         * 2、该字段需进行加密处理
         */
        @SerializedName("id_card_name")
        private String idCardName;

        /**
         * 身份证号码
         * 1、请填写个体户经营者/法定代表人对应身份证的号码。
         * 2、15位数字或17位数字+1位数字|X，该字段需进行加密处理
         */
        @SerializedName("id_card_number")
        private String idCardNumber;

        /**
         * 身份证有效期开始时间
         * 1、必填，请按照示例值填写。
         * 2、结束时间大于开始时间。
         */
        @SerializedName("card_period_begin")
        private String cardPeriodBegin;

        /**
         * 身份证有效期结束时间
         * 1、必填，请按照示例值填写。
         * 2、若证件有效期为长期，请填写：长期。
         * 3、结束时间大于开始时间。
         * 4、证件有效期需大于60天。
         */
        @SerializedName("card_period_end")
        private String cardPeriodEnd;

      }

    }
  }

  /**
   * 经营资料
   * 请填写商家的经营业务信息、售卖商品/提供服务场景信息。
   */
  @Data
  @Builder
  public static class BusinessInfo {

    /**
     * 商户简称
     * 在支付完成页向买家展示，需与微信经营类目相关。
     */
    @SerializedName("merchant_shortname")
    private String merchantShortname;

    /**
     * 客服电话
     * 在交易记录中向买家展示，请确保电话畅通以便平台回拨确认。
     */
    @SerializedName("service_phone")
    private String servicePhone;

    /**
     * 经营场景
     */
    @Data
    @Builder
    public static class SalesInfo {

      /**
       * 经营场景类型
       * 1、请勾选实际售卖商品/提供服务场景（至少一项），以便为你开通需要的支付权限。
       * 2、建议只勾选目前必须的场景，以便尽快通过入驻审核，其他支付权限可在入驻后再根据实际需要发起申请。
       * 枚举值：
       *
       * 线下门店：SALES_SCENES_STORE
       * 公众号：SALES_SCENES_MP
       * 小程序：SALES_SCENES_MINI_PROGRAM
       * 互联网：SALES_SCENES_WEB
       * APP：SALES_SCENES_APP
       * 企业微信：SALES_SCENES_WEWORK
       */
      @SerializedName("sales_scenes_type")
      private String salesScenesType;

      @SerializedName("mini_program_info")
      private MiniProgramInfo miniProgramInfo;

      @Data
      @Builder
      public static class MiniProgramInfo {

        /**
         * 服务商小程序APPID
         * 1、服务商小程序APPID与商家小程序APPID，二选一必填。
         * 2、可填写当前服务商商户号已绑定的小程序APPID。
         */
        @SerializedName("mini_program_appid")
        private String miniProgramAppid;

        /**
         * 商家小程序APPID
         * 1、服务商小程序APPID与商家小程序APPID，二选一必填。
         * 2、可填写与商家主体一致且已认证的小程序APPID，需是已认证的小程序。
         * 3、审核通过后，系统将发起特约商家商户号与该AppID的绑定（即配置为sub_appid），服务商随后可在发起支付时选择传入该appid，以完成支付，并获取sub_openid用于数据统计，营销等业务场景。
         */
        @SerializedName("mini_program_sub_appid")
        private String miniProgramSubAppid;

        /**
         * 小程序截图
         * 1、请提供展示商品/服务的页面截图/设计稿（最多5张）。
         * 2、请填写通过图片上传接口预先上传图片生成好的MediaID。
         * 首次提交未成功进件，二次提交时必填
         */
        @SerializedName("mini_program_pics")
        private String miniProgramPics;

      }
    }

  }

  /**
   * 结算规则
   * 请填写商家的结算费率规则、特殊资质等信息。
   */
  @Data
  @Builder
  public static class SettlementInfo {

    /**
     * 入驻结算规则ID
     */
    @SerializedName("settlement_id")
    private String settlementId;

    /**
     * 所属行业
     */
    @SerializedName("qualification_type")
    private String qualificationType;

    /**
     * 特殊资质图片
     */
    @SerializedName("qualifications")
    private String qualifications;

    /**
     * 优惠费率活动ID
     */
    @SerializedName("activities_id")
    private String activitiesId;

    /**
     * 优惠费率活动值
     * 根据优惠费率活动规则，由服务商自定义填写，支持两个小数点，需在优惠费率活动ID指定费率范围内，如0.6%（接口无需传%，只需传数字）。
     */
    @SerializedName("activities_rate")
    private String activitiesRate;

    /**
     * 优惠费率活动补充材料
     * 根据优惠费率活动规则，由服务商自定义填写，支持两个小数点，需在优惠费率活动ID指定费率范围内，如0.6%（接口无需传%，只需传数字）。
     */
    @SerializedName("activities_additions")
    private String activitiesAdditions;
  }

  /**
   * 结算银行账户
   * 1、请填写商家提现收款的银行账户信息。
   * 2、若结算规则id为“719、721、716、717、730、739、727、738、726”，可选填结算账户。
   */
  @Data
  @Builder
  public static class BankAccountInfo {

    /**
     * 账户类型
     * 1、若主体为企业/党政、机关及事业单位/其他组织，可填写：对公银行账户。
     * 2、若主体为个体户，可选择填写：对公银行账户或经营者个人银行卡。
     * 枚举值：
     *
     * BANK_ACCOUNT_TYPE_CORPORATE：对公银行账户
     * BANK_ACCOUNT_TYPE_PERSONAL：经营者个人银行卡
     */
    @SerializedName("bank_account_type")
    private String bankAccountType;

    /**
     * 开户名称
     * 1、选择“经营者个人银行卡”时，开户名称必须与“经营者证件姓名”一致。
     * 2、选择“对公银行账户”时，开户名称必须与营业执照上的“商户名称”一致。
     * 3、该字段需进行加密处理
     */
    @SerializedName("account_name")
    private String accountName;

    /**
     * 开户银行
     */
    @SerializedName("account_bank")
    private String accountBank;

    /**
     * 开户银行省市编码
     * 至少精确到市
     */
    @SerializedName("bank_address_code")
    private String bankAddressCode;

    /**
     * 开户银行联行号
     * 1、17家直连银行无需填写，如为其他银行，则开户银行全称（含支行）和开户银行联行号二选一。
     */
    @SerializedName("bank_branch_id")
    private String bankBranchId;

    /**
     * 开户银行全称（含支行)
     * 1、17家直连银行无需填写，如为其他银行，则开户银行全称（含支行）和 开户银行联行号二选一。
     * 2、需填写银行全称，如"深圳农村商业银行XXX支行"
     */
    @SerializedName("bank_name")
    private String bankName;

    /**
     * 银行账号
     * 1、数字，长度遵循系统支持的卡号长度要求表。
     * 2、该字段需进行加密处理
     */
    @SerializedName("account_number")
    private String accountNumber;
  }

  /**
   * 加密敏感信息
   */
  public void encryptData(X509Certificate certificate) throws WxPayException {
    try {
      this.contactInfo.contactName = V3Utils.rsaEncryptOAEP(this.contactInfo.contactName, certificate);
      this.contactInfo.contactIdNumber = V3Utils.rsaEncryptOAEP(this.contactInfo.contactIdNumber, certificate);
      this.contactInfo.mobilePhone = V3Utils.rsaEncryptOAEP(this.contactInfo.mobilePhone, certificate);
      this.contactInfo.contactEmail = V3Utils.rsaEncryptOAEP(this.contactInfo.contactEmail, certificate);
      this.subjectInfo.identityInfo.idCardInfo.idCardName = V3Utils.rsaEncryptOAEP(this.subjectInfo.identityInfo.idCardInfo.idCardName, certificate);
      this.subjectInfo.identityInfo.idCardInfo.idCardNumber = V3Utils.rsaEncryptOAEP(this.subjectInfo.identityInfo.idCardInfo.idCardNumber, certificate);
      this.bankAccountInfo.accountName = V3Utils.rsaEncryptOAEP(this.bankAccountInfo.accountName, certificate);
      this.bankAccountInfo.accountNumber = V3Utils.rsaEncryptOAEP(this.bankAccountInfo.accountNumber, certificate);
    } catch (Exception e) {
      throw new WxPayException("信息加密失败");
    }
  }

}
