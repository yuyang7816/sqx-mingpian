package com.sqx.modules.pay.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 充值记录
 *
 * @author fang 2020-05-14
 */
@Data
@TableName("pay_details")
public class PayDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 充值记录id
     */
    @TableId(type = IdType.INPUT)
    private Long id;

    /**
     * 分类（1微信 1app微信 2微信公众号 3微信小程序 4支付宝）
     */
    private Integer classify;

    /**
     * 订单id
     */
    private String orderId;

    /**
     * 订单号
     */
    private String tradeNo;

    /**
     * 充值金额
     */
    private Double money;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 0待支付 1支付成功 2失败
     */
    private Integer state;
    /**
     * 关联id(如果是购买会员则是会员类型)
     */
    private Long parentId;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 支付时间
     */
    private String payTime;

    /**
     * 购买类型 1充值点数 2购买会员
     */
    private Integer type;

    private String remark;

    /**
     * 合作商邀请码(开通会员才用)
     */
    @ApiModelProperty("合作商邀请码(开通会员才用)")
    private String staffInviterCode;


}
