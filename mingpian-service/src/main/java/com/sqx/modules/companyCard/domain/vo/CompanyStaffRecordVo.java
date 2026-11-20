package com.sqx.modules.companyCard.domain.vo;

import com.sqx.modules.companyCard.entity.CompanyStaffRecord;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author chengming
 * @since 2025/12/16
 */
@Data
public class CompanyStaffRecordVo extends CompanyStaffRecord {

    @ApiModelProperty(value = "员工姓名")
    private String realName;

    @ApiModelProperty(value = "员工头像")
    private String cardAvatar;

    @ApiModelProperty(value = "岗位")
    private String companyPosition;
}
