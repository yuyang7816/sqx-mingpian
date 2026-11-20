package com.sqx.modules.companyDynamic.controller;

import com.sqx.modules.app.annotation.Login;
import com.sqx.modules.companyDynamic.entity.CompanyDynamicRecord;
import com.sqx.modules.companyDynamic.service.CompanyDynamicRecordService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import com.sqx.common.utils.Result;
import com.alibaba.fastjson.JSONObject;
/**
 * 企业动态表(CompanyDynamicRecord)表控制层
 *
 * @author makejava
 * @since 2024-05-09 17:14:51
 */
@RestController
@RequestMapping("/app/cDynamic")
public class AppCompanyDynamicRecordController {
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
    @Login
    @ApiOperation("分页查询企业员工动态信息")
    @PostMapping("/page")
    public Result queryByComCardId(@RequestBody JSONObject params){
        Result result = companyDynamicRecordService.queryByComCardId(params);
        return result;
    }

}

