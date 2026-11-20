package com.sqx.modules.companyTemplatePic.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.modules.companyTemplatePic.domain.CompanyTemplatePic;
import com.sqx.modules.companyTemplatePic.domain.bo.CompanyTemplatePicPageBo;
import com.sqx.modules.companyTemplatePic.service.CompanyTemplatePicService;
import com.sqx.modules.companyTemplatePic.mapper.CompanyTemplatePicMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

/**
* @author ming
* @description 针对表【company_template_pic(模板背景图片)】的数据库操作Service实现
* @createDate 2025-12-16 10:09:59
*/
@Service
public class CompanyTemplatePicServiceImpl extends ServiceImpl<CompanyTemplatePicMapper, CompanyTemplatePic>
    implements CompanyTemplatePicService{

    @Override
    public void saveInfo(CompanyTemplatePic companyTemplatePic) {
        companyTemplatePic.setCreateTime(LocalDateTime.now());
        this.save(companyTemplatePic);
    }

    @Override
    public void updateInfo(CompanyTemplatePic companyTemplatePic) {
        this.updateById(companyTemplatePic);
    }

    @Override
    public IPage<CompanyTemplatePic> pageInfo(CompanyTemplatePicPageBo companyTemplatePicPageBo) {
        Integer pageNum = companyTemplatePicPageBo.getPageNum();
        Integer pageSize = companyTemplatePicPageBo.getPageSize();

        if (Objects.isNull(companyTemplatePicPageBo.getPageNum()) || 0 == companyTemplatePicPageBo.getPageNum()) {
            pageNum = 1;
        }

        if (Objects.isNull(companyTemplatePicPageBo.getPageSize()) || 0 == companyTemplatePicPageBo.getPageSize()) {
            pageSize = 10;
        }

        Page<CompanyTemplatePic> initPage = new Page<>(pageNum, pageSize);

        IPage<CompanyTemplatePic> iPage = this.page(initPage, new LambdaQueryWrapper<CompanyTemplatePic>()
                .eq(Objects.nonNull(companyTemplatePicPageBo.getCompanyCardId()), CompanyTemplatePic::getCompanyCardId, companyTemplatePicPageBo.getCompanyCardId())
                .orderByDesc(CompanyTemplatePic::getCreateTime)
        );

        return iPage;
    }

    @Override
    public void delete(Long id) {
        this.removeById(id);
    }
}




