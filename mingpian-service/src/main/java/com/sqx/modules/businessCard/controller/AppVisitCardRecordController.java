package com.sqx.modules.businessCard.controller;

import com.sqx.modules.app.annotation.Login;
import com.sqx.modules.businessCard.service.VisitCardRecordService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import com.sqx.common.utils.Result;
import com.alibaba.fastjson.JSONObject;
/**
 * 访问记录表(VisitCardRecord)表控制层
 *
 * @author makejava
 * @since 2024-05-07 10:06:15
 */
@RestController
@RequestMapping("/app/visit")
public class AppVisitCardRecordController {
    /**
     * 服务对象
     */
    @Resource
    private VisitCardRecordService visitCardRecordService;

    /**
     * 分页查询
     *
     * @return 查询结果
     */
    @Login
    @ApiOperation("分页查询访问记录")
    @PostMapping("/page")
    public Result queryByPage(@RequestAttribute Long userId,@RequestBody JSONObject params) {
        Result result = visitCardRecordService.queryByPage(userId, params);
        return result;
    }

    /**
     * 新增数据
     *
     * @return 新增结果
     */
    @Login
    @ApiOperation("访问名片")
    @PostMapping("/save")
    public Result add(@RequestAttribute Long userId, @RequestBody JSONObject params) {
        Result result = visitCardRecordService.visitCard(userId, params);
        return result;
    }

    @Login
    @ApiOperation("访问数据统计")
    @GetMapping("/userData")
    public Result visitData(@RequestAttribute Long userId){
        Result result = visitCardRecordService.visitUserData(userId);
        return result;
    }

    @Login
    @ApiOperation("我的访客")
    @PostMapping("/visitMe")
    public Result visitMe(@RequestAttribute Long userId,@RequestBody JSONObject params){
        Result result = visitCardRecordService.visitMe(userId,params);
        return result;
    }

    @Login
    @ApiOperation("我的看过的")
    @PostMapping("/myVisit")
    public Result myVisit(@RequestAttribute Long userId,@RequestBody JSONObject params){
        Result result = visitCardRecordService.myVisit(userId,params);
        return result;
    }
}

