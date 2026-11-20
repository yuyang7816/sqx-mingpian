package com.sqx.modules.sourceGroup.controller;

import com.sqx.common.utils.Result;
import com.sqx.modules.app.annotation.Login;
import com.sqx.modules.sourceGroup.entity.SourceGroup;
import com.sqx.modules.sourceGroup.service.SourceGroupService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 群组管理-用户端
 * @author wuChang
 * @since 2024-02-27
 */
@RestController
@Api(value = "群组管理-用户端", tags = {"群组管理-用户端"})
@RequestMapping("/app/sourceGroup/")
public class AppSourceGroupController {
    @Autowired
    private SourceGroupService sourceGroupService;
   /**
     * 获取群组管理列表
     * @param sourceGroup 群组管理
     */
    @ApiOperation("获取群组列表")
    @GetMapping(value = "getSourceGroupList")
    public Result getSourceGroupList(Integer page, Integer limit,Long loginUserId, SourceGroup sourceGroup) {
        sourceGroup.setIsEnable(1);
        if (StringUtils.isNotBlank(sourceGroup.getGroupName())  && loginUserId!=null) {
        }
        return Result.success().put("data", sourceGroupService.getSourceGroupList(page, limit, sourceGroup));
    }

    /**
     * 获取我已加入的群列表
     */
    @Login
    @ApiOperation("获取我已加入的群列表")
    @GetMapping(value = "getMySourceGroupList")
    public Result getMySourceGroupList(Integer page, Integer limit,@RequestAttribute Long userId,String groupName) {
        return Result.success().put("data", sourceGroupService.getMySourceGroupList(page, limit, userId,groupName));
    }

    /**
     * 点数购买加入方式
     * @param userId
     * @param groupId 群组id
     * @return
     */

}