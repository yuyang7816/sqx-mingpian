package com.sqx.modules.invite.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.modules.invite.entity.InviteRecord;
import com.sqx.modules.invite.model.InviteRecordVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 邀请记录表(InviteRecord)表数据库访问层
 *
 * @author makejava
 * @since 2024-05-13 18:04:08
 */
 @Mapper
public interface InviteRecordDao extends BaseMapper<InviteRecord>{

    IPage<InviteRecord> myInvite(Page<InviteRecord> initPage, @Param("userId")Long userId,@Param("cardId")Long cardId);

    IPage<InviteRecordVo> myInviteRecord(Page<InviteRecord> initPage, @Param("userId") Long userId);
}

