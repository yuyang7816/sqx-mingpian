package com.sqx.modules.scFollow.controller.app;

import com.sqx.common.utils.Result;
import com.sqx.modules.app.annotation.Login;
import com.sqx.modules.scFollow.entity.ScFollow;
import com.sqx.modules.scFollow.service.ScFollowService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
/**
 * 收藏管理-用户端
 * @author wuChang
 * @since 2024-02-28
 */
@RestController
@RequestMapping("/app/scFollow/")
@Api(value = "收藏管理-用户端", tags = {"收藏管理-用户端"})
public class AppScFollowController {
    @Autowired
    private ScFollowService scFollowService;


    @Login
    @ApiOperation("收藏列表")
    @GetMapping("getMyFollowList")
    public Result getMyFollowList(@RequestAttribute("userId") Long userId,Integer page, Integer limit,Double lng,Double lat) {

        return Result.success().put("data", scFollowService.getMyFollowList(page, limit,userId, lng,lat));
    }

    @Login
    @ApiOperation("收藏|取消收藏")
    @PostMapping("updateFollow")
    public Result updateFollow(@RequestAttribute("userId") Long userId, ScFollow follow) {
        follow.setUserId(userId);
        return scFollowService.updateFollow(follow);
    }


}
