package com.sqx.modules.companyCard.controller;

import com.alibaba.fastjson.JSONObject;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.annotation.Login;
import com.sqx.modules.businessCard.service.BusinessCardService;
import com.sqx.modules.companyCard.domain.vo.StaffDataVo;
import com.sqx.modules.companyCard.service.CompanyStaffRecordService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @description：TODO
 * @author： qiao
 * @create： 2024/5/9 10:49
 */
@RestController
@RequestMapping("/app/company")
public class AppCompanyController {

    @Resource
    private BusinessCardService businessCardService;
    @Resource
    private CompanyStaffRecordService companyStaffRecordService;


    @Login
    @ApiOperation("查询企业名片信息")
    @GetMapping("/cardInfo")
    public Result queryComCardInfo(@RequestAttribute Long userId){
        Result result = businessCardService.queryCompanyCard(userId);
        return result;
    }


    /*
    * 公司录入员工名片
    * */
    @Login
    @ApiOperation("公司录入员工名片")
    @GetMapping("/saveStaffCard")
    public Result insertCard(@RequestAttribute Long userId,
                             @RequestParam("cardId") Long cardId,
                             @RequestParam("companyCardId") Long companyCardId){
        Result result = businessCardService.saveStaffCard(userId, cardId, companyCardId);
        return result;
    }

    /*
    * 公司剔除员工名片
    * */
    @Login
    @ApiOperation("剔除员工名片")
    @GetMapping("/delStaff/{staffId}")
     public Result delStaffCard(@RequestAttribute Long userId,@PathVariable Long staffId){
        Result result = businessCardService.delStaff(userId, staffId);
        return result;
     }


     @Login
     @ApiOperation("查询企业全部成员")
     @PostMapping("/queryStaff")
     public Result queryAllStaff(@RequestBody JSONObject params){
         Result result = businessCardService.queryStaff(params);
         return result;
     }

     @Login
     @ApiOperation("用户申请加入列表")
     @PostMapping("/applyList")
     public Result queryApplyRecord(@RequestBody JSONObject params){
         Result result = companyStaffRecordService.queryApplyRecord(params);
         return result;
     }


     @Login
     @ApiOperation("审核加入申请")
     @PostMapping("/auditApply")
     public Result auditApply(@RequestBody JSONObject params){
         Result result = companyStaffRecordService.auditApply(params);
         return result;
     }

    @Login
    @ApiOperation("企业员工统计")
    @GetMapping("staffData")
    public Result staffData(Long cardId) {
        StaffDataVo vo = companyStaffRecordService.staffData(cardId);
        return Result.success().put("data", vo);
    }
}
