package com.sqx.modules.member.controller;

import com.sqx.common.utils.Result;
import com.sqx.modules.member.entity.Member;
import com.sqx.modules.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 会员特权图标-管理端
 * @author wuChang
 * @since 2024-03-27
 */
@RestController
@Api(value = "会员特权图标-管理端", tags = {"会员特权图标-管理端"})
@RequestMapping("/admin/member/")
public class AdminMemberController {
    @Autowired
    private MemberService memberService;

    /**
     * 新增或修改会员特权图标
     *@param member 会员特权图标
     */
    @ApiOperation("新增或修改会员特权图标")
    @PostMapping(value = "saveMember")
    public Result saveMember(Member member) {
        return memberService.saveMember(member);
    }

    /**
     * 获取会员特权图标列表
     * @param member 会员特权图标
     */
    @ApiOperation("获取会员特权图标列表")
    @GetMapping(value = "getMemberList")
    public Result getMemberList(Integer page, Integer limit, Member member) {

        return Result.success().put("data", memberService.getMemberList(page, limit, member));
    }

    /**
     * 删除会员特权图标
     *@param memberId 会员特权图标id
     */
    @ApiOperation("删除会员特权图标")
    @PostMapping(value = "deleteMemberById")
    public Result deleteMemberById(Long memberId) {

        return memberService.removeById(memberId) ? Result.success() : Result.error();
    }


    /**
     * 获取会员特权图标详情
     *@param memberId 会员特权图标id
     */
    @ApiOperation("获取会员特权图标详情")
    @GetMapping(value = "getMemberById")
    public Result getMemberById(Long memberId) {
        return Result.success().put("data", memberService.getById(memberId));
    }

}