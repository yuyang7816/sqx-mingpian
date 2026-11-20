package com.sqx.modules.dataCenter.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author chengming
 * @since 2026/3/6
 */
@Data
public class CardStatisticsVO {

    @ApiModelProperty("总名片数")
    private Integer total;

    @ApiModelProperty("个人名片数")
    private Integer user;

    @ApiModelProperty("企业名片数")
    private Integer company;
}
