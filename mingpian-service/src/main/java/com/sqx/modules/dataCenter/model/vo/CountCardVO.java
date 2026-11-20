package com.sqx.modules.dataCenter.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author chengming
 * @since 2026/1/13
 */
@Data
public class CountCardVO {

    @ApiModelProperty("最近访客")
    private Integer visitor;

    @ApiModelProperty("最近查看")
    private Integer views;

    @ApiModelProperty("最近邀请")
    private Integer invites;
}
