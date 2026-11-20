package com.sqx.modules.invite.model;

import com.sqx.modules.invite.entity.Invite;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author chengming
 * @since 2026/1/20
 */
@Data
public class InvitePageVo extends Invite {

    @ApiModelProperty("被邀请人ID")
    private String userName;

    @ApiModelProperty("头像")
    private String avatar;

    @ApiModelProperty("金额")
    private BigDecimal amount;
}
