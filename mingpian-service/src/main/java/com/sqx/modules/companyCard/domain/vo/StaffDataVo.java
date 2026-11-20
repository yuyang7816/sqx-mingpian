package com.sqx.modules.companyCard.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author chengming
 * @since 2025/12/8
 */
@Data
public class StaffDataVo {

    @ApiModelProperty(value = "总员工数")
    private Long totalNum;
}
