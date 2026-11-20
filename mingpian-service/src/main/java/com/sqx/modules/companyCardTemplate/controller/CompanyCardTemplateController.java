package com.sqx.modules.companyCardTemplate.controller;

import com.alibaba.fastjson.JSONObject;
import com.sqx.common.utils.Result;
import com.sqx.modules.companyCardTemplate.entity.CompanyTemplate;
import com.sqx.modules.companyCardTemplate.service.CompanyTemplateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author： qiao
 * @create： 2024/5/9 11:35
 */
@Api(tags = "管理端-公司名片模板")
@RestController
@RequestMapping("/card/template")
public class CompanyCardTemplateController {
    @Resource
    private CompanyTemplateService companyTemplateService;

    @ApiOperation("新增公司名片模板")
    @PostMapping("/save")
    public Result savetemplate(@RequestBody CompanyTemplate companyTemplate){
        Result result = companyTemplateService.saveCompanyTemp(companyTemplate);
        return result;
    }

    @ApiOperation("查询公司名片模板")
    @PostMapping("/query")
    public Result queryTemplate(@RequestBody JSONObject params){
        Result result = companyTemplateService.queryTemplate(params);
        return result;
    }

    @ApiOperation("编辑公司名片模板")
    @PostMapping("/update")
    public Result updateTemplate(@RequestBody CompanyTemplate companyTemplate){
        return companyTemplateService.updateTemplate(companyTemplate);
    }

    @ApiOperation("查看本公司模板")
    @PostMapping("/info")
    public Result info(@RequestParam("companyCardId") Long companyCardId){
        Result result = companyTemplateService.info(companyCardId);
        return result;
    }
}
