package com.sqx.modules.vipDetails.entity;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.annotation.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *
 * 会员配置
 *
 * @author wuChang
 * @since 2024-02-26
 */
@Data
@TableName("vip_details")
public class VipDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 月数
     */
    @ApiModelProperty("月数")
    private Integer vipMonthCount;

    /**
     * 会员价格
     */
    @ApiModelProperty("会员价格")
    private BigDecimal money;

    /**
     * 会员名称
     */
    @ApiModelProperty("会员名称")
    private String vipName;

    /**
     * 更新时间
     */
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    /**
     * 是否启用(0未启用 1启用)
     */
    @ApiModelProperty("是否启用(0未启用 1启用)")
    private Integer isEnable;

    /**
     * 排序
     */
    @ApiModelProperty("排序")
    private Integer sort;

}
