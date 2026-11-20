package com.sqx.modules.industryNewsComment.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 资讯评论
 *
 * @author wuChang
 * @since 2023-09-26
 */
@Data
@TableName("industry_news_comment")
public class IndustryNewsComment implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 评论id
     */
    @ApiModelProperty("评论id")
    @TableId(value = "comment_id", type = IdType.AUTO)
    private Long commentId;

    /**
     * 动态id
     */
    @ApiModelProperty("资讯id")
    private Long newsId;

    /**
     * 评论内容
     */
    @ApiModelProperty("评论内容")
    private String commentContent;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 状态 0待审核 1审核通过 2审核不通过
     */
    @ApiModelProperty("状态 0待审核 1审核通过 2审核不通过")
    private Integer status;
    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remarks;
    /**
     * 用户id
     */
    @ApiModelProperty("用户id")
    private Long userId;

    /**
     * 评论人昵称
     */
    @TableField(exist = false)
    @ApiModelProperty("评论人昵称")
    private String userName;
    /**
     * 评论人头像
     */
    @TableField(exist = false)
    @ApiModelProperty("评论人头像")
    private String avatar;

}
