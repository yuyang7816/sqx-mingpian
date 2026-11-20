package com.sqx.modules.businessCard.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.annotation.Login;
import com.sqx.modules.businessCard.entity.BusinessCard;
import com.sqx.modules.businessCard.service.BusinessCardService;
import com.sqx.modules.invite.service.InviteRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 名片管理-用户端
 * @author wuChang
 * @since 2024-02-26
 */
@RestController
@Api(value = "名片管理-用户端", tags = {"名片管理-用户端"})
@RequestMapping("/app/businessCard/")
public class AppBusinessCardController {
    @Autowired
    private BusinessCardService businessCardService;
    @Resource
    private InviteRecordService inviteRecordService;

    /**
     * 获取市列表
     */
    @ApiOperation("获取市列表")
    @GetMapping(value = "getCity")
    public Result getCity(){
        return Result.success().put("data", businessCardService.getCity());
    }

    /**
     * 切换名片查询接口
     * @param businessCard 名片管理
     */
    @Login
    @ApiOperation("切换名片查询接口")
    @PostMapping(value = "/myCardList")
    public Result getMyBusinessCardList(@RequestAttribute Long userId,@RequestBody JSONObject params) {
        return Result.success().put("data", businessCardService.getMyBusinessCardList(userId,params));
    }


    @Login
    @ApiOperation("新增名片")
    @PostMapping("/insert")
    public Result saveBusiessCard(@RequestAttribute Long userId,@RequestBody BusinessCard businessCard){
        Result result = businessCardService.saveBusinessCard(userId, businessCard);
        return result;
    }


    @Login
    @ApiOperation("修改名片")
    @PostMapping("/edit")
    public Result updateBusiessCard(@RequestBody BusinessCard businessCard){
        Result result = businessCardService.updateBusinessCard(businessCard);
        return result;
    }

    @Login
    @ApiOperation("修改名片样式")
    @GetMapping("/editStyle")
    public Result editStyle(Long cardId, String cardStyleCode){
        businessCardService.editStyle(cardId, cardStyleCode);
        return Result.success();
    }


    /**
     * 删除名片管理
     *@param cardId 名片管理id
     */
    @Login
    @ApiOperation("删除名片管理")
    @GetMapping(value = "/delById/{id}")
    public Result deleteBusinessCardById(@PathVariable Long id) {

        return businessCardService.deleteBusinessCardById(id);
    }


    /**
     * 获取名片管理详情
     *@param cardId 名片管理id
     */
    @Login
    @ApiOperation("获取名片管理详情")
    @PostMapping(value = "/queryById")
    public Result getBusinessCardById(@RequestBody JSONObject params,@RequestAttribute Long userId) {
        Result result = businessCardService.getBusinessCardById(params,userId);
        return result;
    }
    /**
     * 获取我的名片信息
     */
    @Login
    @ApiOperation("获取我的名片信息")
    @GetMapping(value = "/list")
    public Result getBusinessCard(@RequestAttribute Long userId){
        return Result.success().put("data", businessCardService.getBusinessCard(userId));
    }

    @Login
    @ApiOperation("查询所有的名片")
    @PostMapping("/queryAllList")
    public Result queryAllList(@RequestAttribute Long userId,@RequestBody JSONObject params){
        Result result = businessCardService.queryAllList(userId, params);
        return result;
    }

    @Login
    @ApiOperation("查询用户展示的名片")
    @GetMapping("/showCard")
    public Result queryShowCard(@RequestAttribute Long userId){
        Result result = businessCardService.queryShowCard(userId);
        return result;
    }

    @Login
    @ApiOperation("切换用户展示的名片")
    @GetMapping("/switchCard/{cardId}")
    public Result editShowCard(@RequestAttribute Long userId,@PathVariable Long cardId){
        Result result = businessCardService.eidtShowCard(userId, cardId);
        return result;
    }


    @Login
    @ApiOperation("名片访问记录增加")
    @GetMapping("/browseCount/{cardId}")
    public Result addCount(@PathVariable Long cardId){
        Result result = businessCardService.addBrowseCount(cardId);
        return result;
    }

    @Login
    @ApiOperation("根据用户id查询用户名片")
    @GetMapping("/queryUserCard/{userId}")
    public Result queryCardByUserId(@PathVariable Long userId){
        Result result = businessCardService.queryCardByUserId(userId);
        return result;
    }

    @ApiOperation("查询名片榜单")
    @PostMapping("/hotList")
    public Result cardHotList(@RequestBody JSONObject params){
        Result result = businessCardService.cardHotList(params);
        return result;
    }

    @ApiOperation("获取名片二维码")
    @GetMapping("/cardQrCode/{cardId}")
    public void getQrCode(@PathVariable Long cardId, HttpServletResponse response){
        businessCardService.getQrCode(cardId,response);
    }

    @Login
    @ApiOperation("我邀请的")
    @GetMapping("/myInvite")
    public Result myInvite(@RequestAttribute Long userId,
                           Integer page,
                           Integer limit,
                           @RequestParam(value = "cardId", required = false) Long cardId){
        Result result = inviteRecordService.myInvite(userId, page, limit, cardId);
        return result;
    }

    @ApiOperation("信息")
    @GetMapping("info")
    public Result info (@RequestParam("cardId") Long cardId, Long userId) {
        BusinessCard businessCard = businessCardService.info(cardId, userId);
        return Result.success().put("data", businessCard);
    }

    @ApiOperation("根据手机号查询名片")
    @GetMapping("queryByPhone")
    public Result queryByPhone (@RequestParam("cardId") Long cardId, @RequestParam("phone") String phone) {
        List<BusinessCard> businessCards = businessCardService.queryByPhone(cardId, phone);
        return Result.success().put("data", businessCards);
    }

    @ApiOperation("搜索名片")
    @GetMapping("searchBusinessCard")
    public Result searchBusinessCard(@RequestParam("keyword") String keyword,
                                     @RequestParam("userId") Long userId,
                                     Integer page,
                                     Integer limit) {
        IPage<BusinessCard> businessCardIPage = businessCardService.searchBusinessCard(keyword, userId, page, limit);
        return Result.success().put("data", businessCardIPage);
    }

    @ApiOperation("用户是否有名片")
    @GetMapping("userCardExists")
    public Result userCardExists(Long userId){
        Boolean exists = businessCardService.userCardExists(userId);
        return Result.success().put("data", exists);
    }
}