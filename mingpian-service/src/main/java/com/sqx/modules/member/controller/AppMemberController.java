package com.sqx.modules.member.controller;

import com.sqx.common.utils.Result;
import com.sqx.modules.member.entity.Member;
import com.sqx.modules.member.service.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 会员特权图标-管理端
 * @author wuChang
 * @since 2024-03-27
 */
@RestController
@Api(value = "会员特权图标-用户端", tags = {"会员特权图标-用户端"})
@RequestMapping("/app/member/")
public class AppMemberController {
    @Autowired
    private MemberService memberService;

    /**
     * 获取会员特权图标列表
     * @param member 会员特权图标
     */
    @ApiOperation("获取会员特权图标列表")
    @GetMapping(value = "getMemberList")
    public Result getMemberList(Integer page, Integer limit, Member member) {

        return Result.success().put("data", memberService.getMemberList(page, limit, member));
    }

}