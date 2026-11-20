package com.sqx.modules.sourceGroupRole.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sqx.modules.sourceGroup.service.SourceGroupService;
import com.sqx.modules.sourceGroupRole.entity.SourceGroupRole;
import com.sqx.modules.sourceGroupRole.dao.SourceGroupRoleDao;
import com.sqx.modules.sourceGroupRole.service.SourceGroupRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.sqx.common.utils.Result;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.time.LocalDateTime;

/**
 * 群组用户关联管理 服务实现类
 * @author wuChang
 * @since 2024-02-27
 */
@Service
public class SourceGroupRoleServiceImp extends ServiceImpl<SourceGroupRoleDao, SourceGroupRole> implements SourceGroupRoleService {
    @Autowired
    private SourceGroupService sourceGroupService;

    @Override
    public Result saveSourceGroupRole(SourceGroupRole sourceGroupRole) {
        if (sourceGroupRole.getRoleId() != null) {
            return Result.upStatus(baseMapper.updateById(sourceGroupRole));
        } else {
            sourceGroupRole.setCreateTime(LocalDateTime.now());
            return Result.upStatus(baseMapper.insert(sourceGroupRole));
        }
    }

    @Override
    public IPage<SourceGroupRole> getSourceGroupRoleList(Integer page, Integer limit, SourceGroupRole sourceGroupRole) {
        Page<SourceGroupRole> pages;
        if (page != null && limit != null) {
            pages = new Page<>(page, limit);
        } else {
            pages = new Page<>();
            pages.setSize(-1);
        }
        return baseMapper.getSourceGroupRoleList(pages, sourceGroupRole);
    }


}
