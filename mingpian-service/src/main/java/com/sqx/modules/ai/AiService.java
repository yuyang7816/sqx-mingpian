package com.sqx.modules.ai;

import com.alibaba.fastjson.JSONObject;

/**
 * @author chengming
 * @since 2026/3/10
 */
public interface AiService {
    JSONObject callCompanyInfo(String companyName);

    JSONObject extractCardInfo(String image);

    String createInfo(String companyName);
}
