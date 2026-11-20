package com.sqx.modules.verify.db.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.modules.verify.db.entity.VerifyRecord;
import com.sqx.modules.verify.db.mapper.VerifyRecordMapper;
import com.sqx.modules.verify.db.service.VerifyRecordDbService;
import com.sqx.modules.verify.model.bo.VerifyRecordPageBO;
import com.sqx.modules.verify.model.vo.VerifyRecordVO;
import org.springframework.stereotype.Service;

/**
 * @author chengming
 * @since 2026/3/11
 */
@Service
public class VerifyRecordDbServiceImpl extends ServiceImpl<VerifyRecordMapper, VerifyRecord> implements VerifyRecordDbService {

    @Override
    public IPage<VerifyRecordVO> pageInfo(Page<VerifyRecordVO> initPage, VerifyRecordPageBO verifyRecordPageBO) {
        return this.baseMapper.pageInfo(initPage, verifyRecordPageBO);
    }
}
