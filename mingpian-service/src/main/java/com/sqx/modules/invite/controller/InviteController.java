package com.sqx.modules.invite.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sqx.common.utils.Result;
import com.sqx.modules.invite.model.InvitePageVo;
import com.sqx.modules.invite.service.InviteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author fang
 * @date 2020/7/9
 */
@Slf4j
@RestController
@Api(value = "邀请收益", tags = {"邀请收益"})
@RequestMapping(value = "/admin/invite")
public class InviteController {

    @Autowired
    private InviteService inviteService;

    @ApiOperation("收益列表")
    @GetMapping("invitePage")
    public Result invitePage(Long userId, Integer page,Integer limit){
        IPage<InvitePageVo> voIPage = inviteService.invitePage(userId, page, limit);
        return Result.success().put("page", voIPage);
    }

}