package com.sqx.modules.verify.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author chengming
 * @since 2026/3/11
 */
@Data
public class VerifyRecordSaveBO {

    @ApiModelProperty(value = "用户ID")
    private Long userId;

    /**
     * 公司名
     */
    @ApiModelProperty(value = "公司名")
    private String name;
}
