package com.sqx.modules.industry.entity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.annotation.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDateTime;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *
 * 行业分类
 *
 * @author wuChang
 * @since 2024-02-26
 */
@Data
public class Industry implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 行业id
     */
    @ApiModelProperty("行业id")
    @TableId(value = "industry_id", type = IdType.AUTO)
    private Integer industryId;

    /**
     * 行业名称
     */
    @ApiModelProperty("行业名称")
    private String industryName;

    /**
     * 是否启用 0不启用 1启用
     */
    @ApiModelProperty("是否启用 0不启用 1启用")
    private Integer isEnable;

    /**
     * 上级id
     */
    @ApiModelProperty("上级id")
    private Integer parentId;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 排序
     */
    @ApiModelProperty("排序")
    private Integer sort;


    @TableField(exist = false)
    private List<Industry> childrenList;
}
