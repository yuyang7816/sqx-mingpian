package com.sqx.modules.invite.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.utils.Result;
import com.sqx.modules.invite.dao.InviteRecordDao;
import com.sqx.modules.invite.entity.InviteRecord;
import com.sqx.modules.invite.model.InviteRecordVo;
import com.sqx.modules.invite.service.InviteRecordService;
import com.sqx.modules.utils.uuid.IdUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * 邀请记录表(InviteRecord)表服务实现类
 *
 * @author makejava
 * @since 2024-05-13 18:04:08
 */
@Service("inviteRecordService")
public class InviteRecordServiceImpl extends ServiceImpl<InviteRecordDao, InviteRecord> implements InviteRecordService {
    @Resource
    private InviteRecordDao inviteRecordDao;

    @Override
    public int saveInfo(InviteRecord inviteRecord) {
        inviteRecord.setId(IdUtils.fastSimpleUUID());
        inviteRecord.setInviteTime(LocalDateTime.now());
        inviteRecord.setCreateTime(LocalDateTime.now());
        int insert = inviteRecordDao.insert(inviteRecord);
        return insert;
    }

    @Override
    public Result myInvite(Long userId, Integer page, Integer limit, Long cardId) {
        if (null == page || 0 == page) {
            page = 1;
        }

        if (null == limit || 0 == limit) {
            limit = 10;
        }

        Page<InviteRecord> initPage = new Page<>(page, limit);
        IPage<InviteRecord> inviteRecords = inviteRecordDao.myInvite(initPage, userId, cardId);
        return Result.success().put("data", inviteRecords);
    }

    @Override
    public IPage<InviteRecordVo> myInviteRecord(Long userId, Integer pageNum, Integer pageSize) {
        Page<InviteRecord> initPage = new Page<>(pageNum, pageSize);

        return this.baseMapper.myInviteRecord(initPage, userId);
    }
}
