package com.sqx.modules.utils;

import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationOutput;
import com.alibaba.dashscope.aigc.generation.GenerationParam;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.aigc.multimodalconversation.MultiModalConversation;
import com.alibaba.dashscope.aigc.multimodalconversation.MultiModalConversationParam;
import com.alibaba.dashscope.aigc.multimodalconversation.MultiModalConversationResult;
import com.alibaba.dashscope.aigc.multimodalconversation.OcrOptions;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.MultiModalMessage;
import com.alibaba.dashscope.common.ResponseFormat;
import com.alibaba.dashscope.common.Role;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.alibaba.dashscope.exception.UploadFileException;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import com.google.gson.internal.LinkedTreeMap;

import java.util.*;

/**
 * @author chengming
 * @since 2026/3/10
 *
 * dashscope 工具类
 */
public class DashscopeUtil {

    private static GenerationResult callWithMessage(String apiKey, String model, String sysMessage, String userMessage, String responseFormat) throws ApiException, NoApiKeyException, InputRequiredException {
        Generation gen = new Generation();

        Message systemMsg = Message.builder()
                .role(Role.SYSTEM.getValue())
                .content(sysMessage)
                .build();

        Message userMsg = Message.builder()
                .role(Role.USER.getValue())
                .content(userMessage)
                .build();

        GenerationParam param = GenerationParam.builder()
                .apiKey(apiKey)
                .model(model)
                .messages(Arrays.asList(systemMsg, userMsg))
                .responseFormat(ResponseFormat.from(responseFormat))
                .resultFormat(GenerationParam.ResultFormat.TEXT)
                .enableSearch(true)
                .build();

        return gen.call(param);
    }

    /**
     * 查询公司信息
     * @param apikey qwen apikey
     * @param model qwen模型
     * @param userMessage 用户输入
     */
    public static JSONObject callCompanyInfo(String apikey, String model, String userMessage) throws ApiException, NoApiKeyException, InputRequiredException {
        String sysMessage = "你是一个类似于爱企查的助手, 可以对公司进行深度的背景调查和真实性验证, 结果返回json, 如果没有查询到公司信息或公司信息是虚假的直接返回空的json: {}" +
                "【输出的Json Scheme】"
                + DashscopePrompt.companyInfoScheme;

        GenerationResult generationResult = callWithMessage(apikey, model, sysMessage, userMessage, ResponseFormat.JSON_OBJECT);

        List<GenerationOutput.Choice> choices = generationResult.getOutput().getChoices();
        StringBuilder res = new StringBuilder();
        for (GenerationOutput.Choice choice : choices) {
            res.append(choice.getMessage().getContent());
        }

        return JSONObject.parseObject(res.toString());
    }

    private static MultiModalConversationResult callOcr(String apiKey, String image, JsonObject schema) throws ApiException, NoApiKeyException, UploadFileException {
        MultiModalConversation conv = new MultiModalConversation();
        Map<String, Object> map = new HashMap<>();
        map.put("image", image);
        // 输入图像的最大像素阈值，超过该值图像会进行缩小，直到总像素低于max_pixels
        map.put("max_pixels", 8388608);
        // 输入图像的最小像素阈值，小于该值图像会进行放大，直到总像素大于min_pixels
        map.put("min_pixels", 3072);
        // 开启图像自动转正功能
        map.put("enable_rotate", true);

        MultiModalMessage userMessage = MultiModalMessage.builder()
                .role(Role.USER.getValue())
                .content(Collections.singletonList(map))
                .build();

        // 配置内置的OCR任务
        OcrOptions ocrOptions = OcrOptions.builder()
                .task(OcrOptions.Task.KEY_INFORMATION_EXTRACTION)
                .taskConfig(OcrOptions.TaskConfig.builder()
                        .resultSchema(schema)
                        .build())
                .build();

        MultiModalConversationParam param = MultiModalConversationParam.builder()
                .apiKey(apiKey)
                .model("qwen-vl-ocr-latest")  // model写死
                .message(userMessage)
                .ocrOptions(ocrOptions)
                .build();

        return conv.call(param);
    }

    public static LinkedTreeMap extractCardInfo(String apiKey, String image) throws ApiException, NoApiKeyException, UploadFileException {
        JsonObject resultSchema = new JsonObject();
        // 核心身份信息
        resultSchema.addProperty("name", "【必填】提取名片上的个人姓名。如果是个人名片或企业名片，此字段必须有值。");
        resultSchema.addProperty("company", "【可选】提取所属公司或组织的全称。若图中未出现公司信息（如纯个人名片），则留空。");
        resultSchema.addProperty("title", "提取个人的职位或头衔，例如 'CEO', 'Manager', '工程师' 等");
        resultSchema.addProperty("department", "提取具体的部门名称，若未提及可留空");

        // 联系方式
        resultSchema.addProperty("mobile", "提取手机号码，通常为 11 位数字或带有国家/地区代码");
        resultSchema.addProperty("phone", "提取固定电话或传真号码，若未提及可留空");
        resultSchema.addProperty("email", "提取电子邮箱地址，通常包含 '@' 符号");
        resultSchema.addProperty("website", "提取网址链接，若未提及可留空");
        resultSchema.addProperty("address", "提取完整的通讯地址或办公地点");
        resultSchema.addProperty("social_media", "提取微信、LinkedIn 或其他社交平台的账号信息，若未提及可留空");

        MultiModalConversationResult multiModalConversationResult = callOcr(apiKey, image, resultSchema);
        return (LinkedTreeMap) multiModalConversationResult.getOutput().getChoices().get(0).getMessage().getContent().get(0).get("ocr_result");
    }

    public static String createInfo(String apikey, String model, String userMessage) throws ApiException, NoApiKeyException, InputRequiredException {
        String sysMessage = "你是一个类似于爱企查的助手, 可以对公司进行深度的背景调查和真实性验证, 根据结果生成一个公司的简介, 如果该公司不存在, 只需要返回[暂无该公司介绍]";

        GenerationResult generationResult = callWithMessage(apikey, model, sysMessage, userMessage, ResponseFormat.TEXT);

        List<GenerationOutput.Choice> choices = generationResult.getOutput().getChoices();
        StringBuilder res = new StringBuilder();
        for (GenerationOutput.Choice choice : choices) {
            res.append(choice.getMessage().getContent());
        }

        return res.toString();
    }
}
