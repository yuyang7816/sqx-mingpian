package com.sqx.modules.companyCard.controller;

import com.alibaba.fastjson.JSONObject;
import com.sqx.common.utils.Result;
import com.sqx.modules.businessCard.service.BusinessCardService;
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
@Api(tags = "管理端-公司员工动态信息")
@RestController
@RequestMapping("/admin/comRecord")
public class AdminCompanyStaffRecordController {
    /**
     * 服务对象
     */
    @Resource
    private CompanyStaffRecordService companyStaffRecordService;
    @Resource
    private BusinessCardService businessCardService;


    @ApiOperation("申请加入企业")
    @PostMapping("/applyJoin")
    public Result userApplyJoin(@RequestAttribute Long userId, @RequestBody JSONObject params){
        Result result = companyStaffRecordService.applyJoin(userId, params);
        return result;
    }

    @ApiOperation("列表")
    @GetMapping("/page")
    public Result page(Integer pageNum, Integer pageSize, Long cardId, Integer status) {
        Result result = companyStaffRecordService.pageInfo(pageNum, pageSize, cardId, status);
        return result;
    }

    @ApiOperation("审核加入申请")
    @PostMapping("/auditApply")
    public Result auditApply(@RequestBody JSONObject params){
        Result result = companyStaffRecordService.auditApply(params);
        return result;
    }

    @ApiOperation("剔除员工名片")
    @GetMapping("/delStaffCard")
    public Result delStaffCard(@RequestParam("cardId") Long cardId, @RequestParam("staffId") Long staffId){
        Result result = businessCardService.delStaffCard(cardId, staffId);
        return result;
    }
}

