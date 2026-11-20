package com.sqx.modules.cardList.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.Result;
import com.sqx.modules.cardList.entity.UserCardGroups;
/**
 * 用户名片分组表(UserCardGroups)表服务接口
 *
 * @author makejava
 * @since 2024-05-07 17:06:30
 */
public interface UserCardGroupsService extends IService<UserCardGroups>{

    Result createGroups(UserCardGroups userCardGroups,Long userId);

    Result queryGroups(Long userId, Integer isDefault);

    Result editGroupInfo(UserCardGroups userCardGroups);

    Result delete(String id);
}
