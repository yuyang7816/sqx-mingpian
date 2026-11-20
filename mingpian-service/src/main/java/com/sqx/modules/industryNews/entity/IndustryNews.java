package com.sqx.modules.industryNews.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.annotation.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.sqx.modules.industryNewsComment.entity.IndustryNewsComment;
import io.swagger.annotations.ApiModelProperty;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDateTime;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *
 * 行业资讯
 *
 * @author wuChang
 * @since 2024-02-26
 */
@Data
@TableName("industry_news")
public class IndustryNews implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 新闻id
     */
    @ApiModelProperty("新闻id")
    @TableId(value = "news_id", type = IdType.AUTO)
    private Long newsId;

    /**
     * 发布类型 1用户发布 2平台发布
     */
    @ApiModelProperty("发布类型 1用户发布 2平台发布")
    private Integer releaseType;
    /**
     * 上下架 0下架 1上架
     */
    @ApiModelProperty("上下架 0下架 1上架")
    private Integer isEnable;

    /**
     * 用户id
     */
    @ApiModelProperty("用户id")
    private Long userId;

    /**
     * 平台用户id
     */
    @ApiModelProperty("平台用户id")
    private Long sysUserId;

    /**
     * 下架原因
     */
    @ApiModelProperty("下架原因")
    private String downRemark;
  /**
     * 资讯类型
     */
    @ApiModelProperty("资讯类型")
    private String industryType;

    /**
     * 标题
     */
    @ApiModelProperty("标题")
    private String title;

    /**
     * 正文
     */
    @ApiModelProperty("正文")
    private String content;

    /**
     * 审核备注
     */
    @ApiModelProperty("审核备注")
    private String examineRemark;

    /**
     * 状态 0待审核 1审核成功 2审核未通过 3会员到期已下架
     */
    @ApiModelProperty("状态 0待审核 1审核成功 2审核未通过 3会员到期已下架")
    private Integer status;

    /**
     * 封面图
     */
    @ApiModelProperty("封面图")
    private String titleImg;
    /**
     * 企业名称
     */
    @ApiModelProperty("企业名称")
    private String enterpriseName;
    /**
     * 平台用户名
     */
    @ApiModelProperty("平台用户名")
    private String sysUserName;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;


}
