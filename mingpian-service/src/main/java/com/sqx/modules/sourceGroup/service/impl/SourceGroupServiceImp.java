package com.sqx.modules.sourceGroup.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sqx.modules.app.entity.UserEntity;
import com.sqx.modules.app.service.UserService;
import com.sqx.modules.sourceGroup.entity.SourceGroup;
import com.sqx.modules.sourceGroup.dao.SourceGroupDao;
import com.sqx.modules.sourceGroup.service.SourceGroupService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.modules.sourceGroupRole.entity.SourceGroupRole;
import com.sqx.modules.sourceGroupRole.service.SourceGroupRoleService;
import com.sqx.modules.sys.entity.SysUserEntity;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Service;
import com.sqx.common.utils.Result;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 群组管理 服务实现类
 * @author wuChang
 * @since 2024-02-27
 */
@Service
public class SourceGroupServiceImp extends ServiceImpl<SourceGroupDao, SourceGroup> implements SourceGroupService {
    @Autowired
    private SourceGroupRoleService groupRoleService;
    @Autowired
    private UserService userService;
    private ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock(true);


    @Override
    public Result saveSourceGroup(SourceGroup sourceGroup) {
       SysUserEntity sysUserEntity= (SysUserEntity)SecurityUtils.getSubject().getPrincipal();
        sourceGroup.setSysUserId(sysUserEntity.getUserId());
        sourceGroup.setSysUserName(sysUserEntity.getUsername());
        if (sourceGroup.getGroupId() != null) {
            return Result.upStatus(baseMapper.updateById(sourceGroup));
        } else {
            sourceGroup.setCreateTime(LocalDateTime.now());
            return Result.upStatus(baseMapper.insert(sourceGroup));
        }
    }

    @Override
    public IPage<SourceGroup> getSourceGroupList(Integer page, Integer limit, SourceGroup sourceGroup) {
        Page<SourceGroup> pages;
        if (page != null && limit != null) {
            pages = new Page<>(page, limit);
        } else {
            pages = new Page<>();
            pages.setSize(-1);
        }
        return baseMapper.selectPage(pages, new QueryWrapper<>(sourceGroup).orderByAsc("sort"));
    }

    @Override
    public Result deleteSourceGroupById(Long groupId) {
        groupRoleService.remove(new QueryWrapper<SourceGroupRole>().eq("group_id", groupId));
        return Result.upStatus(baseMapper.deleteById(groupId));
    }



    @Override
    public IPage<SourceGroup> getMySourceGroupList(Integer page, Integer limit, Long userId, String groupName) {
        Page<SourceGroup> pages;
        if (page != null && limit != null) {
            pages = new Page<>(page, limit);
        } else {
            pages = new Page<>();
            pages.setSize(-1);
        }
        return baseMapper.getMySourceGroupList(pages,userId,groupName);

    }
}
