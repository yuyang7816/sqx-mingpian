package com.sqx.modules.ai;

import com.alibaba.fastjson.JSONObject;
import com.sqx.common.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author chengming
 * @since 2026/3/10
 */
@Api(tags = "AI")
@RestController
@RequestMapping("app/ai")
public class AiController {

    @Resource
    private AiService aiService;

    @ApiModelProperty("获取公司信息")
    @GetMapping("callCompanyInfo")
    public Result callCompanyInfo(String companyName) {
        JSONObject data = aiService.callCompanyInfo(companyName);
        return Result.success().put("data", data);
    }

    @ApiModelProperty("提取名片信息")
    @GetMapping("extractCardInfo")
    public Result extractCardInfo(@RequestParam("image") String image) {
        JSONObject data = aiService.extractCardInfo(image);
        return Result.success().put("data", data);
    }

    @ApiModelProperty("生成公司介绍")
    @GetMapping("createInfo")
    public Result createInfo (String companyName) {
        String data = aiService.createInfo(companyName);
        return Result.success().put("data", data);
    }
}
