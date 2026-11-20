package com.sqx.modules.industryNewsLike.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.dao.UserDao;
import com.sqx.modules.app.entity.UserEntity;
import com.sqx.modules.app.service.UserService;
import com.sqx.modules.industryNews.entity.IndustryNews;
import com.sqx.modules.industryNews.service.IndustryNewsService;
import com.sqx.modules.industryNewsLike.dao.IndustryNewsLikeDao;
import com.sqx.modules.industryNewsLike.entity.IndustryNewsLike;
import com.sqx.modules.industryNewsLike.service.IIndustryNewsLikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 资讯点赞 服务实现类
 *
 * @author wuChang
 * @since 2023-09-27
 */
@Service
public class IndustryNewsLikeServiceImpl extends ServiceImpl<IndustryNewsLikeDao, IndustryNewsLike> implements IIndustryNewsLikeService {
    @Autowired
    private IndustryNewsService industryNewsService;
    @Autowired
    private UserDao userDao;
    @Override
    public Result saveIndustryNewsLike(IndustryNewsLike industryNewsLike) {
        IndustryNews industryNews = industryNewsService.getById(industryNewsLike.getNewsId());
        if (industryNews == null) {
            return Result.error("行业资讯不存在");
        }
        IndustryNewsLike selectOne = baseMapper.selectOne(new QueryWrapper<>(industryNewsLike));
        if (selectOne == null) {
            industryNewsLike.setCreateTime(LocalDateTime.now());

            return Result.upStatus(baseMapper.insert(industryNewsLike));
        } else {
            return Result.upStatus(baseMapper.delete(new QueryWrapper<>(industryNewsLike)));
        }

    }

    @Override
    public IPage<IndustryNewsLike> getIndustryNewsLikeList(Integer page, Integer limit, IndustryNewsLike industryNewsLike) {
        Page<IndustryNewsLike> pages;
        if (page != null && limit != null) {
            pages = new Page<>(page, limit);
        } else {
            pages = new Page<>();
            pages.setSize(-1);
        }
        return baseMapper.getNewsLikeList(pages,industryNewsLike);

    }

    @Override
    public IPage<IndustryNews> getMyLikeIndustryNewsList(Long userId, Integer page, Integer limit) {
        Page<IndustryNews> pages;
        if (page != null && limit != null) {
            pages = new Page<>(page, limit);
        } else {
            pages = new Page<>();
            pages.setSize(-1);
        }
        return baseMapper.getMyLikeIndustryNewsList(pages,userId);

    }

    @Override
    public int getCountByUserId(Long userId) {

        return baseMapper.getCountByUserId(userId);

    }

    @Override
    public IPage<UserEntity> getLikeUserList(Long newsId, Integer page, Integer limit) {
        Page<UserEntity> pages;
        if (page != null && limit != null) {
            pages = new Page<>(page, limit);
        } else {
            pages = new Page<>();
            pages.setSize(-1);
        }

        return userDao.getLikeUserList(pages,newsId);
    }

}
