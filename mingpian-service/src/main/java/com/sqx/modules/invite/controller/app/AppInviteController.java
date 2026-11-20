package com.sqx.modules.invite.controller.app;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.annotation.Login;
import com.sqx.modules.app.entity.UserEntity;
import com.sqx.modules.app.service.UserService;
import com.sqx.modules.common.entity.CommonInfo;
import com.sqx.modules.common.service.CommonInfoService;
import com.sqx.modules.invite.model.InvitePageVo;
import com.sqx.modules.invite.service.InviteService;
import com.sqx.modules.utils.SenInfoCheckUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @author fang
 * @date 2020/7/9
 */
@Slf4j
@RestController
@Api(value = "邀请收益", tags = {"邀请收益"})
@RequestMapping(value = "/app/invite")
public class AppInviteController {

    @Autowired
    private InviteService inviteService;
    @Autowired
    private UserService userService;
    @Autowired
    private CommonInfoService commonInfoService;

    @RequestMapping(value = "/selectInviteCount", method = RequestMethod.GET)
    @ApiOperation("查看我邀请的人员数量")
    @ResponseBody
    public Result selectInviteCount(Integer state,Long userId){
        return Result.success().put("data",inviteService.selectInviteCount(state,userId));
    }
    @Login
    @RequestMapping(value = "/selectInviteAndPoster", method = RequestMethod.GET)
    @ApiOperation("查看我的邀请码和海报二维码")
    @ResponseBody
    public Result selectInviteAndPoster(@RequestAttribute Long userId){
        UserEntity userEntity = userService.queryByUserId(userId);
        CommonInfo one = commonInfoService.findOne(19);
        Map<String,Object> map=new HashMap<>();
        map.put("url",one.getValue());
        map.put("user",userEntity);
        return Result.success().put("data",map);
    }

    @Login
    @RequestMapping(value = "/selectInviteMoney", method = RequestMethod.GET)
    @ApiOperation("我的收益")
    @ResponseBody
    public Result selectInviteMoney(@RequestAttribute("userId") Long userId){
        Double inviteMoney = inviteService.selectInviteSum(-1,userId);
        Integer inviteCount = inviteService.selectInviteCount(-1, userId);
        Map<String,Object> result=new HashMap<>();
        result.put("inviteMoney",inviteMoney);
        result.put("inviteCount",inviteCount);
        return Result.success().put("data",result);
    }

    @GetMapping("/mpCreateQr")
    @ApiOperation("小程序推广二维码")
    public void mpCreateQr(@RequestParam String invitationCode, HttpServletResponse response) {
        SenInfoCheckUtil.getPoster(invitationCode,response);
    }

    @Login
    @ApiOperation("收益列表")
    @GetMapping("invitePage")
    public Result invitePage(@RequestAttribute("userId") Long userId, Integer page,Integer limit){
        IPage<InvitePageVo> voIPage = inviteService.invitePage(userId, page, limit);
        return Result.success().put("page", voIPage);
    }
}