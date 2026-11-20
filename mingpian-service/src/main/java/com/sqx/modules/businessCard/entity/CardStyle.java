package com.sqx.modules.businessCard.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.io.Serializable;

/**
 * 名片样式(CardStyle)实体类
 *
 * @author makejava
 * @since 2024-05-06 17:17:18
 */
@Data
public class CardStyle implements Serializable {
    private static final long serialVersionUID = -57219713994515950L;
/**
     * id
     */
    @TableId(type = IdType.INPUT)
    private String id;
/**
     * 样式编码
     */
    @ApiModelProperty("样式编码")
    private String styleCode;
    /**
     * 样式名称
     */
    @ApiModelProperty("样式名称")
    private String styleName;
/**
     * 样式图片
     */
    @ApiModelProperty("样式图片")
    private String styleImg;
/**
     * 序号
     */
    @ApiModelProperty("序号")
    private Integer sortId;
/**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /*是否免费 0 免费 1VIP免费*/
    private Integer isFree;

}

