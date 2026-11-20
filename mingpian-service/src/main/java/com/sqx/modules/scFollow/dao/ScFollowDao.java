package com.sqx.modules.scFollow.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.modules.scFollow.entity.ScFollow;
import com.sqx.modules.scFollow.vo.ScFollowVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ScFollowDao extends BaseMapper<ScFollow> {


    IPage<ScFollowVo> getMyFollowList(@Param("pages") Page<ScFollowVo> pages, @Param("userId") Long userId, @Param("lng") Double lng, @Param("lat") Double lat);

}
