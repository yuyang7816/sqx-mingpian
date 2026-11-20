package com.sqx.modules.sourceGroup.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDateTime;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *
 * 群组管理
 *
 * @author wuChang
 * @since 2024-02-27
 */
@Data
@TableName("source_group")
public class SourceGroup implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 群id
     */
    @ApiModelProperty("群id")
    @TableId(value = "group_id", type = IdType.AUTO)
    private Long groupId;

    /**
     * 平台用户id
     */
    @ApiModelProperty("平台用户id")
    private Long sysUserId;
    /**
     * 发布人昵称
     */
    @TableField(condition = SqlCondition.LIKE)
    @ApiModelProperty("发布人昵称")
    private String sysUserName;

    /**
     * 群名称
     */
    @TableField(condition = SqlCondition.LIKE)
    @ApiModelProperty("群名称")
    private String groupName;

    /**
     * 群分类 1省级群 2市级群 3全国群
     */
    @ApiModelProperty("群分类 1省级群 2市级群 3全国群")
    private Integer groupType;

    /**
     * 是否启用 0否 1是
     */
    @ApiModelProperty("是否启用 0否 1是")
    private Integer isEnable;

    /**
     * 排序
     */
    @ApiModelProperty("排序")
    private Integer sort;

    /**
     * 群人数
     */
    @ApiModelProperty("群人数")
    private Integer peopleCount;

    /**
     * 加群二维码
     */
    @ApiModelProperty("加群二维码")
    private String groupImg;
    /**
     * 群封面图
     */
    @ApiModelProperty("群封面图")
    private String groupTitleImg;

    /**
     * 加群所需积分数
     */
    @ApiModelProperty("加群所需积分数")
    private Integer countMoney;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;


}
