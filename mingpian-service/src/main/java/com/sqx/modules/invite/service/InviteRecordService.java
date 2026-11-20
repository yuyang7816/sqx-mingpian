package com.sqx.modules.invite.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.Result;
import com.sqx.modules.invite.entity.InviteRecord;
import com.sqx.modules.invite.model.InviteRecordVo;
/**
 * 邀请记录表(InviteRecord)表服务接口
 *
 * @author makejava
 * @since 2024-05-13 18:04:08
 */
public interface InviteRecordService extends IService<InviteRecord>{

    int saveInfo(InviteRecord inviteRecord);

    Result myInvite(Long userId,Integer page, Integer limit, Long cardId);

    IPage<InviteRecordVo> myInviteRecord(Long userId, Integer pageNum, Integer pageSize);
}
