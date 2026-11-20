package com.sqx.modules.businessCard.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.modules.businessCard.entity.BusinessCard;
import com.sqx.modules.businessCard.vo.CityVo;
import com.sqx.modules.dataCenter.model.vo.CardLineVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 名片管理 Mapper 接口
 * </p>
 *
 * @author wuChang
 * @since 2024-02-26
 */
@Mapper
public interface BusinessCardDao extends BaseMapper<BusinessCard> {

    List<BusinessCard> getAppBusinessCardList(@Param("businessCard") BusinessCard businessCard,
                                              @Param("sortType") Integer sortType);

    List<CityVo> getProvinceList();

    List<BusinessCard> getBusinessCardList(@Param("userName")String userName, @Param("companyName")String companyName, @Param("cardStatus")Integer cardStatus,
                                           @Param("userType")Integer userType, @Param("realName")String realName, @Param("industryFirstName") String industryFirstName,
                                           @Param("auditStatus")Integer auditStatus);

    List<CityVo> getCityList(@Param("city") String city);

    List<CityVo> getDistrict(@Param("city") String city);

    List<BusinessCard> queryBusinessCardList(@Param("userId")Long userId, @Param("cardType") Integer cardType);

    List<BusinessCard> queryAllList(@Param("userId")Long userId,@Param("auditStatus")Integer auditStatus, @Param("cardType") Integer cardType);

    BusinessCard queryShowCard(@Param("userId")Long userId,@Param("userType")Integer userType);
    BusinessCard queryCompanyShowCard(@Param("userId")Long userId);

    int updateBrowseCount(@Param("cardId")Long cardId);

    BusinessCard queryStaffCard(@Param("userId")Long userId,@Param("parentId")Long parentId);

    List<Map<String, Object>> queryByPopularityValue(@Param("industrySecondName")String industrySecondName,@Param("companyProvince")String companyProvince,
                                                     @Param("companyCity")String companyCity,@Param("companyDistrict")String companyDistrict,
                                                     @Param("sortNum")Integer sortNum,@Param("userId")Long userId);

    List<BusinessCard> queryStaff(@Param("cardId") Long cardId);

    void decrQuota(@Param("cardId") Long cardId, @Param("num") Integer num);

    IPage<BusinessCard> searchBusinessCard(Page<BusinessCard> initPage, @Param("keyword") String keyword, @Param("userId") Long userId);

    void incrBrowseCount(@Param("cardId") Long cardId);

    void incrShareCount(@Param("cardId") Long cardId);

    void incrPopularityValue(@Param("cardId") Long cardId);

    List<BusinessCard> queryByPhone(@Param("cardId") Long cardId, @Param("phone") String phone);

    Integer countCardNum(@Param("cardType") Integer cardType, @Param("startTime")LocalDateTime startTime, @Param("endTime")LocalDateTime endTime);

    List<CardLineVO.CardLine> queryCardLine(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
