package com.sqx.modules.invite.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sqx.common.utils.PageUtils;
import com.sqx.modules.invite.model.InvitePageVo;

import java.util.Date;

public interface InviteService {

    PageUtils selectInviteList(int page, int limit, Integer state, Long userId);

    Integer selectInviteCount(Integer state,Long userId);

    Double selectInviteSum(Integer state,Long userId);



    Integer selectInviteByUserIdCountNotTime(Long userId);

    Integer selectInviteByUserIdCount(Long userId, Date startTime, Date endTime);

    Double selectInviteByUserIdSum(Long userId, Date startTime,Date endTime);

    Double sumInviteMoney(String time,Integer flag);

    PageUtils inviteAnalysis(int page,int limit, String time, Integer flag);


    IPage<InvitePageVo> invitePage(Long userId, Integer page, Integer limit);
}
