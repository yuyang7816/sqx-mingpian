package com.sqx.modules.businessCard.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * 名片管理
 *
 * @author wuChang
 * @since 2024-02-26
 */
@Data
@TableName("business_card")
public class BusinessCard implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 名片id
     */
    @ApiModelProperty("名片id")
    @TableId(value = "card_id", type = IdType.AUTO)
    private Long cardId;
    /*
    * 父id 用于个人名片关联公司名片
    * */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private Long parentId;

    /**
     * 用户id
     */
    @ApiModelProperty("用户id")
    private Long userId;

    /**
     * 名片类型 1个人认证 2企业认证
     */
    @ApiModelProperty("名片类型 名片类型 1=个人名片 2企业名片")
    private Integer cardType;
    /**
     * 名片状态 名片状态 0不展示 1展示
     */
    @ApiModelProperty("名片状态  0不展示 1展示 ")
    private Integer cardStatus;
    /**
     * 审核状态 0待审核 1审核成功 2审核未通过 3:会员到期已下架
     */
    @ApiModelProperty("审核状态 0待审核 1审核成功 2审核未通过 3会员到期已下架")
    private Integer auditStatus;
    /**
     * 审核备注
     */
    @ApiModelProperty("审核备注")
    private String examineRemark;
    /**
     * 用户头像
     */
    @ApiModelProperty("用户头像")
    private String cardAvatar;
    /**
     * 联系电话
     */
    @ApiModelProperty("联系电话")
    private String cardPhone;
    /**
     * 真实姓名
     */
    @ApiModelProperty("真实姓名")
    private String realName;
    /**
     * 公司名称
     */
    @ApiModelProperty("公司名称")
    private String companyName;

    /**
     * 所属岗位(字典)
     */
    @ApiModelProperty("所属岗位(字典)")
    private String companyPosition;

    /**
     * 公司省
     */
    @ApiModelProperty("公司省")
    private String companyProvince;

    /**
     * 公司市
     */
    @ApiModelProperty("公司市")
    private String companyCity;

    /**
     * 公司区
     */
    @ApiModelProperty("公司区")
    private String companyDistrict;
    /**
     * 主营产品图
     */
    @ApiModelProperty("主营产品图")
    private String firstProductImg;

    /**
     * 公司详细地址
     */
    @ApiModelProperty("公司详细地址")
    private String companyAddress;

    /**
     * 面积(单位:平方米)
     */
    @ApiModelProperty("面积(单位:平方米)")
    private Double companyArea;
    /**
     * 公司LOGO
     */
    @ApiModelProperty("公司LOGO")
    private String companyLogo;
    /**
     * 公司环境
     */
    @ApiModelProperty("公司环境")
    private String companyAmbient;

    /**
     * 年产值(单位:万元)
     */
    @ApiModelProperty("年产值(单位:万元)")
    private BigDecimal companyOutputValue;

    /**
     * 公司官网
     */
    @ApiModelProperty("公司官网")
    private String companyWebsite;

    /**
     * 资质认证图片
     */
//    @TableField(updateStrategy = FieldStrategy.IGNORED)
//    @ApiModelProperty("资质认证图片")
//    private String companyFlairImg;
//
//    /**
//     * 资质认证名称
//     */
//    @TableField(updateStrategy = FieldStrategy.IGNORED)
//    @ApiModelProperty("资质认证名称")
//    private String companyFlair;

    @TableField(exist = false)
    @ApiModelProperty("资质认证列表")
    List<CompanyFlair> companyFlairs;

    @ApiModelProperty("资质认证")
    private String companyFlair;

    /**
     * 公司简介
     */
    @ApiModelProperty("公司简介")
    private String companyBlurb;

    /**
     * 主营产品
     */
    @ApiModelProperty("主营产品")
    private String firstProduct;
    /**
     * 浏览次数
     */
    @ApiModelProperty("浏览次数")
    private Integer browseCount;
    /**
     * 行业一级id
     */
    @ApiModelProperty("行业一级id")
    private Long industryFirstId;
    /**
     * 行业二级id
     */
    @ApiModelProperty("行业二级id")
    private Long industrySecondId;
    /**
     * 行业一级名称
     */
    @ApiModelProperty("行业一级名称")
    private String industryFirstName;
    /**
     * 行业二级名称
     */
    @ApiModelProperty("行业二级名称")
    private String industrySecondName;

    @ApiModelProperty("营业执照")
    private String companyLicense;

    @ApiModelProperty("公司人数")
    private Integer companyNum;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;


    /**
     * 搜索关键词
     */
    @TableField(exist = false)
    @ApiModelProperty("搜索关键词")
    private String keyWord;
    /**
     * 用户名
     */
    @TableField(exist = false)
    @ApiModelProperty("用户名")
    private String userName;

    @ApiModelProperty("样式code")
    private String cardStyleCode;

    @ApiModelProperty("名片照片")
    private String cardImg;
    @ApiModelProperty("分享次数")
    private Integer shareCount;
    @ApiModelProperty("人气值")
    private Integer popularityValue;

    @ApiModelProperty("法人")
    private String legalPerson;
    @ApiModelProperty("注册资金")
    private String registeredCapital;
    @ApiModelProperty("注册时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime registeredTime;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("地址")
    private String address;

    @ApiModelProperty("工作经历")
    private String workExperience;

    @ApiModelProperty("教育经历")
    private String eduExperience;

    @ApiModelProperty("名片背面")
    private String cardImgBack;

    @ApiModelProperty("额度")
    private Integer quota;

    @ApiModelProperty("已使用额度")
    private Integer usedQuota;

    @ApiModelProperty("微信号")
    private String wxCode;

    @ApiModelProperty("个人简介（图片）")
    private String detailsImage;

    @ApiModelProperty("个人简介（文字）")
    private String detailsText;

    @ApiModelProperty("个人简介（视频）")
    private String detailsVideo;

    //邀请码
    @TableField(exist = false)
    private String invitationCode;

    //邀请加入企业的企业名片id
    @TableField(exist = false)
    private Long invitationCardId;

    @TableField(exist = false)
    private Boolean isAdmin;

    @Data
    public static class CompanyFlair {

        @ApiModelProperty("资质认证名称")
        private String name;

        @ApiModelProperty("资质认证图片")
        private String pic;
    }

    @ApiModelProperty("核查记录 0=未核查 1=已核查")
    @TableField(exist = false)
    private Integer verifyFlag;

    @TableField(exist = false)
    private Integer userCardListCount;

    @TableField(exist = false)
    private Integer messageStatus;

    @TableField(exist = false)
    private Integer companyStaffCount;

}
