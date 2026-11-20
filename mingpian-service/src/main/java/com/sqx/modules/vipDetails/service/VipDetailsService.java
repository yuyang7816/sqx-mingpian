package com.sqx.modules.vipDetails.service;

import com.alibaba.fastjson.JSONObject;
import com.sqx.modules.vipDetails.entity.VipDetails;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.Result;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.math.BigDecimal;

/**
 * 会员配置 服务类
 * @author wuChang
 * @since 2024-02-26
 */

public interface VipDetailsService extends IService<VipDetails> {
    /**
     * 新增或修改会员配置
     */
    Result saveVipDetails (VipDetails vipDetails);

    /**
     * 获取改会员配置列表
     */
    Result getVipDetailsList(JSONObject params);

    Result buyVipCallBack(Long userId, BigDecimal money, Integer classify, Long parentId, String staffInviterCode);

    Result giveUserVip(Long userId, Long vipId, Integer count);

    Result cancelUserVip(Long userId);
}
