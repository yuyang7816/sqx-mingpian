package com.sqx.modules.businessCard.dao;

import com.sqx.modules.businessCard.entity.VisitCardRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 访问记录表(VisitCardRecord)表数据库访问层
 *
 * @author makejava
 * @since 2024-05-07 10:08:39
 */
@Mapper
public interface VisitCardRecordDao extends BaseMapper<VisitCardRecord>{

    List<Map<String,Object>> queryVisitRecord(@Param("userId")Long userId);

    int queryTodayVisitData(@Param("cardId")Long cardId,@Param("date")String date);

    List<Map<String, Object>> queryrecentlyVisitData(@Param("cardId")Long cardId,@Param("userId") Long userId);

    List<Map<String, Object>> queryVisitMe(@Param("userId")Long userId, @Param("cardId") Long cardId);
    List<Map<String, Object>> queryMyVisit(@Param("userId")Long userId);

    Integer countVisitor(@Param("cardId") Long cardId);

    Integer countViews(@Param("userId") Long userId);
}

