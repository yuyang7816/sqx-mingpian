package com.sqx.modules.cardList.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sqx.common.utils.Result;
import com.sqx.modules.cardList.entity.UserCardGroups;
import com.sqx.modules.cardList.dao.UserCardGroupsDao;
import com.sqx.modules.cardList.service.UserCardGroupsService;
import com.sqx.modules.utils.uuid.IdUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * 用户名片分组表(UserCardGroups)表服务实现类
 *
 * @author makejava
 * @since 2024-05-07 17:06:30
 */
@Service("userCardGroupsService")
public class UserCardGroupsServiceImpl extends ServiceImpl<UserCardGroupsDao, UserCardGroups> implements UserCardGroupsService {
    @Resource
    private UserCardGroupsDao userCardGroupsDao;

    @Override
    public Result createGroups(UserCardGroups userCardGroups,Long userId) {
        userCardGroups.setId(IdUtils.fastSimpleUUID());
        userCardGroups.setUserId(userId);
        userCardGroups.setCreateTime(LocalDateTime.now());
        userCardGroupsDao.insert(userCardGroups);
        return Result.success();
    }

    @Override
    public Result queryGroups(Long userId, Integer isDefault) {
        List<UserCardGroups> groupsList = userCardGroupsDao.selectList(new QueryWrapper<UserCardGroups>()
                .eq("user_id", userId)
                .eq(Objects.nonNull(isDefault), "is_default", isDefault)
        );
        return Result.success().put("data",groupsList);
    }

    @Override
    public Result editGroupInfo(UserCardGroups userCardGroups) {
        int i = userCardGroupsDao.updateById(userCardGroups);
        return Result.upStatus(i);
    }

    @Override
    public Result delete(String id) {
        userCardGroupsDao.deleteById(id);
        return Result.success();
    }
}
