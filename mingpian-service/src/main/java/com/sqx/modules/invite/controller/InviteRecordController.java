package com.sqx.modules.invite.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sqx.common.utils.Result;
import com.sqx.modules.invite.model.InviteRecordVo;
import com.sqx.modules.invite.service.InviteRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
/**
 * 邀请记录表(InviteRecord)表控制层
 *
 * @author makejava
 * @since 2024-05-13 18:04:08
 */
@Api(tags = "管理端-邀请记录")
@RestController
@RequestMapping("inviteRecord")
public class InviteRecordController {
    /**
     * 服务对象
     */
    @Resource
    private InviteRecordService inviteRecordService;

    @ApiOperation(value = "我的邀请列表")
    @GetMapping("myInviteRecord")
    public Result myInviteRecord(Long userId, Integer page, Integer limit) {
        if (page == null || 0 == page) {
            page = 1;
        }

        if (limit == null || 0 == limit) {
            limit = 10;
        }

        IPage<InviteRecordVo> voIPage = inviteRecordService.myInviteRecord(userId, page, limit);
        return Result.success().put("page", voIPage);
    }
}

