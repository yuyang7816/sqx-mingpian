package com.sqx.modules.industryNewsLike.entity;

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
 *
 * 资讯点赞
 *
 * @author wuChang
 * @since 2023-09-27
 */
@Data
@TableName("industry_news_like")
public class IndustryNewsLike implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty("id")
    @TableId(value = "like_id", type = IdType.AUTO)
    private Long likeId;

    /**
     * 资讯id
     */
    @ApiModelProperty("资讯id")
    private Long newsId;

    /**
     * 用户id
     */
    @ApiModelProperty("用户id")
    private Long userId;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 点赞用户昵称
     */
    @TableField(exist = false)
    private String userName;
    /**
     * 点赞用户头像
     */
    @TableField(exist = false)
    private String avatar;
}
