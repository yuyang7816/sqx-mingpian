package com.sqx.modules.companyCard.controller;

import com.alibaba.fastjson.JSONObject;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.annotation.Login;
import com.sqx.modules.companyCard.service.CompanyStaffRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 公司员工动态信息(CompanyStaffRecord)表控制层
 *
 * @author makejava
 * @since 2024-05-09 13:56:04
 */
@Api(tags = "公司员工动态信息")
@RestController
@RequestMapping("/app/comRecord")
public class AppCompanyStaffRecordController {
    /**
     * 服务对象
     */
    @Resource
    private CompanyStaffRecordService companyStaffRecordService;


    @Login
    @ApiOperation("申请加入企业")
    @PostMapping("/applyJoin")
    public Result userApplyJoin(@RequestAttribute Long userId, @RequestBody JSONObject params){
        Result result = companyStaffRecordService.applyJoin(userId, params);
        return result;
    }

    @Login
    @ApiOperation("列表")
    @GetMapping("/page")
    public Result page(Integer pageNum, Integer pageSize, Long cardId, Integer status) {
        Result result = companyStaffRecordService.pageInfo(pageNum, pageSize, cardId, status);
        return result;
    }

    @Login
    @ApiOperation("审核加入申请")
    @PostMapping("/auditApply")
    public Result auditApply(@RequestBody JSONObject params){
        Result result = companyStaffRecordService.auditApply(params);
        return result;
    }

    @ApiOperation("申请加入数量")
    @GetMapping("/applyCount")
    public Result applyCount(Long cardId) {
        Integer count = companyStaffRecordService.applyCount(cardId);
        return Result.success().put("data", count);
    }
}

