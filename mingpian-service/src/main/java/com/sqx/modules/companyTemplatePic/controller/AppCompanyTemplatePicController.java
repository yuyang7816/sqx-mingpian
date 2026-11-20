package com.sqx.modules.companyTemplatePic.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sqx.common.utils.Result;
import com.sqx.modules.companyTemplatePic.domain.CompanyTemplatePic;
import com.sqx.modules.companyTemplatePic.domain.bo.CompanyTemplatePicPageBo;
import com.sqx.modules.companyTemplatePic.service.CompanyTemplatePicService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author chengming
 * @since 2025/12/16
 */
@Api(value = "用户端-模板图片", tags = {"用户端-模板图片"})
@RestController
@RequestMapping("/app/companyTemplatePic")
public class AppCompanyTemplatePicController {

    @Resource
    private CompanyTemplatePicService companyTemplatePicService;

    @ApiOperation(value = "保存")
    @PostMapping("save")
    public Result save(@RequestBody CompanyTemplatePic companyTemplatePic) {
        companyTemplatePicService.saveInfo(companyTemplatePic);
        return Result.success();
    }

    @ApiOperation(value = "更新")
    @PostMapping("update")
    public Result update(@RequestBody CompanyTemplatePic companyTemplatePic) {
        companyTemplatePicService.updateInfo(companyTemplatePic);
        return Result.success();
    }

    @ApiOperation(value = "列表")
    @PostMapping("page")
    public Result page(@RequestBody CompanyTemplatePicPageBo companyTemplatePicPageBo) {
        IPage<CompanyTemplatePic> companyTemplatePicIPage = companyTemplatePicService.pageInfo(companyTemplatePicPageBo);
        return Result.success().put("data", companyTemplatePicIPage);
    }

    @ApiOperation(value = "删除")
    @GetMapping("delete/{id}")
    public Result delete(@PathVariable("id") Long id) {
        companyTemplatePicService.delete(id);
        return Result.success();
    }
}
