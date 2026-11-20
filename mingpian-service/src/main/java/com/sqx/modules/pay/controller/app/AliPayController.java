package com.sqx.modules.pay.controller.app;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.sqx.common.utils.DateUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.annotation.Login;
import com.sqx.modules.common.service.CommonInfoService;
import com.sqx.modules.pay.config.AliPayConstants;
import com.sqx.modules.pay.dao.PayDetailsDao;
import com.sqx.modules.pay.entity.PayDetails;
import com.sqx.modules.pay.service.AliPayService;
import com.sqx.modules.vipDetails.service.VipDetailsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 支付宝支付处理--暂不做同步处理、回调方式使用异步
 */
@Slf4j
@RestController
@Api(value = "支付宝支付", tags = {"支付宝支付"})
@RequestMapping("/app/aliPay")
public class AliPayController {
    @Autowired
    private AliPayService aliPayService;
    @Autowired
    private CommonInfoService commonInfoService;
    @Autowired
    private PayDetailsDao payDetailsDao;
    @Autowired
    private VipDetailsService vipDetailsService;


    @Login
    @ApiOperation("购买会员")
    @RequestMapping(value = "/payVip", method = RequestMethod.POST)
    @Transactional(rollbackFor = Exception.class)
    public Result payVip(@RequestAttribute("userId") Long userId, Long id, Integer classify,String staffInvitationCode) {
        return aliPayService.payVip(userId, id, classify, 2,staffInvitationCode);
    }


    @RequestMapping(value = "/notifyApp", method = RequestMethod.POST)
    @Transactional(rollbackFor = Exception.class)
    public void notifyApp(HttpServletRequest request, HttpServletResponse response) {
        //获取支付宝POST过来反馈信息
        Map<String, String> params = new HashMap<String, String>();
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用。
            //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }
        try {
            log.info("回调成功！！！");
            boolean flag = AlipaySignature.rsaCheckV1(params, commonInfoService.findOne(64).getValue(), AliPayConstants.CHARSET, "RSA2");
            log.info(flag + "回调验证信息");
            if (flag) {
                String tradeStatus = params.get("trade_status");
                if ("TRADE_SUCCESS".equals(tradeStatus) || "TRADE_FINISHED".equals(tradeStatus)) {
                    //支付宝返回的订单编号
                    String outTradeNo = params.get("out_trade_no");
                    log.error(outTradeNo);
                    //支付宝支付单号
                    String tradeNo = params.get("trade_no");
                    PayDetails payDetails = payDetailsDao.selectByOrderId(outTradeNo);
                    if (payDetails != null && payDetails.getState() == 0) {
                        payDetails.setState(1);
                        payDetails.setPayTime(DateUtils.format(new Date()));
                        payDetails.setTradeNo(tradeNo);
                        payDetailsDao.updateById(payDetails);

                        if (payDetails.getType() == 1) {
                            //充值点数
                        } else if (payDetails.getType() == 2) {
                            //购买会员
                            vipDetailsService.buyVipCallBack(payDetails.getUserId(), BigDecimal.valueOf(payDetails.getMoney()), payDetails.getClassify(), payDetails.getParentId(),payDetails.getStaffInviterCode());
                        }
                    }

                }
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
            log.info("回调验证失败！！！");
        }
    }

}
