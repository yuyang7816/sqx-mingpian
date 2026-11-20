package com.sqx.modules.member.service;

import com.sqx.modules.member.entity.Member;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.Result;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
/**
 * 会员特权图标 服务类
 * @author wuChang
 * @since 2024-03-27
 */

public interface MemberService extends IService<Member> {
    /**
     * 新增或修改会员特权图标
     */
    Result saveMember (Member member);

    /**
     * 获取改会员特权图标列表
     */
    IPage<Member> getMemberList(Integer page, Integer limit, Member member);
}
