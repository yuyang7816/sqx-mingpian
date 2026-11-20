package com.sqx.modules.businessCard.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.Result;
import com.sqx.modules.businessCard.entity.BusinessCard;
import com.sqx.modules.businessCard.vo.CityVo;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 名片管理 服务类
 * @author wuChang
 * @since 2024-02-26
 */

public interface BusinessCardService extends IService<BusinessCard> {
    /**
     * 新增或修改名片管理
     */
    Result saveBusinessCard (BusinessCard businessCard);

    /**
     * 获取改名片管理列表
     */
    Result getBusinessCardList(JSONObject params);


    Result deleteBusinessCardById(Long cardId);

    Result getBusinessCardById(JSONObject params,Long userId);


    Result downOrUp(Long userId,Long carId, Integer type);

    Result examineCard(JSONObject params);

    Result getBusinessCard(Long userId);

    List<CityVo> getCity();

    Result getMyBusinessCardList(Long userId,JSONObject params);


    Result saveBusinessCard(Long userId,BusinessCard businessCard);

    Result updateBusinessCard(BusinessCard businessCard);

    /**
     * 根据用户id查询所有的名片
     * @param userId
     * @param params
     * @return
     */
    Result queryAllList(Long userId,JSONObject params);

    /**
     * 查询用户展示的名片信息
     * @param userId
     * @return
     */
    Result queryShowCard(Long userId);

    Result eidtShowCard(Long userId,Long cardId);

    Result addBrowseCount(Long cardId);

    int addBrowseCounts(Long cardId);

    Result queryCardByUserId(Long userId);

    BusinessCard queryUserShowCard(Long userId,Integer userType);


    Result saveStaffCard(Long userId, Long cardId, Long companyCardId);


    Result delStaff(Long userId,Long staffId);

    Result queryStaff(JSONObject params);

    Result queryCompanyCard(Long userId);

    Result cardHotList(JSONObject params);

    void getQrCode(Long cardId, HttpServletResponse response);

    BusinessCard info(Long cardId, Long userId);

    List<BusinessCard> queryByPhone(Long cardId, String phone);

    IPage<BusinessCard> searchBusinessCard(String keyword, Long userId, Integer page, Integer limit);

    Result delStaffCard(Long cardId, Long staffId);

    void editStyle(Long cardId, String cardStyleCode);

    Boolean userCardExists(Long userId);
}
