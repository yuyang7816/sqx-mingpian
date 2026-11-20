package com.sqx.modules.industryNews.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.modules.industryNews.entity.IndustryNews;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sqx.modules.industryNews.vo.IndustryNewsVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 行业资讯 Mapper 接口
 * </p>
 *
 * @author wuChang
 * @since 2024-02-26
 */
@Mapper
public interface IndustryNewsDao extends BaseMapper<IndustryNews> {



    int getCountByDate(@Param("cycle") Integer cycle, @Param("userId") Long userId);


    IPage<IndustryNews> getMyLikeIndustryList(@Param("pages") Page<IndustryNews> pages, @Param("userId") Long userId);

    IndustryNewsVo getIndustryNewsById(@Param("loginUserId") Long loginUserId, @Param("newsId") Long newsId);

    IPage<IndustryNewsVo> getIndustryNewsList(@Param("pages") Page<IndustryNewsVo> pages, @Param("industryNews") IndustryNewsVo industryNews);
}
