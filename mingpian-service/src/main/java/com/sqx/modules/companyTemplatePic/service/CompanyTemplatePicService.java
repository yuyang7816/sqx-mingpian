package com.sqx.modules.companyTemplatePic.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sqx.modules.companyTemplatePic.domain.CompanyTemplatePic;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.modules.companyTemplatePic.domain.bo.CompanyTemplatePicPageBo;

/**
* @author ming
* @description 针对表【company_template_pic(模板背景图片)】的数据库操作Service
* @createDate 2025-12-16 10:09:59
*/
public interface CompanyTemplatePicService extends IService<CompanyTemplatePic> {

    void saveInfo(CompanyTemplatePic companyTemplatePic);

    void updateInfo(CompanyTemplatePic companyTemplatePic);

    IPage<CompanyTemplatePic> pageInfo(CompanyTemplatePicPageBo companyTemplatePicPageBo);

    void delete(Long id);
}
