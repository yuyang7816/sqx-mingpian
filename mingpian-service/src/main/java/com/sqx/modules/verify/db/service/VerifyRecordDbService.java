package com.sqx.modules.verify.db.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.modules.verify.db.entity.VerifyRecord;
import com.sqx.modules.verify.model.bo.VerifyRecordPageBO;
import com.sqx.modules.verify.model.vo.VerifyRecordVO;

/**
 * @author chengming
 * @since 2026/3/11
 */
public interface VerifyRecordDbService extends IService<VerifyRecord> {
    IPage<VerifyRecordVO> pageInfo(Page<VerifyRecordVO> initPage, VerifyRecordPageBO verifyRecordPageBO);
}
