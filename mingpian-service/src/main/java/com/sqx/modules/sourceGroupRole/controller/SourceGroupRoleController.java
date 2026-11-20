package com.sqx.modules.sourceGroupRole.controller;

import com.sqx.common.utils.Result;
import com.sqx.modules.sourceGroupRole.entity.SourceGroupRole;
import com.sqx.modules.sourceGroupRole.service.SourceGroupRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

/**
 * 群组用户关联管理-管理端
 * @author wuChang
 * @since 2024-02-27
 */
@RestController
@Api(value = "群组用户关联管理-管理端", tags = {"群组用户关联管理-管理端"})
@RequestMapping("/admin/sourceGroupRole/")
public class SourceGroupRoleController {
    @Autowired
    private SourceGroupRoleService sourceGroupRoleService;


    /**
     * 获取群组用户关联管理列表
     * @param sourceGroupRole 群组用户关联管理
     */
    @ApiOperation("获取群组用户关联管理列表")
    @GetMapping(value = "getSourceGroupRoleList")
    public Result getSourceGroupRoleList(Integer page, Integer limit, SourceGroupRole sourceGroupRole) {

        return Result.success().put("data", sourceGroupRoleService.getSourceGroupRoleList(page, limit, sourceGroupRole));
    }

    /**
     * 删除群组用户关联管理
     *@param roleId 群组用户关联管理id
     */
    @ApiOperation("删除群组用户关联管理")
    @PostMapping(value = "deleteSourceGroupRoleById")
    public Result deleteSourceGroupRoleById(Long roleId) {

        return sourceGroupRoleService.removeById(roleId) ? Result.success() : Result.error();
    }


    /**
     * 获取群组用户关联管理详情
     *@param roleId 群组用户关联管理id
     */
    @ApiOperation("获取群组用户关联管理详情")
    @GetMapping(value = "getSourceGroupRoleById")
    public Result getSourceGroupRoleById(Long roleId) {
        return Result.success().put("data", sourceGroupRoleService.getById(roleId));
    }

}