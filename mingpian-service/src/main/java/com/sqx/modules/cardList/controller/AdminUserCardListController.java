package com.sqx.modules.cardList.controller;

import com.alibaba.fastjson.JSONObject;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.annotation.Login;
import com.sqx.modules.cardList.entity.UserCardList;
import com.sqx.modules.cardList.service.UserCardListService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

/**
 * 用户名片夹(UserCardList)表控制层
 *
 * @author makejava
 * @since 2024-05-07 17:06:02
 */
@Api(tags = {"管理端-用户名片夹"})
@RestController
@RequestMapping("/admin/userCollect")
public class AdminUserCardListController {
    /**
     * 服务对象
     */
    @Resource
    private UserCardListService userCardListService;


    @Login
    @ApiOperation("收藏名片到名片夹")
    @PostMapping("/insert")
    public Result collectCard(@RequestAttribute Long userId, @RequestBody UserCardList userCardList){
        Result result = userCardListService.collectCard(userId, userCardList);
        return result;
    }

    @Login
    @ApiOperation("查询我的名片夹")
    @PostMapping("/list")
    public Result queryMyColletList(@RequestAttribute Long userId, @RequestBody JSONObject params){
        Result result = userCardListService.queryMyCollect(userId, params);
        return result;
    }

    @Login
    @ApiOperation("修改名片备注名称")
    @PostMapping("/editCardRemark")
    public Result updateCardRemark(@RequestBody JSONObject params){
        Result result = userCardListService.updateCardRemark(params);
        return result;
    }

    @Login
    @ApiOperation("修改备注")
    @PostMapping("/editRemark")
    public Result updateRemark(@RequestBody JSONObject params){
        Result result = userCardListService.updateRemark(params);
        return result;
    }

    @Login
    @ApiOperation("根据id查询名片详情信息")
    @GetMapping("/queryById/{id}")
    public Result queryDetailsById(@PathVariable String id){
        Result result = userCardListService.queryDetailsById(id);
        return result;
    }


    @GetMapping("/shareCard/{cardId}")
    @ApiOperation("小程序名片分享二维码")
    public void shareCard(@PathVariable("cardId") Long cardId, HttpServletResponse response) {
        userCardListService.shareCard(cardId,response);
    }


    @Login
    @ApiOperation("将用户添加至分组当中")
    @PostMapping("/moveToGroup")
    public Result setCardGroups(@RequestBody JSONObject params){
        Result result = userCardListService.moveToGroups(params);
        return result;
    }

    @ApiOperation("将收藏的名片移除")
    @GetMapping("/delCardList/{id}")
    public Result moveOutCardList(@PathVariable String id){
        Result result = userCardListService.removeById(id);
        return result;
    }

}

