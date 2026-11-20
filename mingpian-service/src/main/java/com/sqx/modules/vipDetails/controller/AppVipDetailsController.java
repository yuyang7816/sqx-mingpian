package com.sqx.modules.vipDetails.controller;

import com.alibaba.fastjson.JSONObject;
import com.sqx.common.utils.Result;
import com.sqx.modules.vipDetails.service.VipDetailsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 会员配置-用户端
 * @author wuChang
 * @since 2024-02-26
 */
@RestController
@Api(value = "会员配置-用户端", tags = {"会员配置-用户端"})
@RequestMapping("/app/vipDetails/")
public class AppVipDetailsController {
    @Autowired
    private VipDetailsService vipDetailsService;

    /**
     * 获取会员配置列表
     * @param vipDetails 会员配置
     */
    @ApiOperation("获取会员配置列表")
    @PostMapping(value = "/page")
    public Result getVipDetailsList(@RequestBody JSONObject params) {
        return vipDetailsService.getVipDetailsList(params);
    }
    /**
     * 获取会员配置详情
     *@param id 会员配置id
     */
    @ApiOperation("获取会员配置详情")
    @GetMapping(value = "getVipDetailsById")
    public Result getVipDetailsById(Long id) {
        return Result.success().put("data", vipDetailsService.getById(id));
    }

}