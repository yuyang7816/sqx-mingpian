package com.sqx.modules.companyDynamic.service;

import com.alibaba.fastjson.JSONObject;
import com.sqx.common.utils.Result;
import com.sqx.modules.companyDynamic.entity.CompanyDynamicRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import com.baomidou.mybatisplus.extension.service.IService;
/**
 * 企业动态表(CompanyDynamicRecord)表服务接口
 *
 * @author makejava
 * @since 2024-05-09 17:14:51
 */
public interface CompanyDynamicRecordService extends IService<CompanyDynamicRecord>{

    int saveRecord(Long comCardId,Long staffCardId,Integer type,String title,String content);


    Result queryByComCardId(JSONObject params);

}
