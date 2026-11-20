package com.sqx.modules.member.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sqx.modules.member.entity.Member;
import com.sqx.modules.member.dao.MemberDao;
import com.sqx.modules.member.service.MemberService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.sqx.common.utils.Result;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.time.LocalDateTime;

/**
 * 会员特权图标 服务实现类
 * @author wuChang
 * @since 2024-03-27
 */
@Service
public class MemberServiceImp extends ServiceImpl<MemberDao, Member> implements MemberService {


    @Override
    public Result saveMember(Member member) {
        if (member.getMemberId() != null) {
            return Result.upStatus(baseMapper.updateById(member));
        } else {
            return Result.upStatus(baseMapper.insert(member));
        }
    }

    @Override
    public IPage<Member> getMemberList(Integer page, Integer limit, Member member) {
        Page<Member> pages;
        if (page != null && limit != null) {
            pages = new Page<>(page, limit);
        } else {
            pages = new Page<>();
            pages.setSize(-1);
        }
        return baseMapper.selectPage(pages, new QueryWrapper<>(member).orderByAsc("sort"));
    }
}
