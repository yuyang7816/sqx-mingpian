package com.sqx.modules.dataCenter.controller;

import com.sqx.common.utils.Result;
import com.sqx.modules.dataCenter.model.StatisticsBo;
import com.sqx.modules.dataCenter.model.vo.CardLineVO;
import com.sqx.modules.dataCenter.model.vo.CardStatisticsVO;
import com.sqx.modules.dataCenter.model.vo.UserLineVO;
import com.sqx.modules.dataCenter.service.DataCenterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author chengming
 * @since 2026/3/6
 */
@Api("管理端-数据中心")
@RestController
@RequestMapping("/admin/dataCenter")
public class AdminDataCenter {

    @Resource
    private DataCenterService dataCenterService;

    @ApiOperation("用户增加折线")
    @GetMapping("userLine")
    public Result userLine(@RequestParam String startTime,
                           @RequestParam String endTime) {
        UserLineVO vo = dataCenterService.userLine(startTime, endTime);
        return Result.success().put("data", vo);
    }

    @ApiOperation("名片增加折线")
    @GetMapping("cardLine")
    public Result cardLine(String startTime, String endTime) {
        CardLineVO vo = dataCenterService.cardLine(startTime, endTime);
        return Result.success().put("data", vo);
    }

    @ApiOperation("名片统计")
    @PostMapping("card")
    public Result card(@RequestBody StatisticsBo statisticsBo) {
        CardStatisticsVO vo = dataCenterService.card(statisticsBo);
        return Result.success().put("data", vo);
    }
}
