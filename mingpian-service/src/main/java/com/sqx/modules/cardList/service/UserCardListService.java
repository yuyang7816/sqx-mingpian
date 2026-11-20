package com.sqx.modules.cardList.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.Result;
import com.sqx.modules.cardList.entity.UserCardList;

import javax.servlet.http.HttpServletResponse;

/**
 * 用户名片夹(UserCardList)表服务接口
 *
 * @author makejava
 * @since 2024-05-07 17:06:02
 */
public interface UserCardListService extends IService<UserCardList>{


    Result collectCard(Long userId,UserCardList userCardList);

    Result queryMyCollect(Long userId, JSONObject params);

    Result updateCardRemark(JSONObject params);
    Result updateRemark(JSONObject params);

    Result queryDetailsById(String id);

    void shareCard(Long cardId, HttpServletResponse response);

    Result moveToGroups(JSONObject params);

    Result removeById(String id);

    Boolean isCollectCard(Long userId, Long cardId);

    void incrContactCount(Long userId, Long cardId);
}
