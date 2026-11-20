package com.sqx.modules.industryNews.controller;

import com.sqx.common.utils.Result;
import com.sqx.modules.app.annotation.Login;
import com.sqx.modules.industryNews.entity.IndustryNews;
import com.sqx.modules.industryNews.service.IndustryNewsService;
import com.sqx.modules.industryNews.vo.IndustryNewsVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 行业资讯-管理端
 * @author wuChang
 * @since 2024-02-26
 */
@RestController
@Api(value = "行业资讯-管理端", tags = {"行业资讯-管理端"})
@RequestMapping("/admin/industryNews/")
public class AdminIndustryNewsController {
    @Autowired
    private IndustryNewsService industryNewsService;

    /**
     * 新增或修改行业资讯
     *@param industryNews 行业资讯
     */
    @ApiOperation("新增或修改行业资讯")
    @PostMapping(value = "adminSaveIndustryNews")
    public Result adminSaveIndustryNews(IndustryNews industryNews) {
        return industryNewsService.adminSaveIndustryNews(industryNews);
    }

    /**
     * 获取行业资讯列表
     * @param industryNewsVo 行业资讯
     */
    @ApiOperation("获取行业资讯列表")
    @GetMapping(value = "getIndustryNewsList")
    public Result getIndustryNewsList(Integer page, Integer limit, IndustryNewsVo industryNewsVo) {

        return Result.success().put("data", industryNewsService.getIndustryNewsList(page, limit, industryNewsVo));
    }

    /**
     * 删除行业资讯
     *@param newsId 行业资讯id
     */
    @ApiOperation("删除行业资讯")
    @PostMapping(value = "deleteIndustryNewsById")
    public Result deleteIndustryNewsById(Long newsId) {

        return industryNewsService.deleteIndustryNewsById(null, newsId);
    }


    /**
     * 获取行业资讯详情
     *@param newsId 行业资讯id
     */
    @ApiOperation("获取行业资讯详情")
    @GetMapping(value = "getIndustryNewsById")
    public Result getIndustryNewsById(Long newsId) {
        return Result.success().put("data", industryNewsService.getById(newsId));
    }

    /**
     * 审核行业资讯
     * @param newsId 资讯id
     * @param status 1审核通过 2审核不同
     * @param examineRemark 审核备注
     * @return
     */
    @ApiOperation("审核行业资讯")
    @PostMapping(value = "examineNews")
    public Result examineNews(Long newsId, Integer status, String examineRemark) {
        return industryNewsService.examineNews(newsId, status, examineRemark);
    }

    @ApiOperation("行业资讯上下架")
    @PostMapping(value = "upOrDownNews")
    public Result upOrDownNews(Long newsId,String downRemark) {
        return industryNewsService.upOrDownNews(newsId,downRemark);
    }
}