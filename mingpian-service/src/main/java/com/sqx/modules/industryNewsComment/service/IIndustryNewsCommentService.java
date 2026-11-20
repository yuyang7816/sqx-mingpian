package com.sqx.modules.industryNewsComment.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.Result;
import com.sqx.modules.industryNewsComment.entity.IndustryNewsComment;

import java.util.List;

/**
 * 资讯评论 服务类
 * @author wuChang
 * @since 2023-09-26
 */

public interface IIndustryNewsCommentService extends IService<IndustryNewsComment> {
    /**
     * 新增或修改资讯评论
     */
    Result saveIndustryNewsComment (IndustryNewsComment industryNewsComment);


    IPage<IndustryNewsComment> getCommentList(Integer page, Integer limit, IndustryNewsComment industryNewsComment);

    List<IndustryNewsComment> getIndustryNewsCommentList(Long newsId);

    Result deleteIndustryNewsComment(Long userId, Long commentId);

    Result auditComment(Long commentId, Integer status, String remarks);
}
