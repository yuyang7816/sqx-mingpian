package com.sqx.modules.pay.service;

import com.sqx.common.utils.Result;
import com.sqx.modules.pay.entity.PayDetails;

public interface AliPayService {


    boolean aliRefund(PayDetails payDetails);

    Result payVip(Long userId, Long id, Integer classify, int type, String staffInvitationCode);
}
