package com.sqx.modules.invite.controller.app;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.annotation.Login;
import com.sqx.modules.invite.model.InviteRecordVo;
import com.sqx.modules.invite.service.InviteRecordService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 邀请记录表(InviteRecord)表控制层
 *
 * @author makejava
 * @since 2024-05-13 18:04:08
 */
@RestController
@RequestMapping("app/inviteRecord")
public class AppInviteRecordController {

    @Resource
    private InviteRecordService inviteRecordService;

    @Login
    @ApiOperation(value = "我的邀请列表")
    @GetMapping("myInviteRecord")
    public Result myInviteRecord(@RequestAttribute("userId") Long userId, Integer page, Integer limit) {
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

