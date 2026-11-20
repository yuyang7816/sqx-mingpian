package com.sqx.modules.verify.db.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.modules.verify.db.entity.VerifyRecord;
import com.sqx.modules.verify.model.bo.VerifyRecordPageBO;
import com.sqx.modules.verify.model.vo.VerifyRecordVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author chengming
 * @since 2026/3/11
 */
@Mapper
public interface VerifyRecordMapper extends BaseMapper<VerifyRecord> {
    IPage<VerifyRecordVO> pageInfo(Page<VerifyRecordVO> initPage, @Param("param") VerifyRecordPageBO verifyRecordPageBO);
}
