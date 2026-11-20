package com.sqx.modules.businessCard.controller;

import com.alibaba.fastjson.JSONObject;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.annotation.Login;
import com.sqx.modules.businessCard.service.CardStyleService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @description：TODO
 * @author： qiao
 * @create： 2024/5/7 9:05
 */
@RestController
@RequestMapping("/app/cardStyle")
public class AppCardStyleController {

    @Resource
    private CardStyleService cardStyleService;


    @Login
    @ApiOperation("查询名片样式")
    @PostMapping("/list")
    public Result queryStyleList(@RequestBody JSONObject params){
        Result result = cardStyleService.appList(params);
        return result;
    }
}
