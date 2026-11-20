package com.sqx.modules.companyDynamic.dao;

import com.sqx.modules.companyDynamic.entity.CompanyDynamicRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 企业动态表(CompanyDynamicRecord)表数据库访问层
 *
 * @author makejava
 * @since 2024-05-09 17:14:51
 */
 @Mapper
public interface CompanyDynamicRecordDao extends BaseMapper<CompanyDynamicRecord>{

  List<Map<String,Object>> queryByComCardId(@Param("comCardId")Long comCardId);
}

