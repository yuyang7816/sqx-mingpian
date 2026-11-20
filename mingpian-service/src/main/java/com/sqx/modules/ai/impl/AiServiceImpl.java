package com.sqx.modules.ai.impl;

import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.alibaba.dashscope.exception.UploadFileException;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.internal.LinkedTreeMap;
import com.sqx.modules.ai.AiService;
import com.sqx.modules.common.service.CommonInfoService;
import com.sqx.modules.utils.DashscopeUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author chengming
 * @since 2026/3/10
 */

@Service
public class AiServiceImpl implements AiService {

    @Resource
    private CommonInfoService commonInfoService;

    @Override
    public JSONObject callCompanyInfo(String companyName) {
        String apikey = commonInfoService.findOne(318).getValue();
        String model = commonInfoService.findOne(319).getValue();

        try {
            return DashscopeUtil.callCompanyInfo(apikey, model, companyName);
        } catch (NoApiKeyException | InputRequiredException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public JSONObject extractCardInfo(String image) {
        String apikey = commonInfoService.findOne(318).getValue();

        try {
            LinkedTreeMap data = DashscopeUtil.extractCardInfo(apikey, image);
            String res = (String) data.get("processed_text");
            res = res.replace("```json", "").replace("```", "");
            return JSONObject.parseObject(res);
        } catch (NoApiKeyException | UploadFileException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String createInfo(String companyName) {
        String apikey = commonInfoService.findOne(318).getValue();
        String model = commonInfoService.findOne(319).getValue();

        try {
            return DashscopeUtil.createInfo(apikey, model, companyName);
        } catch (NoApiKeyException | InputRequiredException e) {
            throw new RuntimeException(e);
        }
    }
}
