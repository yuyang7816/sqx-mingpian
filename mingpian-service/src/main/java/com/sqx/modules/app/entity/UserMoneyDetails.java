package com.sqx.modules.app.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("user_money_details")
@ApiModel("钱包详情")
public class UserMoneyDetails implements Serializable {
    /**
     * 钱包详情id
     */
    @ApiModelProperty("钱包详情id")
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 用户id
     */
    @TableField("user_id")
    @ApiModelProperty("用户id")
    private Long userId;
    /**
     * 对应用户id
     */
    @TableField("by_user_id")
    @ApiModelProperty("对应用户id")
    private Long byUserId;
    /**
     * 标题
     */
    @ApiModelProperty("标题")
    private String title;
    /**
     * 用途 0充值余额 1购买vip 3下级分成 4退款
     */
    @ApiModelProperty("用途 1购买vip 2充值点数 3下级分成")
    private Integer classify;
    /**
     * 钱包类型 1积分 2钱包
     */
    @ApiModelProperty("钱包类型 1积分 2钱包")
    private Integer moneyType;
   /**
     * 类型 1增加 2减少
     */
    @ApiModelProperty("类型 1增加 2减少")
    private Integer type;
    /**
     * 状态 1待支付 2已到账 3取消
     */
    @ApiModelProperty("状态 1待支付 2已到账 3取消")
    private Integer state;
    /**
     * 金额
     */
    @ApiModelProperty("金额")
    private BigDecimal money;
    /**
     * 内容
     */
    @ApiModelProperty("内容")
    private String content;
    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;


    /**
     * 关联id
     */
    private Long relationId;
    /**
     * 下级等级(1:一级用户 2:二级用户)
     */
    private Integer userLevel;

}
