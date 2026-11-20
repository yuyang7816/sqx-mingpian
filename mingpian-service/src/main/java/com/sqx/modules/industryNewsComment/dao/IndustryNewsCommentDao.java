package com.sqx.modules.industryNewsComment.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.modules.industryNewsComment.entity.IndustryNewsComment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 资讯评论 Mapper 接口
 * </p>
 *
 * @author wuChang
 * @since 2023-09-26
 */
@Mapper
public interface IndustryNewsCommentDao extends BaseMapper<IndustryNewsComment> {

    List<IndustryNewsComment> getIndustryNewsCommentList(@Param("newsId") Long newsId);

    IPage<IndustryNewsComment> getCommentList(@Param("pages") Page<IndustryNewsComment> pages, @Param("industryNewsComment") IndustryNewsComment industryNewsComment);
}
