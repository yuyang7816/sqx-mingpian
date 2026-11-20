package com.sqx.modules.companyCardTemplate.dao;

import com.sqx.modules.companyCardTemplate.entity.CompanyTemplate;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 公司名片模板(CompanyTemplate)表数据库访问层
 *
 * @author makejava
 * @since 2024-05-09 11:32:35
 */
 @Mapper
public interface CompanyTemplateDao extends BaseMapper<CompanyTemplate>{

}

