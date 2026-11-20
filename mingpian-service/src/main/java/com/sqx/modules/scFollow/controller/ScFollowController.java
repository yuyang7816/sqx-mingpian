package com.sqx.modules.scFollow.controller;

import com.sqx.common.utils.Result;
import com.sqx.modules.scFollow.entity.ScFollow;
import com.sqx.modules.scFollow.service.ScFollowService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 * 收藏管理-管理端
 * @author wuChang
 * @since 2024-02-28
 */
@RestController
@RequestMapping("/admin/scFollow")
@Api(value = "收藏管理-管理端", tags = {"收藏管理-管理端"})
public class ScFollowController {
    @Autowired
    private ScFollowService scFollowService;





    @ApiOperation("收藏|取消收藏")
    @PostMapping("updateFollow")
    public Result updateFollow(Long userId, ScFollow follow) {
        follow.setUserId(userId);
        return scFollowService.updateFollow(follow);
    }
}
