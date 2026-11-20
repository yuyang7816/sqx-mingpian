package com.sqx.modules.companyDynamic.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sqx.common.utils.Result;
import com.sqx.modules.companyDynamic.entity.CompanyDynamicRecord;
import com.sqx.modules.companyDynamic.dao.CompanyDynamicRecordDao;
import com.sqx.modules.companyDynamic.service.CompanyDynamicRecordService;
import com.sqx.modules.utils.text.StringUtils;
import com.sqx.modules.utils.uuid.IdUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 企业动态表(CompanyDynamicRecord)表服务实现类
 *
 * @author makejava
 * @since 2024-05-09 17:14:51
 */
@Service("companyDynamicRecordService")
public class CompanyDynamicRecordServiceImpl extends ServiceImpl<CompanyDynamicRecordDao, CompanyDynamicRecord> implements CompanyDynamicRecordService {
    @Resource
    private CompanyDynamicRecordDao companyDynamicRecordDao;

    @Override
    public int saveRecord(Long comCardId,Long staffCardId,Integer type,String title,String content) {
        CompanyDynamicRecord companyDynamicRecord = new CompanyDynamicRecord();
        companyDynamicRecord.setId(IdUtils.fastSimpleUUID());
        companyDynamicRecord.setCompanyCardId(comCardId);
        companyDynamicRecord.setStaffCardId(staffCardId);
        companyDynamicRecord.setDynamicType(type);
        companyDynamicRecord.setCreateTime(LocalDateTime.now());
        companyDynamicRecord.setTitle(title);
        companyDynamicRecord.setContent(content);
        int insert = companyDynamicRecordDao.insert(companyDynamicRecord);
        return insert;
    }

    @Override
    public Result queryByComCardId(JSONObject params) {
        Integer pageNum = params.getInteger("pageNum");
        Integer pageSize = params.getInteger("pageSize");
        if (StringUtils.isNull(pageNum)){
            return Result.error("pageNum不可以为空！");
        }
        if (StringUtils.isNull(pageSize)){
            return Result.error("pageSize不可以为空！");
        }
        PageHelper.startPage(pageNum,pageSize);
        List<Map<String, Object>> maps = companyDynamicRecordDao.queryByComCardId(params.getLong("comCardId"));
        PageInfo<Map<String, Object>> mapPageInfo = new PageInfo<>(maps);
        return Result.success().put("data",mapPageInfo);
    }
}
