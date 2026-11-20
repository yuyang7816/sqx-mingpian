package com.sqx.modules.app.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;


/**
 * 用户
 */
@Data
@ApiModel("用户")
@TableName("tb_user")
public class UserEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    @TableId(value = "user_id", type = IdType.AUTO)
    private Long userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 性别 1男 2女
     */
    private Integer sex;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 微信小程序openId
     */
    private String openId;

    /**
     * 微信App  openId
     */
    private String wxOpenId;

    /**
     * 密码
     */
    private String password;
    /**
     * 加密盐
     */
    private String salt;

    /**
     * 苹果id
     */
    private String appleId;

    /**
     * 手机类型 1安卓 2ios
     */
    private Integer sysPhone;

    /**
     * 状态 1正常 2禁用
     */
    private Integer status;
    /**
     * 是否是代理 0否 1是
     */
    private Integer isAgent;
    /**
     * 是否是合伙人 0否 1是
     */
    private Integer isStaff;

    /**
     * 来源 APP 小程序  公众号
     */
    private String platform;

    /**
     * app推送id
     */
    private String clientid;

    /**
     * 支付宝名称
     */
    private String zhiFuBaoName;

    /**
     * 支付宝账号
     */
    private String zhiFuBao;

    /**
     * 是否推送消息
     */
    private Integer isSendMsg;

    /**
     * 微信提现图片
     */
    private String wxImg;

    /**
     * 最近一条信息内容
     */
    private String msg;

    /**
     * 用户类型 0未认证 1普通用户 2企业用户
     */
    private Integer userType;

    /**
     * 是否是会员(0不是 1是)
     */
    private Integer isVip;
    /**
     * vip等级 权限 1只能发布名片 2只能发布行业资讯 3两者都能发布
     */
    private Long vipId;

    /**
     * vip到期时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime vipEndTime;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    /**
     * 代理人邀请码
     */
    private String invitationCode;

    /**
     * 代理人上级邀请码
     */
    private String inviterCode;
    /**
     * 合作商邀请码
     */
    private String staffInvitationCode;

    /**
     * 合作商上级邀请码
     */
    private String staffInviterCode;

    /**
     * 收益
     */
    private BigDecimal balance;


    /**
     * 点数
     */
    private Integer countMoney;

    /**
     * 是否推广员 0=否 1=是
     */
    private Integer isPromoter;

    /**
     * 会员申请查看名片次数
     */
    private Integer vipCardNum;

    @TableField(exist = false)
    private String money;

    @TableField(exist = false)
    private Boolean isUserCertification;

    @TableField(exist = false)
    private Boolean isCompanyCertification;

    @TableField(exist = false)
    private Long cardId;

}
