package com.sqx.modules.scBrowse.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.Result;
import com.sqx.modules.businessCard.entity.BusinessCard;
import com.sqx.modules.scBrowse.BrowseVo;
import com.sqx.modules.scBrowse.entity.ScBrowse;

public interface ScBrowseService extends IService<ScBrowse> {

    IPage<BusinessCard> getMyBrowse(Long userId, Integer page, Integer limit, Integer type);


    Result selectAmount(Long userId);

    IPage<BrowseVo> getMyBrowseList(Long userId, Integer page, Integer limit, Double lng, Double lat);

    Object browseMeList(Long userId, Integer page, Integer limit, Double lng, Double lat);
}
