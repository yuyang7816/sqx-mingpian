package com.sqx.modules.businessCard.service;

import com.alibaba.fastjson.JSONObject;
import com.sqx.common.utils.Result;
import com.sqx.modules.businessCard.entity.VisitCardRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import com.baomidou.mybatisplus.extension.service.IService;
/**
 * 访问记录表(VisitCardRecord)表服务接口
 *
 * @author makejava
 * @since 2024-05-07 10:06:15
 */
public interface VisitCardRecordService extends IService<VisitCardRecord>{

    Result queryByPage(Long userId, JSONObject params);

    Result visitCard(Long userId,JSONObject params);

    Result visitUserData(Long userId);
    Result visitMe(Long userId,JSONObject params);
    Result myVisit(Long userId,JSONObject params);
}
