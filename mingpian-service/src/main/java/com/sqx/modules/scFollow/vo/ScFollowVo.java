package com.sqx.modules.scFollow.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.SqlCondition;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class ScFollowVo implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 需求id
     */
    @ApiModelProperty("需求id")
    private Long demandId;
    /**
     * 需求标题
     */
    @ApiModelProperty("需求标题")
    private String title;

    /**
     * 距离
     */
    @ApiModelProperty("距离")
    private double distance;

    /**
     * 采购方联系电话
     */
    @ApiModelProperty("采购方联系电话")
    private String procurePhone;

    /**
     * 采购方联系人
     */
    @ApiModelProperty("采购方联系人")
    private String procureRealName;
    /**
     * 产品照片
     */
    @ApiModelProperty("产品照片")
    private String procureImg;

    /**
     * 浏览次数
     */
    @ApiModelProperty("浏览次数")
    private Integer browseCount;

    /**
     * 查看所需点数
     */
    @ApiModelProperty("查看所需点数")
    private Integer countMoney;
}
