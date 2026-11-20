package com.sqx.modules.vipDetails.controller;

import com.alibaba.fastjson.JSONObject;
import com.sqx.common.utils.Result;
import com.sqx.modules.vipDetails.entity.VipDetails;
import com.sqx.modules.vipDetails.service.VipDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;

/**
 * 会员配置-管理端
 * @author wuChang
 * @since 2024-02-26
 */
@RestController
@Api(value = "会员配置-管理端", tags = {"会员配置-管理端"})
@RequestMapping("/admin/vipDetails/")
public class VipDetailsController {
    @Autowired
    private VipDetailsService vipDetailsService;

    /**
     * 新增或修改会员配置
     *@param vipDetails 会员配置
     */
    @ApiOperation("新增或修改会员配置")
    @PostMapping(value = "/save")
    public Result saveVipDetails(@RequestBody VipDetails vipDetails) {
        return vipDetailsService.saveVipDetails(vipDetails);
    }

    /**
     * 获取会员配置列表
     * @param vipDetails 会员配置
     */
    @ApiOperation("获取会员配置列表")
    @PostMapping(value = "/list")
    public Result getVipDetailsList(@RequestBody JSONObject params) {
        Result vipDetailsList = vipDetailsService.getVipDetailsList(params);
        return Result.success().put("data", vipDetailsList);
    }

    /**
     * 删除会员配置
     *@param id 会员配置id
     */
    @ApiOperation("删除会员配置")
    @PostMapping(value = "deleteVipDetailsById")
    public Result deleteVipDetailsById(Long id) {

        return vipDetailsService.removeById(id) ? Result.success() : Result.error();
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


    @ApiOperation("赠送用户会员")
    @PostMapping(value = "giveUserVip")
    public Result giveUserVip(Long userId, Long vipId, Integer count) {
        return vipDetailsService.giveUserVip(userId, vipId, count);
    }

    @ApiOperation("取消用户会员")
    @PostMapping("cancelUserVip")
    public Result cancelUserVip(Long userId) {
        return vipDetailsService.cancelUserVip(userId);
    }

}