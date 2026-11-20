package com.sqx.modules.verify.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sqx.modules.verify.db.entity.VerifyRecord;
import com.sqx.modules.verify.model.bo.VerifyRecordPageBO;
import com.sqx.modules.verify.model.bo.VerifyRecordSaveBO;
import com.sqx.modules.verify.model.vo.VerifyRecordVO;

/**
 * @author chengming
 * @since 2026/3/11
 */
public interface VerifyRecordService {

    VerifyRecord save(VerifyRecordSaveBO verifyRecordSaveBO);

    IPage<VerifyRecordVO> page(VerifyRecordPageBO verifyRecordPageBO);

    void delete(Long id);
}
