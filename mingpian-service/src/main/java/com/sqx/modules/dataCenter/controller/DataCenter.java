package com.sqx.modules.dataCenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.annotation.Login;
import com.sqx.modules.app.entity.UserCertification;
import com.sqx.modules.app.service.UserCertificationService;
import com.sqx.modules.dataCenter.model.vo.CountCardVO;
import com.sqx.modules.dataCenter.service.DataCenterService;
import com.sqx.modules.scBrowse.service.ScBrowseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/app/dataCenter/")
@Api("数据中心")
public class DataCenter {

    @Autowired
    private ScBrowseService browseService;

    @Autowired
    private UserCertificationService certificationService;
    @Autowired
    private DataCenterService dataCenterService;

    @Login
    @GetMapping("/selectAmount")
    @ApiOperation("我发布的 我的收藏 我的点赞 我联系过的")
    public Result selectAmount(@RequestAttribute("userId") Long userId) {
        return browseService.selectAmount(userId);
    }

    @GetMapping("/getWaitExamine")
    @ApiOperation("获取待审核的数据")
    public Result getWaitExamine() {
        HashMap<String, Object> hashMap = new HashMap<>();
        // todo 发布待审核
        //个人认证待审核
        int userCount = certificationService.count(new QueryWrapper<UserCertification>().eq("user_type", 1).eq("status", 0));
        //企业认证待审核
        int enterpriseCount = certificationService.count(new QueryWrapper<UserCertification>().eq("user_type", 2).eq("status", 0));

        hashMap.put("userCount",userCount);
        hashMap.put("enterpriseCount",enterpriseCount);
        return Result.success().put("data", hashMap);
    }

    @ApiOperation("最近访客、最近查看、最近邀请")
    @GetMapping("countCard")
    public Result countCard(@RequestParam(value = "cardId", required = false) Long cardId) {
        CountCardVO vo = dataCenterService.countCard(cardId);
        return Result.success().put("data", vo);
    }
}
