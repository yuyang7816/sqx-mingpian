package com.sqx.modules.member.entity;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.annotation.IdType;
import io.swagger.annotations.ApiModelProperty;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *
 * 会员特权图标
 *
 * @author wuChang
 * @since 2024-03-27
 */
@Data
public class Member implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 会员特权id
     */
    @ApiModelProperty("会员特权id")
    @TableId(value = "member_id", type = IdType.AUTO)
    private Long memberId;

    /**
     * 特权图标
     */
    @ApiModelProperty("特权图标")
    private String memberImg;

    /**
     * 特权名称
     */
    @ApiModelProperty("特权名称")
    private String memberName;
    /**
     * 描述
     */
    @ApiModelProperty("描述")
    private String memberDescribe;

    /**
     * 排序
     */
    @ApiModelProperty("排序")
    private String sort;

    /**
     * 会员id
     */
    @ApiModelProperty("会员id")
    private Long vipId;

}
