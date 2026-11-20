package com.sqx.modules.sourceGroupRole.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.modules.sourceGroupRole.entity.SourceGroupRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 群组用户关联管理 Mapper 接口
 * </p>
 *
 * @author wuChang
 * @since 2024-02-27
 */
@Mapper
public interface SourceGroupRoleDao extends BaseMapper<SourceGroupRole> {

    IPage<SourceGroupRole> getSourceGroupRoleList(@Param("pages") Page<SourceGroupRole> pages, @Param("sourceGroupRole") SourceGroupRole sourceGroupRole);
}
