package com.sqx.modules.industryNewsLike.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.modules.industryNews.entity.IndustryNews;
import com.sqx.modules.industryNewsLike.entity.IndustryNewsLike;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 资讯点赞 Mapper 接口
 * </p>
 *
 * @author wuChang
 * @since 2023-09-27
 */
@Mapper
public interface IndustryNewsLikeDao extends BaseMapper<IndustryNewsLike> {

    IPage<IndustryNewsLike> getNewsLikeList(@Param("pages") Page<IndustryNewsLike> pages, @Param("industryNewsLike") IndustryNewsLike industryNewsLike);

    IPage<IndustryNews> getMyLikeIndustryNewsList(@Param("pages") Page<IndustryNews> pages, @Param("userId") Long userId);

    int getCountByUserId(@Param("userId") Long userId);
}
