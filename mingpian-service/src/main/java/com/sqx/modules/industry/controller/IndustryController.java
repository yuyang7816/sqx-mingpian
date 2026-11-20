package com.sqx.modules.industry.controller;

import com.sqx.common.utils.Result;
import com.sqx.modules.industry.entity.Industry;
import com.sqx.modules.industry.service.IndustryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

/**
 * 行业分类-管理端
 * @author wuChang
 * @since 2024-02-26
 */
@RestController
@Api(value = "行业分类-管理端", tags = {"行业分类-管理端"})
@RequestMapping("/admin/industry/")
public class IndustryController {
    @Autowired
    private IndustryService industryService;

    /**
     * 新增或修改行业分类
     *@param industry 行业分类
     */
    @ApiOperation("新增或修改行业分类")
    @PostMapping(value = "saveIndustry")
    public Result saveIndustry(Industry industry) {
        return industryService.saveIndustry(industry);
    }

    /**
     * 获取行业分类列表
     * @param industry 行业分类
     */
    @ApiOperation("获取行业分类列表")
    @GetMapping(value = "getIndustryList")
    public Result getIndustryList(Integer page, Integer limit, Industry industry) {

        return Result.success().put("data", industryService.getIndustryList(page, limit, industry));
    }
    /**
     * 获取行业Tree列表
     * @param industry 行业分类
     */
    @ApiOperation("获取行业Tree列表")
    @GetMapping("getIndustryTreeList")
    public Result getIndustryTreeList() {
        return Result.success().put("data", industryService.getIndustryTreeList());
    }

    /**
     * 删除行业分类
     *@param industryId 行业分类id
     */
    @ApiOperation("删除行业分类")
    @PostMapping(value = "deleteIndustryById")
    public Result deleteIndustryById(Long industryId) {

        return industryService.deleteIndustryById(industryId);
    }

}