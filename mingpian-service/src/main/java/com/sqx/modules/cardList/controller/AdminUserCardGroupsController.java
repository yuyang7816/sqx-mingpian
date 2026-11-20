package com.sqx.modules.cardList.controller;

import com.sqx.common.utils.Result;
import com.sqx.modules.cardList.entity.UserCardGroups;
import com.sqx.modules.cardList.service.UserCardGroupsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 用户名片分组表(UserCardGroups)表控制层
 *
 * @author makejava
 * @since 2024-05-07 17:06:30
 */
@Api(tags = {"管理端-用户名片分组表"})
@RestController
@RequestMapping("/admin/cardGroups")
public class AdminUserCardGroupsController {

    @Resource
    private UserCardGroupsService userCardGroupsService;

    @ApiOperation("查询分组信息")
    @GetMapping("/query")
    public Result queryUserGroups(Long userId, Integer isDefault){
        Result result = userCardGroupsService.queryGroups(userId, isDefault);
        return result;
    }

    @ApiOperation("增加名片夹分组")
    @PostMapping("/insert")
    public Result saveUserGroups(@RequestBody UserCardGroups userCardGroups, Long userId){
        Result groups = userCardGroupsService.createGroups(userCardGroups,userId);
        return groups;
    }

    @ApiOperation("修改名片夹分组")
    @PostMapping("/edit")
    public Result editUserGroups(@RequestBody UserCardGroups userCardGroups, Long userId){
        Result result = userCardGroupsService.editGroupInfo(userCardGroups);
        return result;
    }

    @ApiOperation("删除分组信息")
    @PostMapping("/del/{id}")
    public Result editUserGroups(@PathVariable String id){
        return userCardGroupsService.delete(id);
    }



}

