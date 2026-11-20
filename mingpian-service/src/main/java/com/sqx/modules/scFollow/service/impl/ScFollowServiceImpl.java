package com.sqx.modules.scFollow.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.utils.Result;
import com.sqx.modules.scFollow.dao.ScFollowDao;
import com.sqx.modules.scFollow.entity.ScFollow;
import com.sqx.modules.scFollow.service.ScFollowService;
import com.sqx.modules.scFollow.vo.ScFollowVo;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
public class ScFollowServiceImpl extends ServiceImpl<ScFollowDao, ScFollow> implements ScFollowService {


    @Override
    public IPage<ScFollowVo> getMyFollowList(Integer page, Integer limit, Long userId, Double lng, Double lat) {
        Page<ScFollowVo> pages;
        if (page != null && limit != null) {
            pages = new Page<>(page, limit);
        } else {
            pages = new Page<>();
        }
        return baseMapper.getMyFollowList(pages, userId,lng,lat);


    }

    @Override
    public Result updateFollow(ScFollow follow) {
        Integer count = baseMapper.selectCount(new QueryWrapper<ScFollow>().eq("user_id", follow.getUserId()).eq("demand_id", follow.getDemandId()));
        if (count>0){
            baseMapper.delete(new QueryWrapper<ScFollow>().eq("user_id", follow.getUserId()).eq("demand_id", follow.getDemandId()));
            return Result.success("取消收藏成功");
        }else {
            follow.setCreateTime(LocalDateTime.now());
            baseMapper.insert(follow);
            return Result.success("收藏成功");
        }

    }


}
