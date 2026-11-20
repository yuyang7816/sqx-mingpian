package com.sqx.modules.industryNewsLike.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.entity.UserEntity;
import com.sqx.modules.industryNews.entity.IndustryNews;
import com.sqx.modules.industryNewsLike.entity.IndustryNewsLike;

/**
 * 资讯点赞 服务类
 * @author wuChang
 * @since 2023-09-27
 */

public interface IIndustryNewsLikeService extends IService<IndustryNewsLike> {
    /**
     * 新增或修改资讯点赞
     */
    Result saveIndustryNewsLike (IndustryNewsLike industryNewsLike);


    IPage<IndustryNewsLike> getIndustryNewsLikeList(Integer page, Integer limit, IndustryNewsLike industryNewsLike);

    IPage<IndustryNews> getMyLikeIndustryNewsList(Long userId, Integer page, Integer limit);

    int getCountByUserId(Long userId);

    IPage<UserEntity> getLikeUserList(Long newsId, Integer page, Integer limit);
}
