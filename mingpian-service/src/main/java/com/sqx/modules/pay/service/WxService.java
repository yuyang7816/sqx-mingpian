package com.sqx.modules.pay.service;

import com.sqx.common.utils.Result;

import java.math.BigDecimal;


/**
 * @author fang
 * @date 2020/2/26
 */
public interface WxService {



    String payBack(String resXml,Integer type);

    Result payMoney(Long userId, BigDecimal money, Long parentId, Integer classify, Integer type) throws Exception;

    boolean refund(String ordersNo, BigDecimal refundMoney);

    Result payVip(Long userId, Long id, Integer classify, Integer type, String staffInviterCode) throws Exception;


    //boolean refund(Orders orders);

}