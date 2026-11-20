package com.sqx.modules.sourceGroupRole.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.annotation.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.sqx.modules.sourceGroup.entity.SourceGroup;
import io.swagger.annotations.ApiModelProperty;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDateTime;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *
 * 群组用户关联管理
 *
 * @author wuChang
 * @since 2024-02-27
 */
@Data
@TableName("source_group_role")
public class SourceGroupRole implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 关联id
     */
    @ApiModelProperty("关联id")
    @TableId(value = "role_id", type = IdType.AUTO)
    private Long roleId;

    /**
     * 群id
     */
    @ApiModelProperty("群id")
    private Long groupId;

    /**
     * 用户id
     */
    @ApiModelProperty("用户id")
    private Long userId;

    /**
     * 支付点数
     */
    @ApiModelProperty("支付点数")
    private Integer payCount;
    /**
     * 用户头像
     */
    @TableField(exist = false)
    @ApiModelProperty("用户头像")
    private String userName;
    /**
     * 用户头像
     */
    @TableField(exist = false)
    @ApiModelProperty("用户头像")
    private String avatar;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;


    /**
     * 群组信息
     */
    @TableField(exist = false)
    private SourceGroup sourceGroup;
}
