package com.sqx.modules.sourceGroup.service;

import com.sqx.modules.sourceGroup.entity.SourceGroup;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.Result;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * 群组管理 服务类
 * @author wuChang
 * @since 2024-02-27
 */

public interface SourceGroupService extends IService<SourceGroup> {
    /**
     * 新增或修改群组管理
     */
    Result saveSourceGroup (SourceGroup sourceGroup);

    /**
     * 获取改群组管理列表
     */
    IPage<SourceGroup> getSourceGroupList(Integer page, Integer limit, SourceGroup sourceGroup);

    Result deleteSourceGroupById(Long groupId);


    IPage<SourceGroup> getMySourceGroupList(Integer page, Integer limit, Long userId, String groupName);
}
