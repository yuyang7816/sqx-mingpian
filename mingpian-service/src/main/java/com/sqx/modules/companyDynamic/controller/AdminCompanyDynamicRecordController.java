package com.sqx.modules.companyDynamic.controller;

import com.alibaba.fastjson.JSONObject;
import com.sqx.common.utils.Result;
import com.sqx.modules.companyDynamic.service.CompanyDynamicRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 企业动态表(CompanyDynamicRecord)表控制层
 *
 * @author makejava
 * @since 2024-05-09 17:14:51
 */
@Api(tags = "管理端-企业动态")
@RestController
@RequestMapping("/admin/cDynamic")
public class AdminCompanyDynamicRecordController {
    /**
     * 服务对象
     */
    @Resource
    private CompanyDynamicRecordService companyDynamicRecordService;

    /**
     * 分页查询企业员工动态信息
     * @param params
     * @return
     */
    @ApiOperation("分页查询企业员工动态信息")
    @PostMapping("/page")
    public Result queryByComCardId(@RequestBody JSONObject params){
        Result result = companyDynamicRecordService.queryByComCardId(params);
        return result;
    }

}

