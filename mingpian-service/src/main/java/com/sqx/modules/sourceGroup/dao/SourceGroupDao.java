package com.sqx.modules.sourceGroup.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.modules.sourceGroup.entity.SourceGroup;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 群组管理 Mapper 接口
 * </p>
 *
 * @author wuChang
 * @since 2024-02-27
 */
@Mapper
public interface SourceGroupDao extends BaseMapper<SourceGroup> {

    IPage<SourceGroup> getMySourceGroupList(@Param("pages") Page<SourceGroup> pages, @Param("userId") Long userId, @Param("groupName") String groupName);
}
