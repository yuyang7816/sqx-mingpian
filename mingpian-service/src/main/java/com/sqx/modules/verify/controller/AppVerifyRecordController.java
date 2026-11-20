package com.sqx.modules.verify.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sqx.common.utils.Result;
import com.sqx.modules.verify.db.entity.VerifyRecord;
import com.sqx.modules.verify.model.bo.VerifyRecordPageBO;
import com.sqx.modules.verify.model.bo.VerifyRecordSaveBO;
import com.sqx.modules.verify.model.vo.VerifyRecordVO;
import com.sqx.modules.verify.service.VerifyRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author chengming
 * @since 2026/3/11
 */
@Api(tags = "用户端-核查记录")
@RestController
@RequestMapping("/app/verifyRecord")
public class AppVerifyRecordController {

    @Resource
    private VerifyRecordService verifyRecordService;

    @ApiOperation("新增核查")
    @PostMapping("save")
    public Result save(@RequestBody VerifyRecordSaveBO verifyRecordSaveBO) {
        VerifyRecord verifyRecord = verifyRecordService.save(verifyRecordSaveBO);
        return Result.success().put("data", verifyRecord);
    }

    @ApiOperation("列表")
    @PostMapping("page")
    public Result page(@RequestBody VerifyRecordPageBO verifyRecordPageBO) {
        IPage<VerifyRecordVO> voiPage = verifyRecordService.page(verifyRecordPageBO);
        return Result.success().put("data", voiPage);
    }

    @ApiOperation("删除")
    @GetMapping("delelte")
    public Result delete(Long id) {
        verifyRecordService.delete(id);
        return Result.success();
    }
}
