package com.sqx.modules.industryNewsComment.controller;

import com.sqx.common.utils.Result;
import com.sqx.modules.industryNewsComment.entity.IndustryNewsComment;
import com.sqx.modules.industryNewsComment.service.IIndustryNewsCommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 资讯评论-管理端
 *
 * @author wuChang
 * @since 2023-09-26
 */
@RestController
@Api(value = "资讯评论-管理端", tags = {"资讯评论-管理端"})
@RequestMapping("/admin/industryNewsComment/")
public class AdminIndustryNewsCommentController {
    @Autowired
    private IIndustryNewsCommentService iIndustryNewsCommentService;

    /**
     * 获取评论列表
     *
     * @param page          第几页
     * @param limit         多少条
     * @param industryNewsComment
     * @return
     */
    @ApiOperation("获取评论列表")
    @GetMapping(value = "getCommentList")
    public Result getCommentList(Integer page, Integer limit, IndustryNewsComment industryNewsComment) {
        return Result.success().put("data", iIndustryNewsCommentService.getCommentList(page, limit, industryNewsComment));
    }

    /**
     * 新增资讯评论
     *
     * @param industryNewsComment
     * @return
     */
    @ApiOperation("新增或修改资讯评论")
    @PostMapping(value = "saveIndustryNewsComment")
    public Result saveIndustryNewsComment(IndustryNewsComment industryNewsComment) {
        return iIndustryNewsCommentService.saveIndustryNewsComment(industryNewsComment);
    }


    /**
     * 审核评论
     * @param commentId 评论id
     * @param status 1审核通过 2审核未通过
     * @param remarks
     * @return
     */
    @ApiOperation("审核评论")
    @PostMapping(value = "auditComment")
    public Result auditComment(Long commentId, Integer status, String remarks) {
        return iIndustryNewsCommentService.auditComment(commentId, status, remarks);
    }

    /**
     * 删除评论
     *
     * @param commentId 评论id
     * @return
     */
    @ApiOperation("删除评论")
    @PostMapping(value = "deleteIndustryNewsComment")
    public Result deleteIndustryNewsComment(Long commentId) {
        return iIndustryNewsCommentService.removeById(commentId) ? Result.success() : Result.error();
    }

}
