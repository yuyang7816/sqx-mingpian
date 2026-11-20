package com.sqx.modules.verify.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author chengming
 * @since 2026/3/11
 */
@Data
public class VerifyRecordPageBO {

    private Integer page;
    private Integer limit;

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("公司名")
    private String name;
}
