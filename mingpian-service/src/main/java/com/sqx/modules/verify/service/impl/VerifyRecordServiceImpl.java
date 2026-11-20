package com.sqx.modules.verify.service.impl;

import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.common.exception.SqxException;
import com.sqx.modules.businessCard.dao.BusinessCardDao;
import com.sqx.modules.businessCard.entity.BusinessCard;
import com.sqx.modules.common.service.CommonInfoService;
import com.sqx.modules.utils.DashscopeUtil;
import com.sqx.modules.verify.db.entity.VerifyRecord;
import com.sqx.modules.verify.db.service.VerifyRecordDbService;
import com.sqx.modules.verify.model.bo.VerifyRecordPageBO;
import com.sqx.modules.verify.model.bo.VerifyRecordSaveBO;
import com.sqx.modules.verify.model.vo.VerifyRecordVO;
import com.sqx.modules.verify.service.VerifyRecordService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * @author chengming
 * @since 2026/3/11
 */
@Service
public class VerifyRecordServiceImpl implements VerifyRecordService {

    @Resource
    private VerifyRecordDbService verifyRecordDbService;
    @Resource
    private BusinessCardDao businessCardDao;
    @Resource
    private CommonInfoService commonInfoService;

    @Override
    public VerifyRecord save(VerifyRecordSaveBO verifyRecordSaveBO) {
        Integer count = businessCardDao.selectCount(new LambdaQueryWrapper<BusinessCard>()
                .eq(BusinessCard::getCompanyName, verifyRecordSaveBO.getName())
        );
        if (0 >= count) {
            throw new SqxException("暂不支持查询此企业");
        }

        // 是否有人查过该企业
        List<VerifyRecord> verifyRecordList = verifyRecordDbService.list(new LambdaQueryWrapper<VerifyRecord>()
                .eq(VerifyRecord::getName, verifyRecordSaveBO.getName())
                .isNotNull(VerifyRecord::getResult)
        );
        if (CollectionUtils.isNotEmpty(verifyRecordList)) {
            // 当前用户是否查过该企业
            boolean match = verifyRecordList.stream()
                    .anyMatch(item -> Objects.equals(item.getUserId(), verifyRecordSaveBO.getUserId()));

            // 没查过就保存一份记录
            if (!match) {
                VerifyRecord verifyRecord = verifyRecordList.get(0);
                verifyRecord.setUserId(verifyRecordSaveBO.getUserId());
                verifyRecordDbService.save(verifyRecord);
            }

            return verifyRecordList.get(0);
        }

        VerifyRecord verifyRecord = new VerifyRecord();
        BeanUtils.copyProperties(verifyRecordSaveBO, verifyRecord);
        verifyRecord.setCreateTime(LocalDateTime.now());

        String apikey = commonInfoService.findOne(318).getValue();
        String model = commonInfoService.findOne(319).getValue();

        try {
            JSONObject res = DashscopeUtil.callCompanyInfo(apikey, model, verifyRecordSaveBO.getName());
            verifyRecord.setResult(res.toJSONString());
            verifyRecord.setVerifyTime(LocalDateTime.now());
        } catch (NoApiKeyException | InputRequiredException e) {
            throw new RuntimeException(e);
        }

        verifyRecordDbService.save(verifyRecord);

        return verifyRecord;
    }

    @Override
    public IPage<VerifyRecordVO> page(VerifyRecordPageBO verifyRecordPageBO) {
        if (Objects.isNull(verifyRecordPageBO.getPage()) || 0 == verifyRecordPageBO.getPage()) {
            verifyRecordPageBO.setPage(1);
        }

        if (Objects.isNull(verifyRecordPageBO.getLimit()) || 0 == verifyRecordPageBO.getLimit()) {
            verifyRecordPageBO.setLimit(10);
        }

        Page<VerifyRecordVO> initPage = new Page<>(verifyRecordPageBO.getPage(), verifyRecordPageBO.getLimit());

        return verifyRecordDbService.pageInfo(initPage, verifyRecordPageBO);
    }

    @Override
    public void delete(Long id) {
        verifyRecordDbService.removeById(id);
    }
}
