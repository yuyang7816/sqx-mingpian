package com.sqx.modules.industryNews.controller;

import com.sqx.common.utils.Result;
import com.sqx.modules.app.annotation.Login;
import com.sqx.modules.industryNews.entity.IndustryNews;
import com.sqx.modules.industryNews.service.IndustryNewsService;
import com.sqx.modules.industryNews.vo.IndustryNewsVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 行业资讯-用户端
 * @author wuChang
 * @since 2024-02-26
 */
@RestController
@Api(value = "行业资讯-用户端", tags = {"行业资讯-用户端"})
@RequestMapping("/app/industryNews/")
public class AppIndustryNewsController {
    @Autowired
    private IndustryNewsService industryNewsService;
    /**
     * 新增或修改行业资讯
     *@param industryNews 行业资讯
     */
    @Login
    @ApiOperation("新增或修改行业资讯")
    @PostMapping(value = "saveIndustryNews")
    public Result saveIndustryNews(@RequestAttribute Long userId, IndustryNews industryNews) {
        industryNews.setUserId(userId);
        industryNews.setStatus(0);
        industryNews.setIsEnable(0);
        return industryNewsService.saveIndustryNews(industryNews);
    }

    /**
     * 获取行业资讯列表
     * @param industryNewsVo 行业资讯
     */
    @ApiOperation("获取行业资讯列表")
    @GetMapping(value = "getIndustryNewsList")
    public Result getIndustryNewsList(Integer page, Integer limit, Long loginUserId ,IndustryNewsVo industryNewsVo) {
        industryNewsVo.setStatus(1);
        industryNewsVo.setIsEnable(1);
        if (StringUtils.isNotBlank(industryNewsVo.getKeyWord())  && loginUserId!=null) {
        }
        return Result.success().put("data", industryNewsService.getIndustryNewsList(page, limit, industryNewsVo));
    }
    /**
     * 获取行业资讯列表
     * @param industryNews 行业资讯
     */
    @Login
    @ApiOperation("获取我发布的行业资讯列表")
    @GetMapping(value = "getMyIndustryNewsList")
    public Result getMyIndustryNewsList(@RequestAttribute Long userId, Integer page, Integer limit, IndustryNewsVo industryNews) {
        industryNews.setUserId(userId);
        return Result.success().put("data", industryNewsService.getIndustryNewsList(page, limit, industryNews));
    }
    /**
     * 删除行业资讯
     *@param newsId 行业资讯id
     */
    @Login
    @ApiOperation("删除行业资讯")
    @PostMapping(value = "deleteIndustryNewsById")
    public Result deleteIndustryNewsById(@RequestAttribute Long userId, Long newsId) {

        return industryNewsService.deleteIndustryNewsById(userId,newsId);
    }


    /**
     * 获取行业资讯详情
     *@param newsId 行业资讯id
     */
    @ApiOperation("获取行业资讯详情")
    @GetMapping(value = "getIndustryNewsById")
    public Result getIndustryNewsById(Long loginUserId,Long newsId) {
        return Result.success().put("data", industryNewsService.getIndustryNewsById(loginUserId,newsId));
    }
    /**
     * 获取我的点赞列表
     */
    @Login
    @ApiOperation("获取我的点赞列表")
    @GetMapping(value = "getMyLikeIndustryList")
    public Result getMyLikeIndustryList(@RequestAttribute Long userId,Integer page,Integer limit){
        return Result.success().put("data", industryNewsService.getMyLikeIndustryList(page,limit,userId));
    }
}