package com.sqx.modules.industry.controller;

import com.sqx.common.utils.Result;
import com.sqx.modules.industry.entity.Industry;
import com.sqx.modules.industry.service.IndustryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 行业分类-用户端
 * @author wuChang
 * @since 2024-02-26
 */
@RestController
@Api(value = "行业分类-用户端", tags = {"行业分类-用户端"})
@RequestMapping("/app/industry/")
public class AppIndustryController {
    @Autowired
    private IndustryService industryService;


    /**
     * 获取行业分类列表
     * @param industry 行业分类
     */
    @ApiOperation("获取行业分类列表")
    @GetMapping(value = "getIndustryList")
    public Result getIndustryList(Integer page, Integer limit, Industry industry) {
        industry.setIsEnable(1);
        return Result.success().put("data", industryService.getIndustryList(page, limit, industry));
    }

    @ApiOperation("获取行业Tree列表")
    @GetMapping("getIndustryTreeList")
    public Result getIndustryTreeList() {
        return Result.success().put("data", industryService.getIndustryTreeList());
    }

}