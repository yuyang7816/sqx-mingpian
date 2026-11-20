package com.sqx.modules.sourceGroupRole.controller;

import com.sqx.common.utils.Result;
import com.sqx.modules.app.annotation.Login;
import com.sqx.modules.sourceGroupRole.entity.SourceGroupRole;
import com.sqx.modules.sourceGroupRole.service.SourceGroupRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 群组用户关联管理-用户端
 * @author wuChang
 * @since 2024-02-27
 */
@RestController
@Api(value = "群组用户关联管理-用户端", tags = {"群组用户关联管理-用户端"})
@RequestMapping("/app/sourceGroupRole/")
public class AppSourceGroupRoleController {
    @Autowired
    private SourceGroupRoleService sourceGroupRoleService;



}