package com.sqx.modules.industryNewsLike.controller;

import com.sqx.common.utils.Result;
import com.sqx.modules.app.annotation.Login;
import com.sqx.modules.industryNewsLike.entity.IndustryNewsLike;
import com.sqx.modules.industryNewsLike.service.IIndustryNewsLikeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 资讯点赞-用户端
 * @author wuChang
 * @since 2023-09-27
 */
@RestController
@Api(value = "资讯点赞-用户端", tags = {"资讯点赞-用户端"})
@RequestMapping("/app/industryNewsLike/")
public class IndustryNewsLikeController {
    @Autowired
    private IIndustryNewsLikeService iIndustryNewsLikeService;

    /**
     * 新增或修改资讯点赞
     */
    @Login
    @ApiOperation("新增或修改资讯点赞")
    @PostMapping(value = "saveIndustryNewsLike")
    public Result saveIndustryNewsLike(@RequestAttribute("userId") Long userId, IndustryNewsLike industryNewsLike) {
        industryNewsLike.setUserId(userId);
        return iIndustryNewsLikeService.saveIndustryNewsLike(industryNewsLike);
    }

    /**
     * 新增或修改资讯点赞
     */
    @Login
    @ApiOperation("获取我的点赞列表")
    @GetMapping(value = "getMyLikeIndustryNewsList")
    public Result getMyLikeIndustryNewsList(@RequestAttribute("userId") Long userId,Integer page,Integer limit){
        return Result.success().put("data", iIndustryNewsLikeService.getMyLikeIndustryNewsList(userId,page,limit));
    }
}
