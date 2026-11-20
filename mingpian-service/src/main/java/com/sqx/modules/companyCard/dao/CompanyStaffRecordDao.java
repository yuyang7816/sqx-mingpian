package com.sqx.modules.companyCard.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.modules.companyCard.domain.vo.CompanyStaffRecordVo;
import com.sqx.modules.companyCard.entity.CompanyStaffRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 公司员工动态信息(CompanyStaffRecord)表数据库访问层
 *
 * @author makejava
 * @since 2024-05-09 13:56:04
 */
 @Mapper
public interface CompanyStaffRecordDao extends BaseMapper<CompanyStaffRecord>{

  List<Map<String, Object>> queryApplyList(@Param("cardId")Long cardId);

    IPage<CompanyStaffRecordVo> pageInfo(Page<CompanyStaffRecord> initPage, @Param("cardId") Long cardId, @Param("status") Integer status);
}

