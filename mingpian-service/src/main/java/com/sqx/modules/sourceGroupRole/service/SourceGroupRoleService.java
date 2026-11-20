package com.sqx.modules.sourceGroupRole.service;

import com.sqx.modules.sourceGroupRole.entity.SourceGroupRole;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.Result;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
/**
 * 群组用户关联管理 服务类
 * @author wuChang
 * @since 2024-02-27
 */

public interface SourceGroupRoleService extends IService<SourceGroupRole> {
    /**
     * 新增或修改群组用户关联管理
     */
    Result saveSourceGroupRole (SourceGroupRole sourceGroupRole);

    /**
     * 获取改群组用户关联管理列表
     */
    IPage<SourceGroupRole> getSourceGroupRoleList(Integer page, Integer limit, SourceGroupRole sourceGroupRole);

}
