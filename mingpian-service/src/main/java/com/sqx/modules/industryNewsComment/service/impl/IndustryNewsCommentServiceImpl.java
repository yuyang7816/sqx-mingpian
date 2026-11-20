package com.sqx.modules.industryNewsComment.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.utils.Result;
import com.sqx.modules.industryNews.entity.IndustryNews;
import com.sqx.modules.industryNews.service.IndustryNewsService;
import com.sqx.modules.industryNewsComment.dao.IndustryNewsCommentDao;
import com.sqx.modules.industryNewsComment.entity.IndustryNewsComment;
import com.sqx.modules.industryNewsComment.service.IIndustryNewsCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 资讯评论 服务实现类
 *
 * @author wuChang
 * @since 2023-09-26
 */
@Service
public class IndustryNewsCommentServiceImpl extends ServiceImpl<IndustryNewsCommentDao, IndustryNewsComment> implements IIndustryNewsCommentService {
    @Autowired
    private IndustryNewsService industryNewsService;


    @Override
    public Result saveIndustryNewsComment(IndustryNewsComment industryNewsComment) {
        IndustryNews industryNews = industryNewsService.getById(industryNewsComment.getNewsId());
        if (industryNews == null || industryNews.getStatus() != 1) {
            return Result.error("资讯已删除");
        }
        if (industryNewsComment.getCommentId() != null) {
            return Result.upStatus(baseMapper.updateById(industryNewsComment));
        } else {
            industryNewsComment.setCreateTime(LocalDateTime.now());
            return Result.upStatus(baseMapper.insert(industryNewsComment));
        }
    }

    @Override
    public IPage<IndustryNewsComment> getCommentList(Integer page, Integer limit, IndustryNewsComment industryNewsComment) {

        Page<IndustryNewsComment> pages;
        if (page != null && limit != null) {
            pages = new Page<>(page, limit);
        } else {
            pages = new Page<>();
            pages.setSize(-1);
        }
        return baseMapper.getCommentList(pages, industryNewsComment);
    }

    @Override
    public List<IndustryNewsComment> getIndustryNewsCommentList(Long newsId) {

        return baseMapper.getIndustryNewsCommentList(newsId);
    }

    @Override
    public Result deleteIndustryNewsComment(Long userId, Long commentId) {
        IndustryNewsComment industryNewsComment = baseMapper.selectById(commentId);

        if (industryNewsComment.getUserId().equals(userId)) {

            return Result.upStatus(baseMapper.deleteById(commentId));
        } else {
            return Result.error("非法操作");
        }


    }

    @Override
    public Result auditComment(Long commentId, Integer status, String remarks) {
        IndustryNewsComment industryNewsComment = baseMapper.selectById(commentId);
        if (industryNewsComment == null) {
            return Result.error("审核的评论不存在");
        }

        industryNewsComment.setRemarks(remarks);
        if (status == 1) {
            industryNewsComment.setStatus(1);
        } else {
            industryNewsComment.setStatus(2);
        }

        return Result.upStatus(baseMapper.updateById(industryNewsComment));

    }


}
