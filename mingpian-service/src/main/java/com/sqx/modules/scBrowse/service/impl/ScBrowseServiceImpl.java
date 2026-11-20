package com.sqx.modules.scBrowse.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.utils.Result;
import com.sqx.modules.businessCard.entity.BusinessCard;
import com.sqx.modules.industryNews.entity.IndustryNews;
import com.sqx.modules.industryNews.service.IndustryNewsService;
import com.sqx.modules.industryNewsLike.service.IIndustryNewsLikeService;
import com.sqx.modules.scBrowse.BrowseVo;
import com.sqx.modules.scBrowse.dao.ScBrowseDao;
import com.sqx.modules.scBrowse.domain.ScBrowseVo;
import com.sqx.modules.scBrowse.entity.ScBrowse;
import com.sqx.modules.scBrowse.service.ScBrowseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ScBrowseServiceImpl extends ServiceImpl<ScBrowseDao, ScBrowse> implements ScBrowseService {
    @Autowired
    private ScBrowseDao browseDao;
    @Autowired
    private IndustryNewsService industryNewsService;
    @Autowired
    private IIndustryNewsLikeService industryNewsLikeService;

    @Override
    public IPage<BusinessCard> getMyBrowse(Long userId, Integer page, Integer limit, Integer type) {
        Page<ScBrowse> pages;
        if (page != null && limit != null) {
            pages = new Page<>(page, limit);
        } else {
            pages = new Page<>();
        }

        IPage<BusinessCard> voIPage = browseDao.getMyBrowse(pages, userId);
        return voIPage;
    }

    @Override
    public Result selectAmount(Long userId) {

        Map<String, Object> map = new HashMap<>();
        //我的发布
        int industryCount = industryNewsService.count(new QueryWrapper<IndustryNews>().eq("user_id", userId));
        //我的收藏
        //我的点赞
        int industryNewsLikeCount = industryNewsLikeService.getCountByUserId(userId);
        //我联系过的
//        int procureDemandCount = procureDemandRoleService.getCountByUserId(userId);
        int procureDemandCount = browseDao.getBrowseCount(userId);
        map.put("procureDemandCount", procureDemandCount);
        map.put("industryNewsLikeCount", industryNewsLikeCount);
        map.put("industryCount", industryCount);
        return Result.success().put("data", map);


    }

    @Override
    public IPage<BrowseVo> getMyBrowseList(Long userId, Integer page, Integer limit, Double lng, Double lat) {

        Page<ScBrowse> pages;
        if (page != null && limit != null) {
            pages = new Page<>(page, limit);
        } else {
            pages = new Page<>();
        }

        return baseMapper.getMyBrowseList(pages,userId,lng,lat);

    }

    @Override
    public Object browseMeList(Long userId, Integer page, Integer limit, Double lng, Double lat) {
        Page<ScBrowse> pages;
        if (page != null && limit != null) {
            pages = new Page<>(page, limit);
        } else {
            pages = new Page<>();
        }

        return this.baseMapper.browseMeList(pages, userId);
    }


}
