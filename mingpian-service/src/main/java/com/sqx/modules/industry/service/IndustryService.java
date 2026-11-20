package com.sqx.modules.industry.service;

import com.sqx.modules.industry.entity.Industry;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.Result;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

/**
 * 行业分类 服务类
 * @author wuChang
 * @since 2024-02-26
 */

public interface IndustryService extends IService<Industry> {
    /**
     * 新增或修改行业分类
     */
    Result saveIndustry (Industry industry);

    /**
     * 获取改行业分类列表
     */
    IPage<Industry> getIndustryList(Integer page, Integer limit, Industry industry);

    Result deleteIndustryById(Long industryId);

    List<Industry> getIndustryTreeList();
}
