package com.sqx.modules.businessCard.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.Result;
import com.sqx.modules.businessCard.entity.CardStyle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

/**
 * 名片样式(CardStyle)表服务接口
 *
 * @author makejava
 * @since 2024-05-06 17:17:18
 */
public interface CardStyleService extends IService<CardStyle> {

    Result queryByPage(JSONObject params);

    Result saveInfo(CardStyle cardStyle);
    Result editInfo(CardStyle cardStyle);

    Result queryById(String id);
    Result delById(String id);

    Result appList(JSONObject params);

    CardStyle queryInfoByStyleCode(String styleCode);
}
