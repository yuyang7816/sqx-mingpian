package com.sqx.modules.companyCardTemplate.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.Result;
import com.sqx.modules.companyCardTemplate.entity.CompanyTemplate;
/**
 * 公司名片模板(CompanyTemplate)表服务接口
 *
 * @author makejava
 * @since 2024-05-09 11:32:35
 */
public interface CompanyTemplateService extends IService<CompanyTemplate>{

    Result saveCompanyTemp(CompanyTemplate companyTemplate);

    Result queryTemplate(JSONObject params);

    Result updateTemplate(CompanyTemplate companyTemplate);

    Result info(Long companyCardId);

    Result saveTemplate(CompanyTemplate companyTemplate);
}
