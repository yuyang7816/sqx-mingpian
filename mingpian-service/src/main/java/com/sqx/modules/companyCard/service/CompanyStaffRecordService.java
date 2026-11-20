package com.sqx.modules.companyCard.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.Result;
import com.sqx.modules.companyCard.domain.vo.StaffDataVo;
import com.sqx.modules.companyCard.entity.CompanyStaffRecord;
/**
 * 公司员工动态信息(CompanyStaffRecord)表服务接口
 *
 * @author makejava
 * @since 2024-05-09 13:56:13
 */
public interface CompanyStaffRecordService extends IService<CompanyStaffRecord>{

        Result applyJoin(Long usreId, JSONObject params);

        Result queryApplyRecord(JSONObject params);
        Result auditApply(JSONObject params);

    StaffDataVo staffData(Long cardId);

    Result pageInfo(Integer pageNum, Integer pageSize, Long cardId, Integer status);

    Integer applyCount(Long cardId);
}
