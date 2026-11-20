package com.sqx.modules.scBrowse.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.modules.businessCard.entity.BusinessCard;
import com.sqx.modules.scBrowse.BrowseVo;
import com.sqx.modules.scBrowse.domain.ScBrowseVo;
import com.sqx.modules.scBrowse.entity.ScBrowse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ScBrowseDao extends BaseMapper<ScBrowse>
{
    IPage<BrowseVo> getMyBrowseList(@Param("pages") Page<ScBrowse> pages, @Param("userId") Long userId, @Param("lng") Double lng, @Param("lat") Double lat);

    int getBrowseCount(@Param("userId") Long userId);

    IPage<ScBrowseVo> browseMeList(Page<ScBrowse> pages, @Param("userId") Long userId);

    IPage<BusinessCard> getMyBrowse(Page<ScBrowse> pages, Long userId);
}
