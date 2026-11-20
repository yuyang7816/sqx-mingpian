package com.sqx.modules.industryNewsComment.controller;

import com.sqx.common.utils.Result;
import com.sqx.modules.app.annotation.Login;
import com.sqx.modules.industryNewsComment.entity.IndustryNewsComment;
import com.sqx.modules.industryNewsComment.service.IIndustryNewsCommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 资讯评论-用户端
 *
 * @author wuChang
 * @since 2023-09-26
 */
@RestController
@Api(value = "资讯评论-用户端", tags = {"资讯评论-用户端"})
@RequestMapping("/app/industryNewsComment/")
public class AppIndustryNewsCommentController {
    @Autowired
    private IIndustryNewsCommentService iIndustryNewsCommentService;

    /**
     * 新增资讯评论
     *
     * @param industryNewsComment
     * @return
     */
    @Login
    @ApiOperation("新增或修改资讯评论")
    @PostMapping(value = "saveIndustryNewsComment")
    public Result saveIndustryNewsComment(@RequestAttribute("userId") Long userId, IndustryNewsComment industryNewsComment) {
        industryNewsComment.setUserId(userId);
        industryNewsComment.setStatus(0);
        return iIndustryNewsCommentService.saveIndustryNewsComment(industryNewsComment);
    }

    /**
     * 删除评论
     *
     * @param commentId 评论id
     * @return
     */
    @Login
    @ApiOperation("删除评论")
    @PostMapping(value = "deleteIndustryNewsComment")
    public Result deleteIndustryNewsComment(@RequestAttribute Long userId, Long commentId) {
        return iIndustryNewsCommentService.deleteIndustryNewsComment(userId, commentId);
    }


}
