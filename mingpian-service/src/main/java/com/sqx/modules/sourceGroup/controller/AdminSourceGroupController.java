package com.sqx.modules.sourceGroup.controller;

import com.sqx.common.utils.Result;
import com.sqx.modules.sourceGroup.entity.SourceGroup;
import com.sqx.modules.sourceGroup.service.SourceGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 群组管理-管理端
 * @author wuChang
 * @since 2024-02-27
 */
@RestController
@Api(value = "群组管理-管理端", tags = {"群组管理-管理端"})
@RequestMapping("/admin/sourceGroup/")
public class AdminSourceGroupController {
    @Autowired
    private SourceGroupService sourceGroupService;

    /**
     * 新增或修改群组管理
     *@param sourceGroup 群组管理
     */
    @ApiOperation("新增或修改群组管理")
    @PostMapping(value = "saveSourceGroup")
    public Result saveSourceGroup(SourceGroup sourceGroup) {
        return sourceGroupService.saveSourceGroup(sourceGroup);
    }

    /**
     * 获取群组管理列表
     * @param sourceGroup 群组管理
     */
    @ApiOperation("获取群组管理列表")
    @GetMapping(value = "getSourceGroupList")
    public Result getSourceGroupList(Integer page, Integer limit, SourceGroup sourceGroup) {

        return Result.success().put("data", sourceGroupService.getSourceGroupList(page, limit, sourceGroup));
    }

    /**
     * 删除群组管理
     *@param groupId 群组管理id
     */
    @ApiOperation("删除群组管理")
    @PostMapping(value = "deleteSourceGroupById")
    public Result deleteSourceGroupById(Long groupId) {

        return sourceGroupService.deleteSourceGroupById(groupId) ;
    }


    /**
     * 获取群组管理详情
     *@param groupId 群组管理id
     */
    @ApiOperation("获取群组管理详情")
    @GetMapping(value = "getSourceGroupById")
    public Result getSourceGroupById(Long groupId) {
        return Result.success().put("data", sourceGroupService.getById(groupId));
    }

}