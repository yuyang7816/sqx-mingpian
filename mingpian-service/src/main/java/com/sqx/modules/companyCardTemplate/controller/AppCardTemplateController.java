package com.sqx.modules.companyCardTemplate.controller;

import com.alibaba.fastjson.JSONObject;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.annotation.Login;
import com.sqx.modules.companyCardTemplate.entity.CompanyTemplate;
import com.sqx.modules.companyCardTemplate.service.CompanyTemplateService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @description：TODO app企业名片模板管理
 * @author： qiao
 * @create： 2024/5/9 10:56
 */
@RestController
@RequestMapping("/app/template")
public class AppCardTemplateController {


    @Resource
    private CompanyTemplateService companyTemplateService;


    /*
    * 模板添加
    * */
    @Login
    @ApiOperation("新增公司名片模板")
    @PostMapping("/save")
    public Result savetemplate(@RequestBody CompanyTemplate companyTemplate){
        Result result = companyTemplateService.saveCompanyTemp(companyTemplate);
        return result;
    }


    @Login
    @ApiOperation("查询公司名片模板")
    @PostMapping("/query")
    public Result queryTemplate(@RequestBody JSONObject params){
        Result result = companyTemplateService.queryTemplate(params);
        return result;
    }

    @Login
    @ApiOperation("编辑公司名片模板")
    @PostMapping("/update")
    public Result updateTemplate(@RequestBody CompanyTemplate companyTemplate){
        return companyTemplateService.updateTemplate(companyTemplate);
    }

    @Login
    @ApiOperation("查看本公司模板")
    @PostMapping("/info")
    public Result info(@RequestParam("companyCardId") Long companyCardId){
        Result result = companyTemplateService.info(companyCardId);
        return result;
    }
}
