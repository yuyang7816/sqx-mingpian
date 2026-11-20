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
 * 资讯点赞-管理端
 * @author wuChang
 * @since 2023-09-27
 */
@RestController
@Api(value = "资讯点赞-管理端", tags = {"资讯点赞-管理端"})
@RequestMapping("/admin/industryNewsLike/")
public class AdminIndustryNewsLikeController {
    @Autowired
    private IIndustryNewsLikeService iIndustryNewsLikeService;

    /**
     * 新增或修改资讯点赞
     */
    @ApiOperation("获取我的点赞列表")
    @GetMapping(value = "getMyLikeIndustryNewsList")
    public Result getMyLikeIndustryNewsList(Long userId,Integer page,Integer limit){
        return Result.success().put("data", iIndustryNewsLikeService.getMyLikeIndustryNewsList(userId,page,limit));
    }

    /**
     * 获取资讯的点赞用户列表
     */
    @ApiOperation("获取资讯的点赞用户列表")
    @GetMapping(value = "getLikeUserList")
    public Result getLikeUserList(Long newsId,Integer page,Integer limit){
        return Result.success().put("data", iIndustryNewsLikeService.getLikeUserList(newsId,page,limit));
    }
}
