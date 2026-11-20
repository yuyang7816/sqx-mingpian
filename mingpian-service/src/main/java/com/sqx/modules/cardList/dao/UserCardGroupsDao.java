package com.sqx.modules.cardList.dao;

import com.sqx.modules.cardList.entity.UserCardGroups;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户名片分组表(UserCardGroups)表数据库访问层
 *
 * @author makejava
 * @since 2024-05-07 17:06:30
 */
 @Mapper
public interface UserCardGroupsDao extends BaseMapper<UserCardGroups>{

}

