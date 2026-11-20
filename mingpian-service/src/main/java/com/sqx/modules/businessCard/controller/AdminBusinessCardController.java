package com.sqx.modules.businessCard.controller;

import com.alibaba.fastjson.JSONObject;
import com.sqx.common.utils.Result;
import com.sqx.modules.businessCard.entity.BusinessCard;
import com.sqx.modules.businessCard.service.BusinessCardService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 名片管理-管理端
 * @author wuChang
 * @since 2024-02-26
 */
@RestController
@Api(value = "名片管理-管理端", tags = {"名片管理-管理端"})
@RequestMapping("/admin/businessCard/")
public class AdminBusinessCardController {
    @Autowired
    private BusinessCardService businessCardService;

    /**
     * 获取名片管理列表
     * @param businessCard 名片管理
     */
    @ApiOperation("获取名片管理列表")
    @PostMapping(value = "/list")
    public Result getBusinessCardList(@RequestBody JSONObject params) {
        return Result.success().put("data", businessCardService.getBusinessCardList(params));
    }


    /**
     * 新增或修改名片管理
     *@param businessCard 名片管理
     */
    @ApiOperation("新增或修改名片管理")
    @PostMapping(value = "saveBusinessCard")
    public Result saveBusinessCard(@RequestBody BusinessCard businessCard) {
        return businessCardService.saveBusinessCard(businessCard);
    }


    /**
     * 删除名片管理
     *@param cardId 名片管理id
     */
    @ApiOperation("删除名片管理")
    @GetMapping(value = "/del/{id}")
    public Result deleteBusinessCardById(@PathVariable Long id) {

        return businessCardService.removeById(id) ? Result.success() : Result.error();
    }


    /**
     * 获取名片管理详情
     *@param cardId 名片管理id
     */
    @ApiOperation("获取名片管理详情")
    @GetMapping(value = "/queryById/{id}")
    public Result getBusinessCardById(@PathVariable Long id) {
        return Result.success().put("data", businessCardService.getById(id));
    }

    @ApiOperation("审核名片")
    @PostMapping(value = "examineCard")
    public Result examineCard(@RequestBody JSONObject params) {
        return businessCardService.examineCard(params);
    }

    /**
     * 获取指定用户的名片信息
     */
    @ApiOperation("获取指定用户的名片信息")
    @GetMapping(value = "/queryByUserId/{id}")
    public Result getBusinessCard(@PathVariable Long id) {
        return Result.success().put("data", businessCardService.getBusinessCard(id));
    }

    @ApiOperation("查询名片榜单")
    @PostMapping("/hotList")
    public Result cardHotList(@RequestBody JSONObject params){
        Result result = businessCardService.cardHotList(params);
        return result;
    }

    @ApiOperation("查询我所有的名片")
    @PostMapping("/queryAllList")
    public Result queryAllList(Long userId, @RequestBody JSONObject params){
        Result result = businessCardService.queryAllList(userId, params);
        return result;
    }
}