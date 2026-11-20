package com.sqx.modules.industryNews.service;

import com.sqx.modules.industryNews.entity.IndustryNews;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.Result;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sqx.modules.industryNews.vo.IndustryNewsVo;

/**
 * 行业资讯 服务类
 * @author wuChang
 * @since 2024-02-26
 */

public interface IndustryNewsService extends IService<IndustryNews> {
    /**
     * 新增或修改行业资讯
     */
    Result saveIndustryNews (IndustryNews industryNews);

    /**
     * 获取改行业资讯列表
     */
    IPage<IndustryNewsVo> getIndustryNewsList(Integer page, Integer limit, IndustryNewsVo industryNewsVo);

    Result adminSaveIndustryNews(IndustryNews industryNews);
    /**
     * 上架或下架行业资讯 （用户id和资讯id不可同时都传值）
     * @param userId 用户id 传用户id将当前用户全部行业下架或上架
     * @param newsId 资讯id 传资讯id值将当前数据下架或上架
     * @param type 1上架 3会员到期已下架
     */
    void downOrUp(Long userId,Long newsId, Integer type);

    IndustryNews getIndustryNewsById(Long loginUserId, Long newsId);

    Result deleteIndustryNewsById(Long userId, Long newsId);

    Result examineNews(Long newsId, Integer status, String examineRemark);

    IPage<IndustryNews> getMyLikeIndustryList(Integer page, Integer limit, Long userId);

    Result upOrDownNews(Long newsId, String downRemark);
}
