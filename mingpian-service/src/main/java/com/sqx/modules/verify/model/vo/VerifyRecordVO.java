package com.sqx.modules.verify.model.vo;

import com.sqx.modules.verify.db.entity.VerifyRecord;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author chengming
 * @since 2026/3/11
 */
@Data
public class VerifyRecordVO extends VerifyRecord {

    @ApiModelProperty("用户名")
    private String userName;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("头像")
    private String avatar;
}
